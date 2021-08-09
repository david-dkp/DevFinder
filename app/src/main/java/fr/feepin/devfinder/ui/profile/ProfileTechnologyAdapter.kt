package fr.feepin.devfinder.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ProfileTechnologiesItemBinding

class ProfileTechnologyAdapter : ListAdapter<String, ProfileTechnologyAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater.inflate(R.layout.profile_technologies_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val binding = ProfileTechnologiesItemBinding.bind(rootView)

        fun bind(technology: String) {
            binding.tvTechnology.text = technology
        }
    }
}