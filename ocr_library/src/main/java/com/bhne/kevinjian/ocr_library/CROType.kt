package com.bhne.kevinjian.ocr_library

enum class CROType(val code: Int,val content: String,val REQUEST_CODE:Int) {
    ID_CARD_FRONT(0, "身份证（正面）",100),
    ID_CARD_REVERSE_SIDE(1, "身份证（反面）",101),
    BANK_CARD(2,"银行卡",102),
    NUMBER_PLATE(3,"车牌",103),
    CHARACTERISTICA_UNIVERSA(4,"通用文字",104),
    PASSABLE_BILL(5,"通用票据",105),
    HAND_WRITTEN(6,"手写文本",106),
    BUSINESS_CARD(7,"名片",107),
    QRCODE(8,"二维码",108);


    fun getByType(code:Int): CROType? {
        for(type in values()){
            if(type.code == code){
                return  type
            }
        }
        return null
    }
}