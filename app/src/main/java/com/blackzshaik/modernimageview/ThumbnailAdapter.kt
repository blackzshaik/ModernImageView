package com.blackzshaik.modernimageview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.sqrt

class ThumbnailAdapter(
    val imagesList: Array<String>,
    val onClickThumbnail: (Bitmap) -> Unit,
    val onThumbnailMultiTouch: (Bitmap) -> Unit,
    val zoomView: ImageView,
    val zoomViewHolder: ConstraintLayout,
    val onMultiTouchListener:View.OnTouchListener
) :RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>(){

    private val cameraDirectory = "/storage/emulated/0/DCIM/Screenshots"

    class ViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_image_list_item,parent,false))
    }

    override fun getItemCount() = 4

    var activePointerIdA = -1
    var activePointerIdB = -1
    var initalScaleFactor = 1.25f

    var initialX:Float? = null
    var initialY:Float? = null

    var initialC: Double? = null

    var scaleFactor = 0.25F


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.thumbnail).apply {
            val bmpOptions = BitmapFactory.Options()
            bmpOptions.inJustDecodeBounds = false
            bmpOptions.outWidth = 128
            bmpOptions.outHeight = 128
            val path = "$cameraDirectory/${imagesList[position]}"
            val bmp = BitmapFactory.decodeFile( path,bmpOptions )
            this.setImageBitmap(bmp)

//            setOnClickListener {
//                onClickThumbnail(bmp)
//            }

//            onTouchEvent()
            this.tag = path
            setOnTouchListener(onMultiTouchListener)




//            setOnTouchListener { v, event ->
//
//
//                if (event.pointerCount > 1) {
////                    Log.d("RV_TAG","")
////                    onThumbnailMultiTouch(bmp)
////                    onMultiTouchThumbnail(bmp)
//                    isImageZoomed = true
//
////                    zoomViewHolder.onInterceptTouchEvent(event)
//
//                    Log.d("TAG_9","sssss $event")
//
//                    theZoomLogic(v,event)
//
//                } else {
////                     Single touch event
////                    onClickThumbnail(bmp)
//                }
//
//                true
//            }
        }
//        zoomViewHolder.setOnTouchListener { v, event ->
//            Log.d("TAG_9","zzzzzzzzzzzzzzzz $event")
//            theZoomLogic(event)
//            return@setOnTouchListener true
//        }


    }


    @SuppressLint("ClickableViewAccessibility")
    fun onMultiTouchThumbnail(bmp:Bitmap){
        zoomView.setImageBitmap(bmp)
        zoomView.visibility = View.VISIBLE
        zoomView.scaleX = initalScaleFactor
        zoomView.scaleY = initalScaleFactor
        zoomViewHolder.visibility = View.VISIBLE




    }

    var isImageZoomed = false
    fun theZoomLogic(v:View,event: MotionEvent){
        Log.d("TAG_10","tzl $event")
        if (!isImageZoomed){
            return
        }
        if (event.action == MotionEvent.ACTION_UP){
            v.scaleX = 1.0F
            v.scaleY = 1.0F
            initialX = null
            initialY = null
            isImageZoomed = false
        }

        activePointerIdA = event.getPointerId(0)


        val (xA:Float , yA:Float) = event.findPointerIndex(activePointerIdA).let { pointerIndex ->
            event.getX(pointerIndex) to event.getY(pointerIndex)
        }
        Log.d("TAG-6","xA = $xA")
        Log.d("TAG-6","yA = $yA")

        if (event.pointerCount > 1) {

            v.pivotX = xA
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
                    initialC = c
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



//                    if (xDifference > initialX!! && yDifference > initialY!!) {
                if (c > initialC!!) {
                    val scaleFactor = initalScaleFactor + .10f

                    if (scaleFactor <= 3.0F) {
                        v.scaleX = scaleFactor
                        v.scaleY = scaleFactor
                        initalScaleFactor = scaleFactor

                        Log.d("TAG-7", "scale up ==== $scaleFactor")


                    }

                } else {
                    val scaleFactor = initalScaleFactor - .10f
                    if (scaleFactor > 1.0F) {
                        v.scaleX = scaleFactor
                        v.scaleY = scaleFactor
                        initalScaleFactor = scaleFactor
                        Log.d("TAG-7", "scale down ==== $scaleFactor")
                    }
                }
            }


        } else {
            // Single touch event
            Log.d("TAG", "Single touch event")
        }
    }
}