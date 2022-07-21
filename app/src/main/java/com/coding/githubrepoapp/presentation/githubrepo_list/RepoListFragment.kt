package com.coding.githubrepoapp.presentation.githubrepo_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.coding.githubrepoapp.R
import com.coding.githubrepoapp.databinding.FragmentRepoListBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RepoListFragment : Fragment(R.layout.fragment_repo_list) {


    private val repoListViewModel: GithubRepoListViewModel by viewModels()

    private lateinit var githubRepoAdapter: GithubRepoAdapter
    lateinit var binding: FragmentRepoListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRepoListBinding.bind(view)
        setupRecyclerView()
        recyclerViewAdapterListener()
        setupSearchView()
        getData()

    }

    private fun getData() {
        lifecycleScope.launchWhenStarted {

            repoListViewModel.state.collect { data ->
                binding.progressBarMain.visibility = View.GONE
                githubRepoAdapter.submitData(data.githubRepos)

            }
        }
    }

    private fun setupSearchView() {
        binding.searchBar.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        repoListViewModel.myquery.value = query
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        repoListViewModel.myquery.value = newText
                    }
                    return true
                }
            })
        }
    }


    private fun recyclerViewAdapterListener() {
        githubRepoAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading
            /*||
            loadState.append is LoadState.Loading*/
            ) {
                binding.progressBarMain.isVisible = true
                binding.txtErrorMain.isVisible = false
                binding.retryButton.isVisible = false
            } else {
                binding.progressBarMain.isVisible = false
                binding.txtErrorMain.isVisible = false
                binding.retryButton.isVisible = false
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {

                    binding.retryButton.isVisible = true
                    binding.retryButton.setOnClickListener { githubRepoAdapter.retry() }
                    binding.txtErrorMain.isVisible = true
                    binding.txtErrorMain.text = it.error.localizedMessage
                    Toast.makeText(requireContext(), it.error.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun setupRecyclerView() = binding.rvGithubRepoList.apply {
        githubRepoAdapter = GithubRepoAdapter()
        adapter =
            githubRepoAdapter.withLoadStateFooter(footer = LoadingStateAdapter { githubRepoAdapter.retry() })
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }
}