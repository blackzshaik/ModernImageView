package com.blackzshaik.modernimageview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.media.Image
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import kotlin.math.sqrt

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {

    private lateinit var fullScreenImageView:ImageView

    private lateinit var emptyView: ImageView

    private lateinit var zoomView:ImageView

    private lateinit var zoomViewHolder:ConstraintLayout

    private  lateinit var recyclerView:RecyclerView

    private val fullScreenDialog = FullScreenDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fullScreenImageView = findViewById(R.id.fullScreenImageView)
        emptyView = findViewById(R.id.emptyView)
        zoomView = findViewById(R.id.zoomView)
        zoomViewHolder = findViewById(R.id.zoomViewHolder)
        recyclerView = findViewById(R.id.listOfImages)
        fullScreenDialog.createDialog(this)

        checkStoragePermission()
    }


    private fun checkStoragePermission() {
        if (isStoragePermissionGranted()) {
            storagePermissionGrantedAlready()
        } else {
            requestStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private val requestStoragePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            storagePermissionGrantedAlready()
        }

    }

    var imagePath = ""
    private fun storagePermissionGrantedAlready() {
//        val storagePath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM)?.absolutePath
//        Log.d("TAG1","00000000000000 - $storagePath")

        val file = File("/storage/emulated/0/DCIM/Screenshots")
        if (file.list() != null){
            imagePath = "$cameraDirectory/${file.list()!![0]}"
            setupThumbnailAdapter(file.list()!!)
        }



//        val contextWrapper = ContextWrapper(this)
//        val stPath = contextWrapper.getExternalFilesDir("DIRECTORY_DCIM")
//        Log.d("TAG1","00000000000000 - $stPath")
    }

    private fun setupThumbnailAdapter(list: Array<String>) {

        recyclerView.apply {
            adapter = ThumbnailAdapter(list,onClickThumbnail,onThumbnailMultiTouch,zoomView,zoomViewHolder,onMultiTouchListener)
            layoutManager = object :   LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false){
                override fun canScrollVertically(): Boolean {

                    return true
                }
            }




        }
//        findViewById<ConstraintLayout>(R.id.singleImage).findViewById<ImageView>(R.id.thumbnail).apply {
//            setOnTouchListener(onMultiTouchListener)
//            val bmpOptions = BitmapFactory.Options()
//            bmpOptions.inJustDecodeBounds = false
//            bmpOptions.outWidth = 128
//            bmpOptions.outHeight = 128
//            val bmp = BitmapFactory.decodeFile(imagePath,bmpOptions )
//            setImageBitmap(bmp)
//        }

    }

    var movePointThreshold = 0f
    @SuppressLint("ClickableViewAccessibility")
    private val onClickThumbnail =
    fun (bmp: Bitmap){
        fullScreenImageView.apply {
            visibility = View.VISIBLE
            emptyView.visibility = View.VISIBLE
            setImageBitmap(bmp)
//            setOnTouchListener(onTouchListener)
//            movePointThreshold =


            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height: Int = displayMetrics.heightPixels


            Log.d("TAG4","${height }")
            this.post {
                Log.d("TAG4","${this.height}")
                movePointThreshold = ((50.toDouble()/100) * this.height) .toFloat()
                emptyView.setOnTouchListener(onTouchListener)
            }

            Log.d("TAG4","${this.layoutParams.height}")






//            setOnDragListener { v, event ->
//                Log.d("TAG2", "11111111 ${event.action}")
//                when(event.action){
//                    DragEvent.ACTION_DRAG_ENTERED ->{
//                        this.animate().translationY(event.y)
//                        true
//                    }
//                    else -> {
//                        false
//                    }
//                }
//
//            }

        }
    }

    var activePointerIdA = -1
    var activePointerIdB = -1
    var initalScaleFactor = 1.25f

    var initialX:Float? = null
    var initialY:Float? = null

    var initialC: Double? = null

    var scaleFactor = 0.25F


    @SuppressLint("ClickableViewAccessibility")
    val  onThumbnailMultiTouch =
    fun (bmp:Bitmap) {
        zoomView.setImageBitmap(bmp)
        zoomView.visibility = View.VISIBLE
        zoomViewHolder.visibility = View.VISIBLE
//        zoomViewHolder.setOnTouchListener { v, event ->
////            if (event.pointerCount > 1) {
////                val bmpOptions = BitmapFactory.Options()
////                bmpOptions.inJustDecodeBounds = false
////                bmpOptions.outWidth = 128
////                bmpOptions.outHeight = 128
////                val bmp = BitmapFactory.decodeFile(v?.tag as String?, bmpOptions)
////                onThumbnailMultiTouch.invoke(bmp)
////
////            }
//
//
//            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
//                zoomView.scaleX = 0.0F
//                zoomView.scaleY = 0.0F
//                initialX = null
//                initialY = null
//                zoomView.visibility = View.GONE
//                zoomViewHolder.visibility = View.GONE
//
//
////
////            Log.d("TAG_10","MotionEvent ACTION_UP --------")
////
////            return@OnTouchListener false
//
//            }
//
//            activePointerIdA = event.getPointerId(0)
//
//
//            val (xA: Float, yA: Float) = event.findPointerIndex(activePointerIdA)
//                .let { pointerIndex ->
//                    event.getX(pointerIndex) to event.getY(pointerIndex)
//                }
//            Log.d("TAG-6", "xA = $xA")
//            Log.d("TAG-6", "yA = $yA")
//
//            if (event.pointerCount > 1) {
//
//                zoomView.pivotX = xA
//                zoomView.pivotY = yA
//                Log.d("TAG-6", "Multitouch event")
//
//                activePointerIdB = event.getPointerId(1)
//
//
//                val (xB: Float, yB: Float) = event.findPointerIndex(activePointerIdB)
//                    .let { pointerIndex ->
//                        event.getX(pointerIndex) to event.getY(pointerIndex)
//                    }
//                Log.d("TAG-6", "xB = $xB")
//                Log.d("TAG-6", "yB = $yB")
//
//                if (event.action == MotionEvent.ACTION_MOVE) {
//
//
//                    val xDifference = if (xA > xB) {
//                        xA - xB
//                    } else {
//                        xB - xA
//                    }
//
//                    val yDifference = if (yA > yB) {
//                        yA - yB
//                    } else {
//                        yB - yA
//                    }
//
//                    val a = xDifference.toInt()
//                    val b = yDifference.toInt()
//
//                    val cSqur = (a * a) + (b * b)
//                    val c = sqrt(cSqur.toDouble())
//
//                    if (initialC == null) {
//                        initialC = c + 50
//                        Log.d("TAG_8", "initialC ========= $initialC")
//                    }
//
//                    Log.d("TAG_8", "c ========= $c")
//
//                    if (initialX == null && initialY == null) {
//                        initialX = xDifference + 50f
//                        initialY = yDifference + 50f
//                        Log.d("TAG-7", "initial  = $initialX")
//                    }
//
//
//                    Log.d("TAG-7", "xDifference  = ${xDifference}")
//                    Log.d("TAG-7", "yDifference = ${yDifference}")
//
//
////                    if (xDifference > initialX!! && yDifference > initialY!!) {
//
//                    if (c > initialC!!) {
//
//                        val scaleFactor = initalScaleFactor + .20f
//
//                        if (scaleFactor <= 3.0F && lastC != c) {
//                            zoomView.scaleX = scaleFactor
//                            zoomView.scaleY = scaleFactor
//                            initalScaleFactor = scaleFactor
//                            lastC = c
//
//                            Log.d("TAG-7", "scale up ==== $scaleFactor")
//
//
//                        }
//
//                    } else {
//                        val scaleFactor = initalScaleFactor - .20f
//                        if (scaleFactor > 1.0F && lastC != c) {
//                            zoomView.scaleX = scaleFactor
//                            zoomView.scaleY = scaleFactor
//                            initalScaleFactor = scaleFactor
//                            lastC = c
//                            Log.d("TAG-7", "scale down ==== $scaleFactor")
//                        }
//                    }
//
//
//                }
//
//
//            }
//
//
//            return@setOnTouchListener true
//        }
    }

    private val cameraDirectory = "/storage/emulated/0/DCIM/Screenshots"




    var lastC = 0.0
    val onMultiTouchListener = View.OnTouchListener { v, event ->
        Log.d("TAG_12.1","ACTIVITY ========================== onMultiTouchListener")
        if (event.pointerCount >1){
//            val bmpOptions = BitmapFactory.Options()
//            bmpOptions.inJustDecodeBounds = false
//            bmpOptions.outWidth = 128
//            bmpOptions.outHeight = 128
//            val bmp = BitmapFactory.decodeFile(v?.tag as String?,bmpOptions )
//            onThumbnailMultiTouch.invoke(bmp)
            if (!recyclerView.isLayoutSuppressed){
                recyclerView.suppressLayout(true)

                fullScreenDialog.zoomViewHolder.tag = v.tag
                fullScreenDialog.showDialog()
                Log.d("TAG_12","---------------after show dialog")

            }

            fullScreenDialog.zoomViewHolder.dispatchTouchEvent(event)



//            v.setOnTouchListener(null)

        }


        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
//            zoomView.scaleX = 0.0F
//            zoomView.scaleY = 0.0F
//            initialX = null
//            initialY = null
//            zoomView.visibility = View.GONE
//            zoomViewHolder.visibility = View.GONE
            if (recyclerView.isLayoutSuppressed){
                recyclerView.suppressLayout(false)
                fullScreenDialog.dismissDialog()
                Log.d("TAG_12","++++++++++++++++++++++++++++++after dismiss dialog")
            }



//
//            Log.d("TAG_10","MotionEvent ACTION_UP --------")
//
//            return@OnTouchListener false

        }

//        activePointerIdA = event.getPointerId(0)
//
//
//        val (xA:Float , yA:Float) = event.findPointerIndex(activePointerIdA).let { pointerIndex ->
//            event.getX(pointerIndex) to event.getY(pointerIndex)
//        }
//        Log.d("TAG-6","xA = $xA")
//        Log.d("TAG-6","yA = $yA")
//
//        if (event.pointerCount > 1) {
//
//            zoomView.pivotX = xA
//            zoomView.pivotY = yA
//            Log.d("TAG-6", "Multitouch event")
//
//            activePointerIdB = event.getPointerId(1)
//
//
//            val (xB:Float , yB:Float) = event.findPointerIndex(activePointerIdB).let { pointerIndex ->
//                event.getX(pointerIndex) to event.getY(pointerIndex)
//            }
//            Log.d("TAG-6","xB = $xB")
//            Log.d("TAG-6","yB = $yB")
//
//            if(event.action == MotionEvent.ACTION_MOVE ) {
//
//
//                val xDifference = if (xA > xB) {
//                    xA - xB
//                } else {
//                    xB - xA
//                }
//
//                val yDifference = if (yA > yB) {
//                    yA - yB
//                } else {
//                    yB - yA
//                }
//
//                val a = xDifference.toInt()
//                val b = yDifference.toInt()
//
//                val cSqur = (a * a) + (b * b)
//                val c = sqrt(cSqur.toDouble())
//
//                if (initialC == null) {
//                    initialC = c + 50
//                    Log.d("TAG_8", "initialC ========= $initialC")
//                }
//
//                Log.d("TAG_8", "c ========= $c")
//
//                if (initialX == null && initialY == null) {
//                    initialX = xDifference + 50f
//                    initialY = yDifference + 50f
//                    Log.d("TAG-7", "initial  = $initialX")
//                }
//
//
//                Log.d("TAG-7", "xDifference  = ${xDifference}")
//                Log.d("TAG-7", "yDifference = ${yDifference}")
//
//
//
////                    if (xDifference > initialX!! && yDifference > initialY!!) {
//
//                    if (c > initialC!! ) {
//
//                        val scaleFactor = initalScaleFactor + .20f
//
//                        if (scaleFactor <= 3.0F &&  lastC != c) {
//                            zoomView.scaleX = scaleFactor
//                            zoomView.scaleY = scaleFactor
//                            initalScaleFactor = scaleFactor
//                            lastC = c
//
//                            Log.d("TAG-7", "scale up ==== $scaleFactor")
//
//
//                        }
//
//                    } else {
//                        val scaleFactor = initalScaleFactor - .20f
//                        if (scaleFactor > 1.0F  && lastC != c) {
//                            zoomView.scaleX = scaleFactor
//                            zoomView.scaleY = scaleFactor
//                            initalScaleFactor = scaleFactor
//                            lastC = c
//                            Log.d("TAG-7", "scale down ==== $scaleFactor")
//                        }
//                    }
//
//
//            }
//
//
//        } else {
//            // Single touch event
//            Log.d("TAG", "Single touch event")
//        }

//            scaleDetector.onTouchEvent(event)
        true
    }



    private var startPoint = 0.0f
    private var isMoving = false
    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener{ v,event ->

    when(event.action){

        MotionEvent.ACTION_DOWN -> {

                Log.d("TAG2"," ACTION_DOWN ======== Y ${event.y}")
                startPoint = event.y
                Log.d("TAG2"," ACTION_DOWN ======== rawY ${startPoint}")

            true
        }
        MotionEvent.ACTION_UP -> {
            Log.d("TAG2"," ACTION_UP ======== Y ${event.y}")
//            onBackPressed()
            false
        }
        MotionEvent.ACTION_MOVE ->{


            Log.d("TAG2"," ACTION_MOVE ======== Y ${event.y}")

            if (event.y - startPoint != 0.0f && event.y != 0.0f ){
                val movePoint =  event.y - startPoint
                Log.d("TAG3" , "$movePointThreshold ==== ${movePoint <=movePointThreshold } ${movePoint >= (movePointThreshold * -1)}")
                if (movePoint <=movePointThreshold && movePoint >= (movePointThreshold * -1) ){
//                    fullScreenImageView.animate().y(movePoint)
                    fullScreenImageView.y = movePoint
                }

//                if(event.y < startPoint){
//                        Log.d("TAG2", " ACTION_DOWN ======== 1st block $movePoint")
//                       fullScreenImageView.animate().yBy((event.y - startPoint))
//
//                }else{
//                    fullScreenImageView.animate().yBy(event.y )
//                    Log.d("TAG2"," ACTION_DOWN ======== 2nd block ${event.y - startPoint}")
//                    }


                }

            true
        }
        else -> {
            false
        }
    }



    }





    private fun isStoragePermissionGranted() =
            ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    override fun onBackPressed() {
        if (fullScreenImageView.isVisible){
            fullScreenImageView.setImageResource(0)
            fullScreenImageView.visibility = View.GONE
            fullScreenImageView.translationY = 0f
            emptyView.visibility = View.GONE
        }else{
            super.onBackPressed()
        }

    }
}