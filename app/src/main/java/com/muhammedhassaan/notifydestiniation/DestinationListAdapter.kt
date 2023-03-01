package com.muhammedhassaan.notifydestiniation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhammedhassaan.notifydestiniation.databinding.DestinationListItemBinding

class DestinationListAdapter : ListAdapter<Destination, DestinationListAdapter.DestinationViewHolder>(DestinationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val binding = DestinationListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DestinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bind(destination)
    }

    inner class DestinationViewHolder(private val binding: DestinationListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(destination: Destination) {

            binding.apply {
                tvDestinationID.text = destination.id.toString()
                tvAddress.text = destination.address
                tvStatus.text = destination.status
                ivStatus.setImageResource(destination.statusIcon)
            }
        }
    }

    class DestinationDiffCallback : DiffUtil.ItemCallback<Destination>() {
        override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean {
            return oldItem == newItem
        }
    }
}
