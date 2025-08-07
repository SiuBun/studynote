package com.wsb.customview.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wsb.customview.R
import com.wsb.customview.databinding.ItemPagingListBinding

class MyListAdapter : ListAdapter<ListData, MyListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemPagingListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPagingListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ListData) {
            binding.pagingContentTextView.text = data.content

            val imageView = binding.ivPagingImageView

            Glide.with(imageView.context)
                .load(AppCompatResources.getDrawable(imageView.context, data.resId)).placeholder(R.drawable.floating_logo).into(imageView)
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Item Clicked: ${data.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.pagingButton1.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Paging Button 1 Clicked on item ${data.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.pagingButton2.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Paging Button 2 Clicked on item ${data.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.pagingButton3.text = data.id.toString()
            binding.pagingButton3.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Paging Button 3 Clicked on item ${data.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ListData>() {
        override fun areItemsTheSame(oldItem: ListData, newItem: ListData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListData, newItem: ListData): Boolean {
            return oldItem == newItem
        }
    }
}