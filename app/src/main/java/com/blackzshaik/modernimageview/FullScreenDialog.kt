package com.blackzshaik.modernimageview

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.sqrt

class FullScreenDialog {

    private lateinit var dialog: Dialog

    val zoomView by lazy {
        dialog.findViewById<ImageView>(R.id.zoomView)
    }

    val  zoomViewHolder by lazy {
        dialog.findViewById<ConstraintLayout>(R.id.zoomViewHolder)
    }

    fun createDialog(context:Context):Dialog{
        dialog = Dialog(context,R.style.full_screen_dialog)
        dialog.setContentView(R.layout.zoom_view_dialog)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)


        return dialog
    }

    @SuppressLint("ClickableViewAccessibility")
    fun showDialog(){
        zoomViewHolder.setOnTouchListener(onMultiTouchListener)
        zoomViewHolder.visibility = View.VISIBLE
        zoomView.visibility = View.VISIBLE

        zoomView.setImageBitmap((zoomViewHolder.tag as String).createBitmap())

        dialog.show()

    }

    fun dismissDialog(){
        dialog.dismiss()
    }

    var activePointerIdA = -1
    var activePointerIdB = -1
    var initalScaleFactor = 1.25f

    var initialX:Float? = null
    var initialY:Float? = null

    var initialC: Double? = null

    var scaleFactor = 0.25F



    var lastC = 0.0
    @SuppressLint("ClickableViewAccessibility")
    val onMultiTouchListener = View.OnTouchListener { v, event ->
        Log.d("TAG_D1","---------------onMultiTouchListener")

        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
            zoomView.scaleX = 1.0F
            zoomView.scaleY = 1.0F
            initialX = null
            initialY = null

        }

        activePointerIdA = event.getPointerId(0)


        val (xA:Float , yA:Float) = event.findPointerIndex(activePointerIdA).let { pointerIndex ->
            event.getX(pointerIndex) to event.getY(pointerIndex)
        }
        Log.d("TAG-6","xA = $xA")
        Log.d("TAG-6","yA = $yA")

        if (event.pointerCount > 1) {

            zoomView.pivotX = xA
            zoomView.pivotY = yA
            Log.d("TAG-6", "Multitouch event")

            activePointerIdB = event.getPointerId(1)


            val (xB:Float , yB:Float) = event.findPointerIndex(activePointerIdB).let { pointerIndex ->
                event.getX(pointerIndex) to event.getY(pointerIndex)
            }
            Log.d("TAG-6","xB = $xB")
            Log.d("TAG-6","yB = $yB")

            if(event.action == MotionEvent.ACTION_MOVE ) {


                val xDifference = if (xA > xB) {
                    xA - xB
                } else {
                    xB - xA
                }

                val yDifference = if (yA > yB) {
                    yA - yB
                } else {
                    yB - yA
                }

                val a = xDifference.toInt()
                val b = yDifference.toInt()

                val cSqur = (a * a) + (b * b)
                val c = sqrt(cSqur.toDouble())

                if (initialC == null) {
                    initialC = c + 50
                    Log.d("TAG_8", "initialC ========= $initialC")
                }

                Log.d("TAG_8", "c ========= $c")

                if (initialX == null && initialY == null) {
                    initialX = xDifference + 50f
                    initialY = yDifference + 50f
                    Log.d("TAG-7", "initial  = $initialX")
                }


                Log.d("TAG-7", "xDifference  = ${xDifference}")
                Log.d("TAG-7", "yDifference = ${yDifference}")




                if (c > initialC!! ) {

                    val scaleFactor = initalScaleFactor + .20f

                    if (scaleFactor <= 3.0F &&  lastC != c) {
                        zoomView.scaleX = scaleFactor
                        zoomView.scaleY = scaleFactor
                        initalScaleFactor = scaleFactor
                        lastC = c

                        Log.d("TAG-7", "scale up ==== $scaleFactor")


                    }

                } else {
                    val scaleFactor = initalScaleFactor - .20f
                    if (scaleFactor > 1.0F  && lastC != c) {
                        zoomView.scaleX = scaleFactor
                        zoomView.scaleY = scaleFactor
                        initalScaleFactor = scaleFactor
                        lastC = c
                        Log.d("TAG-7", "scale down ==== $scaleFactor")
                    }
                }


            }


        } else {
            // Single touch event
            Log.d("TAG", "Single touch event")
        }

        true
    }
}