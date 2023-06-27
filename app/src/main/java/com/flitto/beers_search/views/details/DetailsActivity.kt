package com.flitto.beers_search.views.details

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.flitto.beers_search.R
import com.flitto.beers_search.databinding.ActivityDetailsBinding
import com.flitto.beers_search.views.SearchActivity
import com.flitto.core.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsActivity : BaseActivity<ActivityDetailsBinding>(
    { ActivityDetailsBinding.inflate(it) }
) {
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_ITEM, SearchItem::class.java)
        } else {
            intent.getParcelableExtra(KEY_ITEM)
        }

        if (item != null) {
            setDetailsItemExceptBookmarked(item)
            setBookmarkedStateObserver(item.name)
            /* 북마크 상태 변화 반영 */
            binding.ivBookmark.setOnClickListener {
                if (isBookmarked(item.name)) {
                    removeBookmarkedFromList(item.name)
                    viewModel.isBookmarked(false)
                } else {
                    addBookmarkedToList(item.name)
                    viewModel.isBookmarked(true)
                }
            }
        }
    }

    /* Bookmarked State Observer */
    private fun setBookmarkedStateObserver(name: String) {
        lifecycleScope.launch {
            viewModel.bookmarkedState.collectLatest {
                Glide
                    .with(binding.ivBookmark)
                    .load(if (isBookmarked(name)) R.drawable.dobookmarks else R.drawable.unbookmarks)
                    .into(binding.ivBookmark)
                val intent = Intent(this@DetailsActivity, SearchActivity::class.java).apply {
                    putExtra("beerName", name)
                    putExtra("isBookmarkedString", isBookmarked(name).toString())
                }
                setResult(0, intent)
            }
        }
    }

    /* 기본 값 셋팅 */
    private fun setDetailsItemExceptBookmarked(item: SearchItem) {
        binding.tvBeerName.text = item.name
        binding.tvTagline.text = item.tagline
        binding.tvDescription.text = item.description
        Glide
            .with(binding.ivBeerImage)
            .load(item.image_url)
            .into(binding.ivBeerImage)
    }

    companion object {
        private const val KEY_ITEM = "key-item"
        fun newInstance(context: Context, searchItem: SearchItem?): Intent =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(KEY_ITEM, searchItem)
            }
    }
}
