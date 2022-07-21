package com.coding.githubrepoapp.presentation.githubrepo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.coding.githubrepoapp.R
import com.coding.githubrepoapp.databinding.GithubrepoListItemBinding
import com.coding.githubrepoapp.domain.model.GithubRepo

class GithubRepoAdapter :
    PagingDataAdapter<GithubRepo, GithubRepoAdapter.GithubRepoViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubRepoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.githubrepo_list_item, parent, false)
        return GithubRepoViewHolder(v)


    }

    override fun onBindViewHolder(holder: GithubRepoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBindView(it, position)
            val bundle = Bundle()
            bundle.putParcelable("githubRepo", it)
            holder.itemView.setOnClickListener(
                View.OnClickListener {
                    Navigation.findNavController(holder.itemView)
                        .navigate(R.id.action_repoListFragment_to_repoDetailFragment, bundle)
                }
            )
        }
    }

    inner class GithubRepoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var binding = GithubrepoListItemBinding.bind(itemView)


        fun onBindView(data: GithubRepo, position: Int) {

            itemView.apply {
                binding.githubrepoName.text = data.name
                binding.githubrepoCreateDate.text = data.created_at
                Glide.with(binding.root).load(data.owner?.avatar_url)
                    .into(binding.imGithubRepo)
            }
        }

    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<GithubRepo>() {
            override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }


}