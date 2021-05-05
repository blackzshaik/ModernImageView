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



    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.thumbnail).apply {

            val path = "$cameraDirectory/${imagesList[position]}"
            val bmp = path.createBitmap()
            this.setImageBitmap(bmp)

            this.tag = path
            setOnTouchListener(onMultiTouchListener)


        }


    }

}