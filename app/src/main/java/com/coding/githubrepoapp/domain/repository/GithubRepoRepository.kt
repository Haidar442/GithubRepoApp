package com.coding.githubrepoapp.domain.repository


import androidx.paging.PagingData
import com.coding.githubrepoapp.data.remote.dto.GithubRepoDto
import kotlinx.coroutines.flow.Flow


interface GithubRepoRepository {

    suspend fun getGithubRepos(): Flow<PagingData<GithubRepoDto>>

}