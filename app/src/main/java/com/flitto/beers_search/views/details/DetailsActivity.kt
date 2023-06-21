package com.flitto.beers_search.views.details

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.bumptech.glide.Glide
import com.flitto.beers_search.R
import com.flitto.beers_search.databinding.ActivityDetailsBinding
import com.flitto.beers_search.views.SearchActivity
import com.flitto.core.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : BaseActivity<ActivityDetailsBinding>(
    { ActivityDetailsBinding.inflate(it) }
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(KEY_ITEM, SearchItem::class.java)
        }else{
            intent.getParcelableExtra(KEY_ITEM)
        }

        binding.tvBeerName.text = item?.name
        binding.tvTagline.text = item?.tagline
        binding.tvDescription.text = item?.description

        /* 맥주 이미지 */
        if (item?.image_url == "") {
            Glide
                .with(binding.ivBeerImage)
                .load(R.drawable.ic_baseline_image_not_supported_24)
                .into(binding.ivBeerImage)
        } else {
            Glide
                .with(binding.ivBeerImage)
                .load(item?.image_url)
                .into(binding.ivBeerImage)
        }

        /* 북마크 초기 이미지 */
        val bookmarkedState = if (item?.bookmarked == true) {
            R.drawable.dobookmarks
        } else {
            R.drawable.unbookmarks
        }
        Glide
            .with(binding.ivBookmark)
            .load(bookmarkedState)
            .into(binding.ivBookmark)

        /* 북마크 상태 변화 반영 */
        binding.ivBookmark.setOnClickListener {
            val bookmarkedState = item?.name?.let { it -> isBookmarked(it) }
            val isBookmarks = if (bookmarkedState == true) {
                removeBookmarkedFromList(item.name)
                R.drawable.unbookmarks
            } else {
                item?.let { it -> addBookmarkedToList(it.name) }
                R.drawable.dobookmarks
            }
            Glide
                .with(binding.ivBookmark)
                .load(isBookmarks)
                .into(binding.ivBookmark)
            val intent = Intent(this@DetailsActivity, SearchActivity::class.java).apply {
                putExtra("beerName", item?.name)
                putExtra("isBookmarkedString",
                    item?.name?.let { it -> isBookmarked(it).toString() })
            }
            setResult(0, intent)
        }
    }

    companion object {
        private const val KEY_ITEM = "key-item"
        fun newInstance(context: Context, searchItem: SearchItem?): Intent =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(KEY_ITEM, searchItem)
            }
    }
}