package com.blackzshaik.modernimageview

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class ZoomImageView (context: Context,attributeSet: AttributeSet? = null,defStyle:Int = 0):AppCompatImageView(context,attributeSet,defStyle){


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.pointerCount!! > 1){

        }

        return true
    }


}