package com.coding.githubrepoapp.presentation.githubrepo_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coding.githubrepoapp.R
import com.coding.githubrepoapp.databinding.PageFooterLayoutBinding
import javax.inject.Inject

class LoadingStateAdapter(
    private val retry: () -> Unit
):
    LoadStateAdapter<LoadingStateAdapter.NetworkStateItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        NetworkStateItemViewHolder(parent ,retry)


    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class NetworkStateItemViewHolder(
        parent: ViewGroup,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.page_footer_layout, parent, false) ){


            private val binding = PageFooterLayoutBinding.bind(itemView)
             private val progressBar: ProgressBar = binding.progressBar
             private val errorMsg: TextView = binding.errorMsg
             private val retryButton: ImageButton = binding.retryButton
                .also {
                    it.setOnClickListener { retry() }
                }


        fun bind(loadState: LoadState) {

                if (loadState is LoadState.Error) {
                    errorMsg.text = loadState.error.localizedMessage
                }
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                errorMsg.text = (loadState as? LoadState.Error)?.error?.message
        }
    }
}