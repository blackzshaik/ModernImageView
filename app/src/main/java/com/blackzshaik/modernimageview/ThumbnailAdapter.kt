package com.blackzshaik.modernimageview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ThumbnailAdapter(
    val imagesList: Array<String>,
    val onClickThumbnail: (Bitmap) -> Unit,
    val onThumbnailMultiTouch: (Bitmap) -> Unit
) :RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>(){

    private val cameraDirectory = "/storage/emulated/0/DCIM/Screenshots"

    class ViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_image_list_item,parent,false))
    }

    override fun getItemCount() = 6

    var activePointerIdA = -1
    var activePointerIdB = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.thumbnail).apply {
            val bmpOptions = BitmapFactory.Options()
            bmpOptions.inJustDecodeBounds = false
            bmpOptions.outWidth = 128
            bmpOptions.outHeight = 128
            val bmp = BitmapFactory.decodeFile( "$cameraDirectory/${imagesList[position]}",bmpOptions )
            this.setImageBitmap(bmp)

//            setOnClickListener {
//                onClickThumbnail(bmp)
//            }

//            onTouchEvent()



            setOnTouchListener { v, event ->


                if (event.pointerCount > 1) {
//                    Log.d("RV_TAG","")
                    onThumbnailMultiTouch(bmp)
                } else {
                    // Single touch event
//                    onClickThumbnail(bmp)
                }

                true
            }
        }
    }
}