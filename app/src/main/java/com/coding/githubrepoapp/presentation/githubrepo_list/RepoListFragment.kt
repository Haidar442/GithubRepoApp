package com.coding.githubrepoapp.presentation.githubrepo_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.coding.githubrepoapp.R
import com.coding.githubrepoapp.databinding.FragmentRepoListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


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

//        setupFilteredTopic()
    }

    private fun getData() {
        repoListViewModel.scope.launch {
            repoListViewModel.searchRepo().collectLatest { data ->
                binding.progressBarMain.visibility = View.GONE
                githubRepoAdapter.submitData(data)
            }
        }
    }
    private fun getData1() {
        lifecycleScope.launchWhenStarted {

            repoListViewModel.state.collect { data ->

//                if (data.isLoading) {
//                    binding.progressBarMain.visibility = View.VISIBLE
//                } else if (data.error.isNotEmpty()) {
//                    binding.progressBarMain.visibility = View.GONE
//                    binding.txtErrorMain.text = data.error
//                    Toast.makeText(context, data.error, Toast.LENGTH_LONG).show()
//                    binding.txtErrorMain.visibility = View.VISIBLE
//                } else {
                    binding.progressBarMain.visibility = View.GONE
                    githubRepoAdapter.submitData(data.githubRepos)
//                }

            }
        }
    }

//    private fun setupFilteredTopic() {
//        lifecycleScope.launchWhenCreated {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                repoListViewModel.filteredRepos.collect { filteredTopicsList ->
//                    githubRepoAdapter.submitData(filteredTopicsList)
//                }
//            }
//        }
//    }

    private fun setupSearchView() {
        binding.searchBar.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
//                    if (query != null) {
                        repoListViewModel.queryText = query?: ""
//                    }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
//                    if (newText != null) {
//                        repoListViewModel.queryText = newText
//                    }
                    return true
                }
            })
        }
    }

    private fun recyclerViewAdapterListener() {

        githubRepoAdapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading
                /*||
                loadState.append is LoadState.Loading*/
            ){
                binding.progressBarMain.isVisible = true
                binding.txtErrorMain.isVisible = false
                binding.retryButton.isVisible=false
            }
            else {
                binding.progressBarMain.isVisible = false
                binding.txtErrorMain.isVisible = false
                binding.retryButton.isVisible=false
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    binding.retryButton.isVisible=true
                    binding.retryButton.setOnClickListener { githubRepoAdapter.retry() }
                    binding.txtErrorMain.isVisible = true
                    binding.txtErrorMain.text = it.error.localizedMessage
                    Toast.makeText(requireContext(), it.error.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupRecyclerView() = binding.rvGithubRepoList.apply {
        githubRepoAdapter = GithubRepoAdapter()
        githubRepoAdapter.withLoadStateFooter(footer = LoadingStateAdapter{githubRepoAdapter.retry()})

        adapter = githubRepoAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }
}