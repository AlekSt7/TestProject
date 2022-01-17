package com.example.testproject.ui.character_card

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.testproject.R

class CharacterFragment : Fragment() {

    private lateinit var characterViewModel: CharacterViewModel

    private lateinit var goneAnimation: Animation

    companion object{

        var CHARACTER_ID = "character_id"
        var CHARACTER_NAME = "character_name"

        var LOADING = 1
        var SUCCESS = 2
        var ERROR = 3

    }

    private var character_id = 0

    private lateinit var name: TextView
    private lateinit var status: TextView
    private lateinit var gender: TextView
    private lateinit var origin: TextView
    private lateinit var location: TextView
    private lateinit var type: TextView
    private lateinit var dimension: TextView
    private lateinit var episodes: TextView

    private lateinit var wrapper: LinearLayout

    private lateinit var indicator: View

    private lateinit var avatar: ImageView

    private lateinit var error_text: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = requireArguments().getString(CHARACTER_NAME)

        character_id = requireArguments().getInt(CHARACTER_ID)

        val root = inflater.inflate(R.layout.fragment_character, container, false)

        goneAnimation = AnimationUtils.loadAnimation(context, R.anim.gone_anim)

        characterViewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)

        wrapper = root.findViewById(R.id.wrapper)
        progressBar = root.findViewById(R.id.progress_bar)
        error_text = root.findViewById(R.id.error)
        avatar = root.findViewById(R.id.main_image)
        indicator = root.findViewById(R.id.indicator)
        name = root.findViewById(R.id.character_name)
        status = root.findViewById(R.id.status)
        gender = root.findViewById(R.id.gender)
        origin = root.findViewById(R.id.origin)
        location = root.findViewById(R.id.location)
        type = root.findViewById(R.id.type)
        dimension = root.findViewById(R.id.dimension)
        episodes = root.findViewById(R.id.episodes)

        progressBar.animation = goneAnimation
        error_text.animation = goneAnimation
        wrapper.animation = goneAnimation

        showProgress(LOADING)
        observeData()

        return root
    }

    fun observeData(){

        characterViewModel.fetchCharacterData(character_id).observe(viewLifecycleOwner, Observer {

            Glide.with(this)
                    .load(it?.getImage_url())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.error_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(avatar)

            name.setText(it?.getName())
            status.setText(it?.getStatus().toString() + " - " + it?.getSpecies())

            val bg = indicator.background.current as GradientDrawable

            when (it?.getStatus()) {
                "Alive" -> bg.setColor(Color.GREEN)
                "Dead" -> bg.setColor(Color.RED)
                "unknown" -> bg.setColor(Color.GRAY)
            }

            gender.setText(it?.getGender())
            origin.setText(it?.getOrigin()?.getName())

            val size: Int = it?.getEpisodes_urls()!!.size
            episodes.text = size.toString()

            characterViewModel.fetchLocationData(it).observe(viewLifecycleOwner, Observer {

                location.setText(it?.getName())
                type.setText(it?.getType())
                dimension.setText(it?.getDimension())

                showProgress(SUCCESS)

            })

        })

        characterViewModel.getThrowable().observe(viewLifecycleOwner, {
            showProgress(ERROR)
        })

    }

    private fun showProgress(state: Int){ //Метод для конфигурирования интерфейса в зависимости от текущего состояния данных в приложении

        when(state){
            LOADING -> {
                error_text.visibility = View.GONE
                error_text.startAnimation(goneAnimation)

                wrapper.visibility = View.GONE
                wrapper.startAnimation(goneAnimation)

                progressBar.visibility = View.VISIBLE
            }
            SUCCESS -> {
                progressBar.visibility = View.GONE
                progressBar.startAnimation(goneAnimation)

                wrapper.visibility = View.VISIBLE
            }
            ERROR -> {
                wrapper.visibility = View.GONE
                wrapper.startAnimation(goneAnimation)

                progressBar.visibility = View.GONE
                progressBar.startAnimation(goneAnimation)

                error_text.visibility = View.VISIBLE
            }
        }

    }

}