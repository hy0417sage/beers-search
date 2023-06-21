package com.flitto.beers_search.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flitto.beers_search.R
import com.flitto.beers_search.databinding.ItemSearchBinding
import com.flitto.core.SearchItem

class SearchAdapter(
    private val onClick: (SearchItem) -> Unit,
) : PagingDataAdapter<SearchItem, SearchAdapter.SearchViewHolder>(DIFF_CALLBACK) {

    class SearchViewHolder(
        private val binding: ItemSearchBinding,
        private val onClick: (SearchItem) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchItem) {
            binding.apply {
                tvBeerName.text = item.name
                if (item.image_url.isNotEmpty()) {
                    Glide
                        .with(ivBeer)
                        .load(item.image_url)
                        .into(ivBeer)
                } else {
                    Glide
                        .with(ivBeer)
                        .load(R.drawable.ic_baseline_image_not_supported_24)
                        .into(ivBeer)
                }

                clViewSearch.setOnClickListener {
                    onClick(item)
                }

                if (item.bookmarked) {
                    ivBookmark.visibility = View.VISIBLE
                } else {
                    ivBookmark.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            binding = ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            onClick = onClick,
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        item?.run {
            holder.bind(this)
        }
    }

    fun bookmarkChange(beerName: String, isBookmarked: Boolean){
        val snapshotSearchItem = this@SearchAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.name == beerName
        }
        if(snapshotSearchItem != null) {
            val position = this@SearchAdapter.snapshot().indexOf(snapshotSearchItem)
            if(snapshotSearchItem.bookmarked != isBookmarked){
                snapshotSearchItem.bookmarked = !snapshotSearchItem.bookmarked
                this@SearchAdapter.notifyItemChanged(position)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.name == newItem.name
            }
            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}