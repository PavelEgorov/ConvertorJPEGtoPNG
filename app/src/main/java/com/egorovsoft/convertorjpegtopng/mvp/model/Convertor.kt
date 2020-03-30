package com.egorovsoft.convertorjpegtopng.mvp.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.egorovsoft.convertorjpegtopng.ui.App
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class Convertor: IConvertor {

    override fun convertToPng(data: Uri): Uri {
        val bitmap = getBitmap(data)

        val dirImage = File(Environment.getDownloadCacheDirectory(),"image");
        val convertedImage = File(dirImage,"converted.png");
        val outStream = FileOutputStream(convertedImage);

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)

        outStream.flush()
        outStream.close()

        /// Обращение к контексту плохое, скорее всего нужно запросить через презентер, или передать.
        /// но идеи как правильно реализовать пока нету. Нужно посмотреть интернет.. Если успею исправить
        val uri = FileProvider.getUriForFile(App.instance.baseContext, "com.egorovsoft.convertorjpegtopng", convertedImage)

        return uri
    }

    fun getBitmap(uri: Uri): Bitmap{

        val bitmap = BitmapFactory.decodeFile(uri.path)

        /// Не нужно, т.к. BitmapFactory делает все тоже самое

//        val conn = URL(uri.path).openConnection()
//        conn.connect()
//
//
//        val inputStream = conn.getInputStream()
//        val bis = BufferedInputStream(inputStream, 8192)
//        val bitmap = BitmapFactory.decodeStream(bis)
//
//        bis?.let {
//            it.close()
//        }
//
//        inputStream?.let {
//            it.close()
//        }

        return bitmap
    }
}