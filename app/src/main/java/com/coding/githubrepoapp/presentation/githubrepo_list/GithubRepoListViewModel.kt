package com.coding.githubrepoapp.presentation.githubrepo_list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.coding.githubrepoapp.data.remote.dto.toGitgubRepo
import com.coding.githubrepoapp.domain.repository.GithubRepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubRepoListViewModel @Inject constructor(
    private val repository: GithubRepoRepository
) : ViewModel() {

    val _state = MutableStateFlow(GithubRepoListState())
    val state: Flow<GithubRepoListState> = _state
    val myquery = MutableStateFlow("")


    init {
        getGithubReposData()
    }

    private fun getGithubReposData() {
        viewModelScope.launch {
            repository.getGithubRepos()
                .cachedIn(this)
                .combine(myquery) { pagingData, query ->
                    println(query)
                    pagingData.filter { repo ->
                        if (query.isEmpty())
                            return@filter true
                        repo.name.lowercase().startsWith(query.lowercase())
                    }
                }.cachedIn(this).collectLatest { data ->
                    println(data.toString())
                    val repos = data.map { it.toGitgubRepo() }
                    _state.value =
                        GithubRepoListState(githubRepos = repos)
                }
        }
    }

}