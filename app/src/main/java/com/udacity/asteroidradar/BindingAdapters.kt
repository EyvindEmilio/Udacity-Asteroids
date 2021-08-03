package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.main.AsteroidApiStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, asteroid: Asteroid) {
    if (asteroid.isPotentiallyHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription =
            imageView.context.getString(R.string.hazardous_image_state)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription =
            imageView.context.getString(R.string.no_hazardous_image_state)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, pictureOfDay: PictureOfDay?) {
    if (pictureOfDay != null) {
        pictureOfDay.url.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            Picasso.with(imgView.context)
                .load(imgUri)
                .error(R.drawable.ic_baseline_broken_image_24)
                .placeholder(R.drawable.loading_animation)
                .into(imgView, object : Callback {
                    override fun onSuccess() {
                        imgView.contentDescription =
                            imgView.context.getString(
                                R.string.nasa_picture_of_day_content_description_format,
                                pictureOfDay.title
                            )

                    }

                    override fun onError() {
                        imgView.contentDescription =
                            imgView.context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
                    }
                })

        }
    } else {
        imgView.contentDescription =
            imgView.context.getString(R.string.image_of_the_day)
    }
}

@BindingAdapter("loaderStatus")
fun bindImage(progressBar: ProgressBar, status: AsteroidApiStatus?) {
    if (status == AsteroidApiStatus.LOADING) {
        progressBar.visibility = View.VISIBLE
    } else {
        progressBar.visibility = View.GONE
    }
}