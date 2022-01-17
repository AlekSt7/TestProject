package com.example.testproject.ui.main

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.testproject.R
import com.example.testproject.data.model.CharacterModel

class RvAdapter(private val context: Context?): RecyclerView.Adapter<RvAdapter.RvViewHolder>() {

    private var characters: MutableList<CharacterModel?>? = null
    private var layoutPath = -1
    private var animationPath = -1
    private val mInflater: LayoutInflater
    private var mClickListener: ItemClickListener? = null
    private var mLongClickListener: ItemLongClickListener? = null
    private var mUpdateListListener: UpdateListListener? = null

    init {
        mInflater = LayoutInflater.from(context)
    }

    inner class RvViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        val name: TextView
        val location: TextView
        val origin: TextView
        val status: TextView
        val preview: ImageView
        val indicator: View

        override fun onClick(view: View) {
            mClickListener?.onItemClick(
                    view, adapterPosition,
                    characters!![adapterPosition]!!.getId(),
                    characters!![adapterPosition]!!.getName())
        }

        override fun onLongClick(view: View?): Boolean {
            mLongClickListener?.onItemLongClick(
                view, adapterPosition,
                characters!![adapterPosition]!!.getId(),
                characters!![adapterPosition]!!
            )
            return true
        }

        init {
            name = itemView.findViewById(R.id.character_name)
            origin = itemView.findViewById(R.id.origin)
            location = itemView.findViewById(R.id.location)
            preview = itemView.findViewById(R.id.preview)
            status = itemView.findViewById(R.id.status)
            indicator = itemView.findViewById(R.id.indicator)
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

    }

    fun setData(character: MutableList<CharacterModel?>?) {
        characters = character
        mUpdateListListener?.onUpdate()
        notifyDataSetChanged()
    }

    fun updateData(character: List<CharacterModel?>?) {
        this.characters?.addAll(character!!)
        mUpdateListListener?.onUpdate()
        notifyItemInserted(lastPosition + 1)
    }

    fun onError(t: Throwable) {
        mUpdateListListener?.onError(t)
    }

    fun setLayout(layoutPath: Int) {
        this.layoutPath = layoutPath
    }

    fun setAnimation(animationPath: Int) {
        this.animationPath = animationPath
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        val view: View = mInflater.inflate(layoutPath, parent, false)
        return RvViewHolder(view)
    }

    var lastPosition = 0
    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {

        holder.name.setText(characters!![position]?.getName())
        holder.origin.setText(characters!![position]?.getOrigin()!!.getName())
        holder.location.setText(characters!![position]?.getLocation()!!.getName())
        val status: String? = characters!![position]?.getStatus()
        val species: String? = characters!![position]?.getSpecies()
        holder.status.text = "$status - $species"

        Glide.with(context!!)
            .load(characters!![position]?.getImage_url())
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.error_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.preview)

        val bg = holder.indicator.background.current as GradientDrawable
        when (status) {
            "Alive" -> bg.setColor(Color.GREEN)
            "Dead" -> bg.setColor(Color.RED)
            "unknown" -> bg.setColor(Color.GRAY)
        }
        if (position > lastPosition && animationPath != -1) {
            lastPosition = position
            val animation = AnimationUtils.loadAnimation(
                context, animationPath
            )
            holder.itemView.startAnimation(animation)
        }

        /**
         * Вызываем callback с реализацией обновления данных в Rv.
         */
        if (position == itemCount - 1) { //Если текущая позиция на единицу меньше от общего количества элементов списка
            mUpdateListListener?.onUpdating()
        }
    }

    override fun getItemCount(): Int {
        return if (characters != null) {
            characters!!.size
        } else { return 0 }
    }

    fun getData(): MutableList<CharacterModel?>? {return characters}

    fun setOnItemClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    fun setItemLongClickListener(longItemLongClickListener: ItemLongClickListener?) {
        mLongClickListener = longItemLongClickListener
    }

    fun setOnUpdateListListener(updateListListener: UpdateListListener?) {
        mUpdateListListener = updateListListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int, character_id: Int, character_name: String?)
    }

    interface ItemLongClickListener{
        fun onItemLongClick(view: View?, position: Int, character_id: Int, characterModel: CharacterModel)
    }

    interface UpdateListListener {
        fun onUpdate()
        fun onUpdating()
        fun onError(throwable: Throwable)
    }
}