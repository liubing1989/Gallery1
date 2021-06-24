package com.example.gallery

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.photo_fragment.*
class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photo_fragment, container, false)
    }



    @SuppressLint("CheckResult")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shimmerLayoutPhoto.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        Glide.with(requireContext())
            .load(arguments?.getParcelable<PhotoItem>("photo")?.fullUrl)
            .placeholder(R.drawable.ic_baseline_photo_24)
            .listener(object :RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { shimmerLayoutPhoto?.stopShimmerAnimation() }
                }

            })
            .into(photoView)
    }

}