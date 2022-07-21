package com.coding.githubrepoapp.presentation.githubrepo_list

import androidx.paging.PagingData
import com.coding.githubrepoapp.domain.model.GithubRepo

data class GithubRepoListState(
    val isLoading: Boolean = false,
    val githubRepos: PagingData<GithubRepo> = PagingData.empty(),
    val error: String = ""
)
