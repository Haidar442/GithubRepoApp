package com.coding.githubrepoapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.coding.githubrepoapp.data.remote.GithubReposApi
import com.coding.githubrepoapp.data.remote.dto.GithubRepoDto
import com.coding.githubrepoapp.domain.model.GithubRepo
import com.coding.githubrepoapp.domain.repository.GithubRepoRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GithubRepoPagingSource @Inject constructor(
    private val api: GithubReposApi
) : PagingSource<Int, GithubRepoDto>() {
    override fun getRefreshKey(state: PagingState<Int, GithubRepoDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepoDto> {

        return try {
            val position = params.key ?: 1

            val response = api.getGithubRepos(position)
                LoadResult.Page(
                    data = response.body()!!,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = position + 1
                )

        }
        catch (e: IOException) {
            println("error pagingsource IOException : ${e.message}")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            println("error pagingsource HttpException : ${e.message}")

            LoadResult.Error(e)
        }
        catch (e: Exception) {
            println("error pagingsource  Exception: ${e.message}")
            LoadResult.Error(e)
        }
    }


}