package com.coding.githubrepoapp.data.remote

import androidx.paging.PagingData
import com.coding.githubrepoapp.data.remote.dto.GithubRepoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubReposApi {

    @GET("/orgs/google/repos")
    suspend fun getGithubRepos(@Query("page") page: Int): Response<List<GithubRepoDto>>

}