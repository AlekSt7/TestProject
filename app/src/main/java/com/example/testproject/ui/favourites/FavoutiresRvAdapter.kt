package com.example.testproject.ui.favourites

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testproject.R
import com.example.testproject.data.db.CharacterEntity

class FavoutiresRvAdapter(private var context: Context?): RecyclerView.Adapter<FavoutiresRvAdapter.MyViewHolder>() {

    private var characters: List<CharacterEntity?>? = null

    private var mLongClickListener: ItemLongClickListener? = null

    inner class MyViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView), View.OnLongClickListener {

        val name: TextView

        init{
            name = itemView.findViewById(R.id.name)
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(view: View?): Boolean {
            mLongClickListener?.onItemLongClick(
                adapterPosition,
                characters?.get(adapterPosition)!!.id
            )
            if(mLongClickListener != null) delete(adapterPosition)
            return true
        }
    }

    fun setData(c: List<CharacterEntity?>?){
        characters = c
        notifyDataSetChanged()
    }

    private fun delete(position: Int){
        (characters as ArrayList<CharacterEntity?>)?.remove(characters?.get(position))
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.favourite_row, parent, false)
        return MyViewHolder(view)
    }

    var lastPosition = 0
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = characters!!.get(position)?.name

        if (position > lastPosition) {
            lastPosition = position
            val animation = AnimationUtils.loadAnimation(
                    context, R.anim.rv_anim
            )
            holder.itemView.startAnimation(animation)
        }

    }

    override fun getItemCount(): Int {
        return if (characters != null) {
            characters!!.size
        } else { return 0 }
    }

    interface ItemLongClickListener{
        fun onItemLongClick(position: Int, character_id: Int)
    }

    fun setLongItemClickListener(longItemLongClickListener: ItemLongClickListener?) {
        mLongClickListener = longItemLongClickListener
    }

}
