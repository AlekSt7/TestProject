package com.example.testproject.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.testproject.R


class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var favoutiresRvAdapter: FavoutiresRvAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        favoutiresRvAdapter = FavoutiresRvAdapter(context)

        favouritesViewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerView = root.findViewById(R.id.favourites_list)

        recyclerView.adapter = favoutiresRvAdapter

        favoutiresRvAdapter.setLongItemClickListener(object : FavoutiresRvAdapter.ItemLongClickListener {
            override fun onItemLongClick(position: Int, character_id: Int) {
                favouritesViewModel.delete(character_id)
            }
        })

        favouritesViewModel.getAll().observe(viewLifecycleOwner, Observer {
            favoutiresRvAdapter.setData(it)
        })

        return root

    }
}