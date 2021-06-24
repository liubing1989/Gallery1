package com.example.gallery

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.pager_photo_fragment.*

class PagerPhotoFragment : Fragment() {

    companion object {
        fun newInstance() = PagerPhotoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pager_photo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val photoList = arguments?.getParcelableArrayList<PhotoItem>("photo_list")
        PagerPhotoListAdapter().apply {
            viewPager2.adapter= this
            submitList(photoList)
        }
        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                photoTag.text="${position+1}/${photoList?.size}"
            }
        })
        viewPager2.setCurrentItem(arguments?.getInt("photo_position")?:0,false)
    }

}