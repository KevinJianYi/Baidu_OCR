package com.bhne.kevinjian.ocr_library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.bhne.kevinjian.ocr_ui_library.ocr.ui.camera.CameraActivity
import com.bhne.kevinjian.ocr_ui_library.ocr.ui.camera.CameraNativeHelper
import com.bhne.kevinjian.ocr_ui_library.ocr.ui.camera.CameraView
import org.json.JSONObject
import java.io.File

class CROUtil {



    companion object {
        var idCardResultCallback:OnIDCardResultListener? = null
         fun initAccessToken(context:Activity) :String ?{
            var result:String ?= null
            OCR.getInstance(context).initAccessToken(object : OnResultListener<AccessToken> {
                override fun onResult(accessToken: AccessToken) {
                    val token = accessToken.accessToken
                }

                override fun onError(error: OCRError) {
                    error.printStackTrace()
                    result = error.message
                }
            }, context)
            return  result
        }


        /**
         * 用明文ak，sk初始化
         */
        fun initAccessTokenWithAkSk(context:Activity,ak:String, sk:String) :String ? {
            var result:String ?= null
            OCR.getInstance(context).initAccessTokenWithAkSk(object : OnResultListener<AccessToken> {
                override fun onResult(result: AccessToken) {
                    val token = result.accessToken
                }

                override fun onError(error: OCRError) {
                    error.printStackTrace()
                    result = error.message
                }
            }, context, ak, sk)
            return  result
        }

        fun initCameraNativeHelper(context: Activity,action:CROType,requestCode: Int){
            CameraNativeHelper.init(context,OCR.getInstance(context).license,object : CameraNativeHelper.CameraNativeInitCallback{
                override fun onError(errorCode: Int, e: Throwable?) {
                    val msg: String
                    when (errorCode) {
                        CameraView.NATIVE_SOLOAD_FAIL -> msg = "加载so失败，请确保apk中存在ui部分的so"
                        CameraView.NATIVE_AUTH_FAIL -> msg = "授权本地质量控制token获取失败"
                        CameraView.NATIVE_INIT_FAIL -> msg = "本地质量控制"
                        else -> msg = errorCode.toString()
                    }
                    Log.d("CROUtil",msg)                }

                override fun onSuccess() {
                    when(action){
                        CROType.ID_CARD_FRONT -> {
                            recognitionIdCardFront(context,requestCode)
                        }
                        CROType.ID_CARD_REVERSE_SIDE -> {
                            recognitionIdCardFront(context,requestCode)
                        }
                    }
                }
            })

        }

        fun recognitionOperation(action:CROType,context:Activity,requestCode: Int){
            when(action){
                CROType.ID_CARD_FRONT ->recognitionIdCardFront(context,requestCode)
                CROType.ID_CARD_REVERSE_SIDE -> recognitionIdCardReverseSide(context,requestCode)
                CROType.BANK_CARD -> recognitionBankCard(context,requestCode)
                CROType.NUMBER_PLATE -> recognitionNumberPlate(context,requestCode)
                CROType.CHARACTERISTICA_UNIVERSA -> recognitionCharacteristicaUniversa(context,requestCode)
                CROType.PASSABLE_BILL -> recognitionPassableBill(context,requestCode)
                CROType.HAND_WRITTEN -> recognitionHandWritten(context,requestCode)
                CROType.BUSINESS_CARD -> recognitionBusinessCard(context,requestCode)
                CROType.QRCODE -> recognitionQrcode(context,requestCode)

            }
        }

        fun recognitionIdCardFront(context:Activity,requestCode:Int){
//            val intent = Intent(context, CameraActivity::class.java)
//            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                    FileUtil.getSaveFile(context).absolutePath)
//            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
//                    true)
//            // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
//            // 请手动使用CameraNativeHelper初始化和释放模型
//            // 推荐这样做，可以避免一些activity切换导致的不必要的异常
//            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
//                    true)
//            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
//            context.startActivityForResult(intent, requestCode)

            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                   getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
            context.startActivityForResult(intent, requestCode)
        }

        fun recognitionIdCardReverseSide(context:Activity,requestCode:Int){
//            val intent = Intent(context, CameraActivity::class.java)
//            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                    FileUtil.getSaveFile(context).absolutePath)
//            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
//                    true)
//            // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
//            // 请手动使用CameraNativeHelper初始化和释放模型
//            // 推荐这样做，可以避免一些activity切换导致的不必要的异常
//            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
//                    true)
//            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK)
//            context.startActivityForResult(intent, requestCode)
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK)
            context.startActivityForResult(intent, requestCode)
        }

        fun recognitionBankCard(context:Activity,requestCode:Int){
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_BANK_CARD)
            context.startActivityForResult(intent, requestCode)
        }

        fun recognitionNumberPlate(context:Activity,requestCode:Int){
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL)
            context.startActivityForResult(intent, requestCode)
        }

        fun recognitionCharacteristicaUniversa(context:Activity,requestCode:Int){
//            val intent = Intent(context, CameraActivity::class.java)
//            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                    FileUtil.getSaveFile(context).absolutePath)
//            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                    CameraActivity.CONTENT_TYPE_GENERAL)
//            context.startActivityForResult(intent, requestCode)
            //高精度
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL)
            context.startActivityForResult(intent, requestCode)
        }

        fun recognitionPassableBill(context:Activity,requestCode:Int){
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL)
            context.startActivityForResult(intent, requestCode)
        }

        fun recognitionHandWritten(context:Activity,requestCode:Int){
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL)
            context.startActivityForResult(intent, requestCode)
        }

        fun recognitionBusinessCard(context:Activity,requestCode:Int){
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL)
            context.startActivityForResult(intent, requestCode)
        }

        fun recognitionQrcode(context:Activity,requestCode:Int){
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    getSaveFile(context).absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL)
            context.startActivityForResult(intent, requestCode)
        }

        fun recIDCard(context:Activity,contentType: String, filePath: String) {
            var idCardSide:String ? = null
            var idCardResult:String ? = null
            if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT == contentType) {
                idCardSide = IDCardParams.ID_CARD_SIDE_FRONT
            }else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK == contentType) {
                idCardSide = IDCardParams.ID_CARD_SIDE_BACK
            }
            val param = IDCardParams()
            param.imageFile = File(filePath)
            // 设置身份证正反面
            param.idCardSide = idCardSide
            // 设置方向检测
            param.isDetectDirection = true
            // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
            param.imageQuality = 20

            OCR.getInstance(context).recognizeIDCard(param, object : OnResultListener<IDCardResult> {
                override fun onResult(result: IDCardResult?) {
                    if (result != null) {
                        idCardResult = result!!.toString()
                        Log.d("CROUtil",idCardResult)
                        var obj = JSONObject()
                        if(idCardSide.equals(IDCardParams.ID_CARD_SIDE_FRONT)){
                            obj.put("name",result.name)
                            obj.put("gender",result.gender)
                            obj.put("ethnic",result.ethnic)
                            obj.put("birthday",result.birthday)
                            obj.put("address",result.address)
                            obj.put("idNumber",result.idNumber)
                        }else{
                            obj.put("issueAuthority",result.issueAuthority)
                            obj.put("signDate",result.signDate)
                            obj.put("expiryDate",result.expiryDate)
                        }

                        idCardResultCallback?.onResultStr(obj!!)
                    }
                }

                override fun onError(error: OCRError) {
                    idCardResult = error.message
                    idCardResultCallback?.onErrorMsg(idCardResult!!)
                }
            })
        }

        fun onRelease(context:Activity){
            //释放内存资源
            OCR.getInstance(context).release()
        }

        fun onCameraNativeHelperRelease(){
            CameraNativeHelper.release()
        }

        fun setIDCardResultListener(listener: OnIDCardResultListener){
            idCardResultCallback = listener
        }

        fun getSaveFile(context: Context): File {
            return File(context.filesDir, "pic.jpg")
        }
    }



    interface OnIDCardResultListener{
        fun onResultStr(result:JSONObject)
        fun onErrorMsg(erroeMsg:String)
    }
}