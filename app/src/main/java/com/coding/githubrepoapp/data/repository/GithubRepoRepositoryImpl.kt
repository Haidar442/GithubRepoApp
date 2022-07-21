package com.coding.githubrepoapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.coding.githubrepoapp.data.remote.GithubReposApi
import com.coding.githubrepoapp.data.remote.dto.GithubRepoDto

import com.coding.githubrepoapp.domain.repository.GithubRepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubRepoRepositoryImpl @Inject constructor(
    private val api: GithubReposApi
) : GithubRepoRepository {

    override suspend fun getGithubRepos(): Flow<PagingData<GithubRepoDto>> {
      return  Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                initialLoadSize = 2
            ),
            pagingSourceFactory = {
                GithubRepoPagingSource(api)
            }, initialKey = 1
        ).flow
    }
}

