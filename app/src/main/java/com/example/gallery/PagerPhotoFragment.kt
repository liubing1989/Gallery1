package com.example.gallery

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.pager_photo_fragment.*
import kotlinx.android.synthetic.main.pager_photo_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val REQUEST_WRITE_EXTERNAL_STORAGE = 1

@Suppress("DEPRECATION")
class PagerPhotoFragment : Fragment() {
    private val TAG = "PagerPhotoFragment"

    companion object;

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
            viewPager2.adapter = this
            submitList(photoList)
        }
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                photoTag.text = getString(R.string.photo_tag, position + 1, photoList?.size)
            }
        })
        viewPager2.setCurrentItem(arguments?.getInt("photo_position") ?: 0, false)
        saveButton.setOnClickListener {
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {
                viewLifecycleOwner.lifecycleScope.launch{
                    savePhoto()
                }
            }
        }
    }

    private suspend fun savePhoto() {
        withContext(Dispatchers.IO){
            val holder =
                (viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(viewPager2.currentItem) as
                        PagerPhotoViewHolder
            val bitmap = holder.itemView.pagerphoto.drawable.toBitmap()
            val saveUrl =requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )?:run {
                MainScope().launch { Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show() }
                return@withContext
            }
            requireContext().contentResolver.openOutputStream(saveUrl).use {
                if(bitmap.compress(Bitmap.CompressFormat.JPEG,90,it)){
                   MainScope().launch { Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show() }
                }else{
                    MainScope().launch { Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show() }
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewLifecycleOwner.lifecycleScope.launch{
                        savePhoto()
                    }
                } else {
                    MainScope().launch { Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

}