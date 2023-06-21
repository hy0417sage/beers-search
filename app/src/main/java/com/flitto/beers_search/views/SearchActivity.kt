package com.flitto.beers_search.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.flitto.beers_search.R
import com.flitto.beers_search.databinding.ActivityMainBinding
import com.flitto.beers_search.views.details.DetailsActivity
import com.flitto.core.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivityMainBinding>(
    { ActivityMainBinding.inflate(it) }
) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private val searchAdapter: SearchAdapter = SearchAdapter(
        onClick = {
            run {
                if (isFinishing)
                    return@run
                getResult.launch(DetailsActivity.newInstance(this, it))
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = searchAdapter

        getResult()
        setSearchView()
        setUiStateObserver()
        setSearchItemFlowObserver()
    }

    private fun setSearchView() {
        /* searchView(검색 UI) Setting */
        binding.svSearch.setIconifiedByDefault(false)
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.run {
                    viewModel.searchBeers(this)
                    binding.rvSearch.scrollToPosition(0)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setSearchItemFlowObserver() {
        /* 아이템 Observer */
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                viewModel.setProgressBar(false)
                searchAdapter.submitData(it)
            }
        }
    }

    private fun setUiStateObserver() {
        /* UIState Observer */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    /* 초기 가이드 메시지 */
                    if (uiState.isGuideMessageVisible) {
                        binding.textViewGuide.visibility = View.VISIBLE
                    } else {
                        binding.textViewGuide.visibility = View.GONE
                    }
                    /* ProgressBar */
                    if (uiState.isLoading) {
                        showProgressBar()
                    } else {
                        hideProgressBar()
                    }
                }
            }
        }

        /* 페이징 Error handling */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchAdapter.loadStateFlow.collectLatest {
                    when (val currentState = it.refresh) {
                        is LoadState.Error -> {
                            when (currentState.error) {
                                is UnknownHostException -> {
                                    binding.rvSearch.visibility = View.GONE
                                    binding.textViewGuide.setText(R.string.search_paging_network_message)
                                    binding.textViewGuide.visibility = View.VISIBLE
                                    viewModel.setProgressBar(false)
                                }

                                is IndexOutOfBoundsException -> {
                                    binding.rvSearch.visibility = View.GONE
                                    binding.textViewGuide.setText(R.string.search_paging_no_search_message)
                                    binding.textViewGuide.visibility = View.VISIBLE
                                    viewModel.setProgressBar(false)
                                }

                                else -> {
                                    binding.rvSearch.visibility = View.GONE
                                    binding.textViewGuide.setText(R.string.search_paging_error_message)
                                    binding.textViewGuide.visibility = View.VISIBLE
                                    viewModel.setProgressBar(false)
                                }
                            }
                        }
                        /** 검색 결과 없음을 알려주는 부분 */
                        is LoadState.NotLoading -> {
                            if (searchAdapter.itemCount == 0) {
                                Log.d("NotLoading", "${searchAdapter.itemCount}")
                                binding.rvSearch.visibility = View.GONE
                                binding.textViewGuide.setText(R.string.search_paging_no_search_message)
                                binding.textViewGuide.visibility = View.VISIBLE
                                viewModel.setProgressBar(false)
                            }
                        }
                        else -> {
                            binding.textViewGuide.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    /** 북마크 변경 여부를 체크하여 검색 화면에 반영하는 함수 */
    private fun getResult() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 0) {
                val beerName = it.data?.getStringExtra("beerName")
                val isBookmarkedString = it.data?.getStringExtra("isBookmarkedString")
                if (beerName != null) {
                    searchAdapter.bookmarkChange(beerName, isBookmarkedString.toBoolean())
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.rvSearch.visibility = View.GONE
        binding.constraintLayoutProgressBarTotal.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.rvSearch.visibility = View.VISIBLE
        binding.constraintLayoutProgressBarTotal.visibility = View.GONE
    }
}