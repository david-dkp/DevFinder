package fr.feepin.devfinder.ui.projectlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.feepin.devfinder.R
import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.databinding.ProjectListItemBinding

class ProjectListAdapter(val onProjectClick: (Project) -> Unit) :
    ListAdapter<Project, ProjectListAdapter.ViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Project>() {

            override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.project_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val binding: ProjectListItemBinding = ProjectListItemBinding.bind(rootView)

        init {
            binding.root.setOnClickListener {
                val project = getItem(bindingAdapterPosition)
                onProjectClick(project)
            }
        }

        fun bind(project: Project) {
            binding.apply {
                tvTitle.text = project.title
                tvUsername.text = project.username
                tvViewCount.text = project.viewCount.toString()
            }
        }

    }
}