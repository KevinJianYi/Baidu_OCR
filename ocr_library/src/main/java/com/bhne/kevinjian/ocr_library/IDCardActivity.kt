package com.bhne.kevinjian.ocr_library

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.bhne.kevinjian.ocr_ui_library.ocr.ui.camera.CameraActivity
import com.bhne.kevinjian.ocr_ui_library.ocr.ui.camera.CameraNativeHelper
import com.bhne.kevinjian.ocr_ui_library.ocr.ui.camera.CameraView
import java.io.File

class IDCardActivity : AppCompatActivity() {
    private val REQUEST_CODE_PICK_IMAGE_FRONT = 201
    private val REQUEST_CODE_PICK_IMAGE_BACK = 202
    private val REQUEST_CODE_CAMERA = 102

    private var infoTextView: TextView? = null
    private var alertDialog: AlertDialog.Builder? = null

    private fun checkGalleryPermission(): Boolean {
        val ret = ActivityCompat.checkSelfPermission(this@IDCardActivity, Manifest.permission
                .READ_EXTERNAL_STORAGE)
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@IDCardActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000)
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idcard)
        alertDialog = AlertDialog.Builder(this)
        infoTextView = findViewById<View>(R.id.info_text_view) as TextView

        //  初始化本地质量控制模型,释放代码在onDestory中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(this, OCR.getInstance(this).license,
                object : CameraNativeHelper.CameraNativeInitCallback {
                    override fun onError(errorCode: Int, e: Throwable) {
                        val msg: String
                        when (errorCode) {
                            CameraView.NATIVE_SOLOAD_FAIL -> msg = "加载so失败，请确保apk中存在ui部分的so"
                            CameraView.NATIVE_AUTH_FAIL -> msg = "授权本地质量控制token获取失败"
                            CameraView.NATIVE_INIT_FAIL -> msg = "本地质量控制"
                            else -> msg = errorCode.toString()
                        }
                        infoTextView!!.text = "本地质量控制初始化错误，错误原因： $msg"
                    }

                    override fun onSuccess() {

                    }
                })

        findViewById<View>(R.id.gallery_button_front).setOnClickListener {
            if (checkGalleryPermission()) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE_FRONT)
            }
        }

        findViewById<View>(R.id.gallery_button_back).setOnClickListener {
            if (checkGalleryPermission()) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE_BACK)
            }
        }

        // 身份证正面拍照
        findViewById<View>(R.id.id_card_front_button).setOnClickListener {
            val intent = Intent(this@IDCardActivity, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    CROUtil.getSaveFile(application).getAbsolutePath())
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }

        // 身份证正面扫描
        findViewById<View>(R.id.id_card_front_button_native).setOnClickListener {
            val intent = Intent(this@IDCardActivity, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    CROUtil.getSaveFile(application).getAbsolutePath())
            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                    true)
            // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
            // 请手动使用CameraNativeHelper初始化和释放模型
            // 推荐这样做，可以避免一些activity切换导致的不必要的异常
            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                    true)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }

        // 身份证反面拍照
        findViewById<View>(R.id.id_card_back_button).setOnClickListener {
            val intent = Intent(this@IDCardActivity, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    CROUtil.getSaveFile(application).getAbsolutePath())
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }

        // 身份证反面扫描
        findViewById<View>(R.id.id_card_back_button_native).setOnClickListener {
            val intent = Intent(this@IDCardActivity, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    CROUtil.getSaveFile(application).getAbsolutePath())
            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                    true)
            // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
            // 请手动使用CameraNativeHelper初始化和释放模型
            // 推荐这样做，可以避免一些activity切换导致的不必要的异常
            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                    true)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }
    }

    private fun recIDCard(idCardSide: String, filePath: String) {
        val param = IDCardParams()
        param.imageFile = File(filePath)
        // 设置身份证正反面
        param.idCardSide = idCardSide
        // 设置方向检测
        param.isDetectDirection = true
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.imageQuality = 20

        OCR.getInstance(this).recognizeIDCard(param, object : OnResultListener<IDCardResult> {
            override fun onResult(result: IDCardResult?) {
                if (result != null) {
                    alertText("", result.toString())
                }
            }

            override fun onError(error: OCRError) {
                alertText("", error.message!!)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE_FRONT && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data
            val filePath = getRealPathFromURI(uri)
            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath)
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE_BACK && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data
            val filePath = getRealPathFromURI(uri)
            recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath)
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)
                val filePath = CROUtil.getSaveFile(applicationContext).getAbsolutePath()
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT == contentType) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath)
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK == contentType) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath)
                    }
                }
            }
        }
    }

    private fun alertText(title: String, message: String) {
        this.runOnUiThread {
            alertDialog!!.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                    .show()
        }
    }

    private fun getRealPathFromURI(contentURI: Uri?): String {
        val result: String
        val cursor = contentResolver.query(contentURI!!, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    override fun onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release()
        super.onDestroy()
    }

}