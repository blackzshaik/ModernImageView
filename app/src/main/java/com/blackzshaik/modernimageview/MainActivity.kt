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

        val file = File("/storage/emulated/0/DCIM/Screenshots")
        if (file.list() != null){
            imagePath = "$cameraDirectory/${file.list()!![0]}"
            setupThumbnailAdapter(file.list()!!)
        }

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


    }

    var movePointThreshold = 0f
    @SuppressLint("ClickableViewAccessibility")
    private val onClickThumbnail =
    fun (bmp: Bitmap){
        fullScreenImageView.apply {
            visibility = View.VISIBLE
            emptyView.visibility = View.VISIBLE
            setImageBitmap(bmp)


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
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    val  onThumbnailMultiTouch =
    fun (bmp:Bitmap) {
        zoomView.setImageBitmap(bmp)
        zoomView.visibility = View.VISIBLE
        zoomViewHolder.visibility = View.VISIBLE
    }

    private val cameraDirectory = "/storage/emulated/0/DCIM/Screenshots"




    var lastC = 0.0
    val onMultiTouchListener = View.OnTouchListener { v, event ->
        Log.d("TAG_12.1","ACTIVITY ========================== onMultiTouchListener")
        if (event.pointerCount >1){

            if (!recyclerView.isLayoutSuppressed){
                recyclerView.suppressLayout(true)

                fullScreenDialog.zoomViewHolder.tag = v.tag
                fullScreenDialog.showDialog()
                Log.d("TAG_12","---------------after show dialog")

            }

            fullScreenDialog.zoomViewHolder.dispatchTouchEvent(event)

        }else{
            val bmp = (v.tag as String).createBitmap()
            if (bmp != null) {
                onClickThumbnail(bmp)
            }
        }


        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){

            if (recyclerView.isLayoutSuppressed){
                recyclerView.suppressLayout(false)
                fullScreenDialog.dismissDialog()
                Log.d("TAG_12","++++++++++++++++++++++++++++++after dismiss dialog")
            }

        }

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

            false
        }
        MotionEvent.ACTION_MOVE ->{


            Log.d("TAG2"," ACTION_MOVE ======== Y ${event.y}")

            if (event.y - startPoint != 0.0f && event.y != 0.0f ){
                val movePoint =  event.y - startPoint
                Log.d("TAG3" , "$movePointThreshold ==== ${movePoint <=movePointThreshold } ${movePoint >= (movePointThreshold * -1)}")
                if (movePoint <=movePointThreshold && movePoint >= (movePointThreshold * -1) ){

                    fullScreenImageView.y = movePoint
                }



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