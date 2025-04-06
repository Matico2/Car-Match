package com.example.carmatch.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class ImagePagerAdapter(
    private val context: Context,
    private val imagePaths: List<String>
) : PagerAdapter() {
    
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        Picasso.get().load(imagePaths[position]).into(imageView)
        
        container.addView(imageView)
        return imageView
    }
    
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }
    
    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
    
    override fun getCount(): Int {
        return imagePaths.size
    }
}
