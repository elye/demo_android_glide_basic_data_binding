package com.elyeproj.demoglide

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.elyeproj.demoglide.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), MyImageRequestListener.Callback {
    private lateinit var binding: ActivityMainBinding

    override fun onFailure(message: String?) {
        Toast.makeText(this, "Fail to load: $message", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(dataSource: String) {
        Toast.makeText(this, "Loaded from: $dataSource", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.imageUrl = ImageUrl(
            "https://images.pexels.com/photos/46239/salmon-dish-food-meal-46239.jpeg?auto=compress&cs=tinysrgb&h=50",
            "https://images.pexels.com/photos/46239/salmon-dish-food-meal-46239.jpeg?auto=compress&cs=tinysrgb&h=1000",
            this
        )
    }
}

data class ImageUrl(val fastLoadUrl: String, val fullImageUrl: String, val listener: MyImageRequestListener.Callback)

@BindingAdapter("android:src")
fun setImageUrl(view: ImageView, imageUrl: ImageUrl?) {
    imageUrl?.let {
                val requestOption = RequestOptions()
                .placeholder(R.drawable.placeholder).centerCrop()

        Glide.with(view.context).load(it.fullImageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(Glide.with(view.context)
                    .load(it.fastLoadUrl)
                    .apply(requestOption))
                .apply(requestOption)
                .listener(MyImageRequestListener(it.listener))
                .into(view)
    }
}
