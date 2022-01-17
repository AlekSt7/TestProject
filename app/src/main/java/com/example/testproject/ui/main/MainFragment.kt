package com.example.testproject.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.testproject.R
import com.example.testproject.data.model.CharacterModel
import com.example.testproject.ui.character_card.CharacterFragment


/**
 * Главный фрагмент приложения со списком персонажей
 */

class MainFragment : Fragment() {

    private val mainViewModel by navGraphViewModels<MainViewModel>(R.id.mobile_navigation)

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvAdapter: RvAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var errorText: TextView

    var isDataRestore = false //Восстанавливаем ли данные RV

    companion object{
        var LOADING = 1
        var SUCCESS = 2
        var ERROR = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        val root = inflater.inflate(R.layout.fragment_main, container, false)

        progressBar = root.findViewById(R.id.progress)
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout)
        errorText = root.findViewById(R.id.error_text)
        recyclerView = root.findViewById(R.id.character_list)
        recyclerView.adapter = initRvAdapter() //Инициализируем адаптер для RecyclerView

        setListeners()

        /**
         * Инициализация данных в RV, если данные уже есть во ViewModel выгружаем их
         */
        if (mainViewModel.characters == null) {
            isDataRestore = false
            showProgress(LOADING)
            fetchData() //Кидаем запрос на получение первой страницы списка
        }
        else{
            isDataRestore = true
            rvAdapter.setData(mainViewModel.characters)
        }

        observeData() //Получаем данныые и обрабатываем состояние запроса

        return root
    }

    fun initRvAdapter(): RvAdapter{

        rvAdapter = RvAdapter(context)
        rvAdapter.setLayout(R.layout.character_row)
        rvAdapter.setAnimation(R.anim.rv_anim)

        return rvAdapter

    }


    fun fetchData() {
        showProgress(LOADING)
        mainViewModel.fetchCharactersData(mainViewModel.getNextPage()!!) //Извлекаем данные из запроса на получение страницы списка персонажей
    }


    /**
     * Реализуем callback-методы слушателей
     */
    fun setListeners(){

        rvAdapter.setItemLongClickListener(object: RvAdapter.ItemLongClickListener { //Тут добавляем персонажа в избранное
            override fun onItemLongClick(
                view: View?,
                position: Int,
                character_id: Int,
                characterModel: CharacterModel
            ) {
                mainViewModel.addToFavourite(characterModel.getName(), character_id)
                Toast.makeText(activity, resources.getString(R.string.add_to_favourites), Toast.LENGTH_SHORT).show()
            }
        })

        rvAdapter.setOnItemClickListener(object: RvAdapter.ItemClickListener { //Тут открываем основую страницу персонажа
            override fun onItemClick(view: View?, position: Int, character_id: Int, character_name: String?) {

                var bundle = Bundle()
                bundle.putInt(CharacterFragment.CHARACTER_ID, character_id)
                bundle.putString(CharacterFragment.CHARACTER_NAME, character_name)

                findNavController().navigate(R.id.navigation_character, bundle)

            }
        })

        /**
         * Callback, который вызывается при обновлении данных RV
         */
        rvAdapter.setOnUpdateListListener(object : RvAdapter.UpdateListListener {
            override fun onUpdate() { //Загрузка выполнена
                showProgress(SUCCESS)
            }

            override fun onUpdating() { //Загрузка выполняется
                mainViewModel.clearThrowable()
                isDataRestore = false
                if(mainViewModel.isLastPage()) { //Выполняем запрос, пока есть новые страницы
                    showProgress(LOADING)
                    fetchData() //Подгружаем в список новую страницу
                }
            }

            override fun onError(throwable: Throwable) { //Ошибка при загрузке данных
                showProgress(ERROR)
            }

        })

        swipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                mainViewModel.clearThrowable()
                fetchData()
            }

        })

    }

    /**
     * Подписываемся на получение данных с viewModel
     */
    private fun observeData(){

        mainViewModel.getApiModel().observe(viewLifecycleOwner, Observer {
            if (mainViewModel.isFirstPage()) {
                if(!isDataRestore) { //Если данные в ModelView пустые
                    rvAdapter.setData(it?.getCharacterModelList() as MutableList<CharacterModel?>) //Получаем первую страницу списка персонажей и надуваем её в RecyclerView
                }
            } else {
                if(!isDataRestore) { rvAdapter.updateData(it?.getCharacterModelList()) } //Добавляем новых персонажей, если первая страница уже загружена
            }
        })

        mainViewModel.getThrowable()?.observe(viewLifecycleOwner, Observer {
            if(it != null){
                rvAdapter.onError(it)
            }
        })

    }

    private fun showProgress(state: Int){ //Метод для конфигурирования интерфейса в зависимости от текущего состояния данных в приложении

            when(state){
                LOADING -> {
                    swipeRefreshLayout.isRefreshing = true
                }
                SUCCESS -> {
                    if(swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
                }
                ERROR -> {
                    if(swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(activity, resources.getString(R.string.error_text), Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onPause() {
        super.onPause()
        mainViewModel.characters = rvAdapter.getData() //Сохраняем данные во ViewModel
    }

}
