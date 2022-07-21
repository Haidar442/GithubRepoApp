package com.coding.githubrepoapp.domain.use_case.get_githubrepos

import androidx.paging.*
import com.coding.githubrepoapp.common.Resource
import com.coding.githubrepoapp.data.remote.dto.toGitgubRepo
import com.coding.githubrepoapp.data.repository.GithubRepoPagingSource
import com.coding.githubrepoapp.domain.model.GithubRepo
import com.coding.githubrepoapp.domain.repository.GithubRepoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetGithubReposUseCase @Inject constructor(
    private val repository: GithubRepoRepository
) {

    operator fun invoke(): Flow<Resource<PagingData<GithubRepo>>> =
        flow {
            try {
                emit(Resource.Loading<PagingData<GithubRepo>>())
              repository.getGithubRepos()
//                    .filter { pagingData ->
//                        val repos = pagingData.map { it.toGitgubRepo() }
//                        emit(Resource.Success<PagingData<GithubRepo>>(repos))
//                        true
//
//                    }
                    .collect { data ->
                        println(data.toString())
                        val repos = data.map { it.toGitgubRepo() }
                        emit(Resource.Success<PagingData<GithubRepo>>(repos))
                    }


            } catch (e: HttpException) {
                print("error usecase: ")
                println(e.localizedMessage ?: "An unexpected error occured")
                emit(
                    Resource.Error<PagingData<GithubRepo>>(
                        e.localizedMessage ?: "An unexpected error occured"
                    )
                )
            } catch (e: IOException) {
                println("Couldn't reach server. Check your internet connection.")

                emit(Resource.Error<PagingData<GithubRepo>>("Couldn't reach server. Check your internet connection."))
            } catch (e: Exception) {
                println("Couldn't reach server. Check your internet connection.")

                emit(Resource.Error<PagingData<GithubRepo>>("Couldn't reach server. Check your internet connection."))
            }
        }


}