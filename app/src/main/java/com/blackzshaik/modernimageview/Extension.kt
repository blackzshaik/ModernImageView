package com.blackzshaik.modernimageview

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun String.createBitmap():Bitmap?{
    val bmpOptions = BitmapFactory.Options()
    bmpOptions.inJustDecodeBounds = false
    bmpOptions.outWidth = 128
    bmpOptions.outHeight = 128
    return BitmapFactory.decodeFile(this ,bmpOptions )
}