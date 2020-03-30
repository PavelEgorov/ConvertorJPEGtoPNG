package com.egorovsoft.convertorjpegtopng.mvp.model

import android.net.Uri

interface IConvertor {
    fun convertToPng(data: Uri): Uri
}