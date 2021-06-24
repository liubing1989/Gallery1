package com.example.gallery

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.gallery_fragment.*
import java.util.*
import kotlin.concurrent.schedule

class GalleryFragment : Fragment() {
    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.swipeInDicator->{
                refreshLayout.isRefreshing =true
                Timer().schedule(1000){
                    galleryViewModel.fetchData()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        val galleryAdapter = GalleryAdapter()
        recyclerView.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireActivity(),2)
        }
         galleryViewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GalleryViewModel::class.java)
        galleryViewModel.photoListLive.observe(viewLifecycleOwner, {
            galleryAdapter.submitList(it)
            refreshLayout.isRefreshing=false
        })
        galleryViewModel.photoListLive.value?:galleryViewModel.fetchData()
        refreshLayout.setOnRefreshListener {
            galleryViewModel.fetchData()
        }

    }

}