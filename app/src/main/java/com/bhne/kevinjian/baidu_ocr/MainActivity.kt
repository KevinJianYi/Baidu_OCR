package com.bhne.kevinjian.baidu_ocr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bhne.kevinjian.mylocation_library.LocationService
import com.bhne.kevinjian.ocr_library.CROType
import com.bhne.kevinjian.ocr_library.CROUtil
import com.bhne.kevinjian.ocr_library.CROUtil.Companion.recIDCard
import com.bhne.kevinjian.ocr_library.RecognizeService
import org.json.JSONArray
import org.json.JSONObject

//import com.bhne.kevinjian.ocr_ui_library.ocr.ui.camera.CameraActivity
//import com.bhne.kevinjian.ocr_ui_library.ocr.ui.camera.CameraNativeHelper

class MainActivity : AppCompatActivity(),AdapterView.OnItemClickListener,CROUtil.OnIDCardResultListener{

    companion object {
        private var alertDialog: AlertDialog.Builder? = null
        private var hasGotToken = false
        const val KEY_CONTENT_TYPE = "contentType"
    }

    private var typeGrid:GridView ?= null

    private var typeArray:Array<CROType> ?= null

    private var contentLayout:LinearLayout?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadType()
        LocationService.getLocation(this)
    }

    fun loadType(){
        alertDialog = AlertDialog.Builder(this)
        typeArray = CROType.values()
        typeGrid = findViewById(R.id.type_gv)
        typeGrid!!.adapter = GridAdapter(typeArray,this@MainActivity)
        typeGrid!!.onItemClickListener = this
        var initToken = CROUtil.initAccessToken(this)
        if(initToken == null){
            hasGotToken = true
//            var initResult = CROUtil.initCameraNativeHelper(applicationContext)
//            if(initResult != null){
//                infoPopText(initResult)
//            }
        }else{
            infoPopText(initToken)
            return
        }

        contentLayout = findViewById(R.id.content_layout)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (!checkTokenStatus()) {
            return
        }
        CROUtil.recognitionOperation(typeArray!![position],this,typeArray!![position].REQUEST_CODE)
    }

    private fun checkTokenStatus(): Boolean {
        if (!hasGotToken) {
            Toast.makeText(applicationContext, "token还未成功获取", Toast.LENGTH_LONG).show()
        }
        return hasGotToken
    }


    class GridAdapter(var data: Array<CROType>?, var context: Context) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var holder:ViewHolder
            var v : View
            if(convertView == null){
                holder = ViewHolder()
                v = LayoutInflater.from(context).inflate(R.layout.grid_item_layou,parent,false)
                holder.typeTitle = v.findViewById(R.id.type_title_tv)
                v.tag = holder
            }else{
                v = convertView
                holder = v.tag as ViewHolder
            }
            holder.typeTitle.text = data!![position].content
            return v
        }

        override fun getItem(position: Int): Any {
            return data!![position]
        }

        override fun getItemId(position: Int): Long {
            return  position.toLong()
        }

        override fun getCount(): Int {
            return data!!.size
        }
    }

    class ViewHolder{
        lateinit var typeTitle:TextView
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            CROUtil.initAccessToken(this)
        } else {
            Toast.makeText(applicationContext, "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 识别成功回调，通用文字识别
        if (requestCode == CROType.CHARACTERISTICA_UNIVERSA.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //recGeneralBasic
            RecognizeService.recAccurateBasic(this, CROUtil.getSaveFile(applicationContext).absolutePath , object : RecognizeService.Companion.ServiceListener {
                override fun onResult(result: String) {
                    infoPopText(result,requestCode)
                }
            })
        }

        // 识别成功回调，银行卡识别
        if (requestCode == CROType.BANK_CARD.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBankCard(this, CROUtil.getSaveFile(applicationContext).absolutePath, object : RecognizeService.Companion.ServiceListener {
                override fun onResult(result: String) {
                    infoPopText(result,requestCode)
                }
            })
        }

        // 识别成功回调，车牌识别
        if (requestCode == CROType.NUMBER_PLATE.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recLicensePlate(this, CROUtil.getSaveFile(applicationContext).absolutePath, object : RecognizeService.Companion.ServiceListener {
                override fun onResult(result: String) {
                    infoPopText(result,requestCode)
                }
            })
        }

        // 识别成功回调，通用票据识别
        if (requestCode == CROType.PASSABLE_BILL.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recReceipt(this, CROUtil.getSaveFile(applicationContext).absolutePath, object : RecognizeService.Companion.ServiceListener {
                override fun onResult(result: String) {
                    infoPopText(result,requestCode)
                }
            })
        }


        // 识别成功回调，二维码
        if (requestCode == CROType.QRCODE.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recQrcode(this, CROUtil.getSaveFile(applicationContext).absolutePath, object : RecognizeService.Companion.ServiceListener {
                override fun onResult(result: String) {
                    infoPopText(result,requestCode)
                }
            })
        }

        // 识别成功回调，手写
        if (requestCode == CROType.HAND_WRITTEN.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recHandwriting(this, CROUtil.getSaveFile(applicationContext).absolutePath, object : RecognizeService.Companion.ServiceListener {
                override fun onResult(result: String) {
                    infoPopText(result,requestCode)
                }
            })
        }

        // 识别成功回调，名片
        if (requestCode == CROType.BUSINESS_CARD.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBusinessCard(this, CROUtil.getSaveFile(applicationContext).absolutePath, object : RecognizeService.Companion.ServiceListener {
                override fun onResult(result: String) {
                    infoPopText(result,requestCode)
                }
            })
        }
        if (requestCode == CROType.ID_CARD_FRONT.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentType = data.getStringExtra(KEY_CONTENT_TYPE)
                val filePath = CROUtil.getSaveFile(applicationContext).absolutePath
                if (!TextUtils.isEmpty(contentType)) {
                    CROUtil.setIDCardResultListener(this)
                    recIDCard(this,contentType, filePath)
                }
            }
        }
        if (requestCode == CROType.ID_CARD_REVERSE_SIDE.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val contentType = data.getStringExtra(KEY_CONTENT_TYPE)
                val filePath = CROUtil.getSaveFile(applicationContext).absolutePath
                if (!TextUtils.isEmpty(contentType)) {
                    CROUtil.setIDCardResultListener(this)
                    recIDCard(this,contentType, filePath)

                }
            }
        }

    }

    fun loadCardInfo(info:JSONObject){
        if(contentLayout!!.childCount > 0){
            contentLayout!!.removeAllViews()
        }
        var obj = info
        if(!TextUtils.isEmpty(obj.optString("idNumber"))){
            var view:View  = LayoutInflater.from(this).inflate(R.layout.idcard_front_info_layout,null)
            var name:TextView = view.findViewById(R.id.name_info)
            name.text = obj.optString("name")
            var gender:TextView = view.findViewById(R.id.gender_info)
            gender.text = obj.optString("gender")
            var ethnic:TextView = view.findViewById(R.id.ethnic_info)
            ethnic.text = obj.optString("ethnic")
            var birthday :TextView = view.findViewById(R.id.birthday_info)
            birthday.text = obj.optString("birthday")
            var address: TextView = view.findViewById(R.id.address_info)
            address.text = obj.optString("address")
            var idNumber: TextView = view.findViewById(R.id.idNumber_info)
            idNumber.text = obj.optString("idNumber")
            contentLayout!!.addView(view)
        }else{
            var view:View  = LayoutInflater.from(this).inflate(R.layout.idcard_back_info_layout,null)
            var issueAuthority:TextView = view.findViewById(R.id.issueAuthority_info)
            issueAuthority.text = obj.optString("issueAuthority")
            var expiryDate:TextView = view.findViewById(R.id.expiry_date_info)
            expiryDate.text = obj.optString("signDate") + "-" +obj.optString("expiryDate")
            contentLayout!!.addView(view)
        }
    }

    override fun onResultStr(result: JSONObject) {
        loadCardInfo(result)
    }

    override fun onErrorMsg(erroeMsg: String) {
        alertText("",erroeMsg)

    }

    private fun infoPopText(result: String) {
        alertText("", result)
    }

    private fun infoPopText(result: String,requestCode:Int) {
        Log.d("Main",result)
        if(TextUtils.isEmpty(result) || result == null  || result.indexOf("error",0) != -1){
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
            return
        }
        if(contentLayout!!.childCount > 0){
            contentLayout!!.removeAllViews()
        }
        var obj:JSONObject? = null
        try {
            if(requestCode != CROType.BANK_CARD.REQUEST_CODE){
                obj = JSONObject(result)
            }
        }catch (e:Exception){
            e.printStackTrace()
            infoPopText(result)
            return
        }
        when(requestCode){
            CROType.NUMBER_PLATE.REQUEST_CODE ->{
                var colorbg: String = obj!!.optJSONObject("words_result").optString("color")
                var number:String = obj!!.optJSONObject("words_result").optString("number")
                var view:View  = LayoutInflater.from(this).inflate(R.layout.number_plate_info_layout,null)
                var cardLayout: CardView = view.findViewById(R.id.number_plate_cv)
                var numberContentLayout:RelativeLayout = view.findViewById(R.id.number_content_layout)
                var contentTv:TextView = view.findViewById(R.id.number_plate_content)
                contentTv.text = number
                if(colorbg == "blue" ){
                    cardLayout.setBackgroundColor( resources.getColor(R.color.color_blue_label))
                    contentTv.setBackgroundColor(resources.getColor(R.color.color_blue_label))
                }else if(colorbg == "green"){
                    cardLayout.setBackgroundColor( resources.getColor(R.color.color_green_label))
                    contentTv.setBackgroundColor(resources.getColor(R.color.color_green_label))
                }else if(colorbg == "yellow"){
                    cardLayout.setBackgroundColor( resources.getColor(R.color.color_yellow_label))
                    contentTv.setBackgroundColor(resources.getColor(R.color.color_yellow_label))
                    contentTv.setTextColor(resources.getColor(R.color.color_black_label))

                }else if(colorbg == "white" || colorbg == "unknown"){
                    cardLayout.setBackgroundColor( resources.getColor(R.color.color_white_label))
                    contentTv.setBackgroundColor(resources.getColor(R.color.color_white_label))
                    numberContentLayout.setBackgroundColor(resources.getColor(R.color.color_black_label))
                    contentTv.setTextColor(resources.getColor(R.color.color_black_label))
                }else if(colorbg == "black"){
                    cardLayout.setBackgroundColor( resources.getColor(R.color.color_black_label))
                    contentTv.setBackgroundColor(resources.getColor(R.color.color_black_label))
                }
                contentLayout!!.addView(view)
            }
            CROType.CHARACTERISTICA_UNIVERSA.REQUEST_CODE -> textResolve(obj!!)
            CROType.HAND_WRITTEN.REQUEST_CODE -> textResolve(obj!!)
            CROType.BUSINESS_CARD.REQUEST_CODE -> textResolve(obj!!)
            CROType.QRCODE.REQUEST_CODE -> textResolve(obj!!)
            CROType.PASSABLE_BILL.REQUEST_CODE -> textResolve(obj!!)
            CROType.BANK_CARD.REQUEST_CODE -> {
                var contentTextView = TextView(this)
                contentTextView.text = result
                contentTextView.setTextColor(resources.getColor(R.color.color_black_label))
                contentTextView.textSize = 18.0f
                contentTextView.gravity = Gravity.CENTER
                contentLayout!!.addView(contentTextView)
            }
        }
    }

    private fun  textResolve(obj:JSONObject){
        var num:Int = obj.optInt("words_result_num")
        var textList:JSONArray = obj.optJSONArray("words_result")
        var textContext = StringBuffer()
        for (i in 0..(textList.length() -1)){
            var textObj:JSONObject = textList.get(i) as JSONObject
            textContext.append(textObj.optString("words")).append("\n")
        }
        var contentTextView = TextView(this)
        contentTextView.text = textContext.toString()
        contentTextView.setTextColor(resources.getColor(R.color.color_black_label))
        contentTextView.textSize = 18.0f
        contentTextView.gravity = Gravity.CENTER
        contentLayout!!.addView(contentTextView)
    }

    private fun alertText(title: String, message: String) {
        this.runOnUiThread {
            alertDialog!!.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                    .show()
        }
    }

    override fun onDestroy() {
//        CROUtil.onCameraNativeHelperRelease()
        super.onDestroy()
        CROUtil.onRelease(this)
    }
}

