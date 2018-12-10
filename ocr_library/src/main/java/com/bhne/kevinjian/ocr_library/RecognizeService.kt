package com.bhne.kevinjian.ocr_library

import android.content.Context
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.*
import java.io.File

class RecognizeService {


    companion object {
        interface ServiceListener {
            fun onResult(result: String)
        }

        fun recGeneral(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = GeneralParams()
            param.setDetectDirection(true)
            param.setVertexesLocation(true)
            param.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL)
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeGeneral(param, object : OnResultListener<GeneralResult> {
                override fun onResult(result: GeneralResult) {
                    val sb = StringBuilder()
                    for (wordSimple in result.wordList) {
                        val word = wordSimple as Word
                        sb.append(word.words)
                        sb.append("\n")
                    }
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recAccurate(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = GeneralParams()
            param.setDetectDirection(true)
            param.setVertexesLocation(true)
            param.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL)
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeAccurate(param, object : OnResultListener<GeneralResult> {
                override fun onResult(result: GeneralResult) {
                    val sb = StringBuilder()
                    for (wordSimple in result.wordList) {
                        val word = wordSimple as Word
                        sb.append(word.words)
                        sb.append("\n")
                    }
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recAccurateBasic(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = GeneralParams()
            param.setDetectDirection(true)
            param.setVertexesLocation(true)
            param.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL)
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeAccurateBasic(param, object : OnResultListener<GeneralResult> {
                override fun onResult(result: GeneralResult) {
                    val sb = StringBuilder()
                    for (wordSimple in result.wordList) {
                        sb.append(wordSimple.words)
                        sb.append("\n")
                    }
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }


        fun recGeneralBasic(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = GeneralBasicParams()
            param.setDetectDirection(true)
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeGeneralBasic(param, object : OnResultListener<GeneralResult> {
                override fun onResult(result: GeneralResult) {
                    val sb = StringBuilder()
                    for (wordSimple in result.wordList) {
                        sb.append(wordSimple.words)
                        sb.append("\n")
                    }
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recGeneralEnhanced(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = GeneralBasicParams()
            param.setDetectDirection(true)
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeGeneralEnhanced(param, object : OnResultListener<GeneralResult> {
                override fun onResult(result: GeneralResult) {
                    val sb = StringBuilder()
                    for (wordSimple in result.wordList) {
                        sb.append(wordSimple.words)
                        sb.append("\n")
                    }
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recWebimage(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = GeneralBasicParams()
            param.setDetectDirection(true)
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeWebimage(param, object : OnResultListener<GeneralResult> {
                override fun onResult(result: GeneralResult) {
                    val sb = StringBuilder()
                    for (wordSimple in result.wordList) {
                        val word = wordSimple
                        sb.append(word.words)
                        sb.append("\n")
                    }
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recBankCard(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = BankCardParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeBankCard(param, object : OnResultListener<BankCardResult> {
                override fun onResult(result: BankCardResult) {
                    val res = String.format("卡号：%s\n类型：%s\n发卡行：%s",
                            result.bankCardNumber,
                            result.bankCardType.name,
                            result.bankName)
                    listener.onResult(res)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recVehicleLicense(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeVehicleLicense(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recDrivingLicense(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeDrivingLicense(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recLicensePlate(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeLicensePlate(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recBusinessLicense(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeBusinessLicense(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recReceipt(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            param.putParam("detect_direction", "true")
            OCR.getInstance(ctx).recognizeReceipt(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recPassport(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizePassport(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recVatInvoice(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeVatInvoice(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recQrcode(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeQrcode(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recNumbers(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeNumbers(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recLottery(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeLottery(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recBusinessCard(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeBusinessCard(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recHandwriting(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeHandwriting(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }

        fun recCustom(ctx: Context, filePath: String, listener: ServiceListener) {
            val param = OcrRequestParams()
            param.putParam("templateSign", "")
            param.putParam("classifierId", 0)
            param.imageFile = File(filePath)
            OCR.getInstance(ctx).recognizeCustom(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    listener.onResult(result.jsonRes)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(error.message!!)
                }
            })
        }
    }
}