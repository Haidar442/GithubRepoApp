package com.coding.githubrepoapp.presentation.githubrepo_list


import android.provider.Contacts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.coding.githubrepoapp.common.Resource
import com.coding.githubrepoapp.data.remote.dto.toGitgubRepo
import com.coding.githubrepoapp.domain.model.GithubRepo
import com.coding.githubrepoapp.domain.repository.GithubRepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GithubRepoListViewModel @Inject constructor(
//    private val getGithubReposUseCase: GetGithubReposUseCase
    private val repository: GithubRepoRepository
) : ViewModel() {

    val job = Job()
    val scope = CoroutineScope(job + Dispatchers.IO)
    private var currentSearchResult: Flow<PagingData<GithubRepo>>? = null


    val _state = MutableStateFlow(GithubRepoListState())
    val state: Flow<GithubRepoListState> = _state

    private val myquery = MutableStateFlow("")

    var queryText: String
        get() = myquery.value
        set(value) {
            println("query viewmodel : $value")
            myquery.value = value
        }

//    init {
//        getGithubReposData()
//    }

    suspend fun searchRepo(): Flow<PagingData<GithubRepo>> {
        val lastResult = currentSearchResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<GithubRepo>> = repository.getGithubRepos()
            .cachedIn(scope).map { it.map { it.toGitgubRepo() }  }

        newResult.cachedIn(scope)
            .combine(myquery) { pagingData, query ->
                pagingData.filter { repo ->
                    if (query.isEmpty())
                        return@filter true
                    repo.name?.lowercase()?.startsWith(query.lowercase()) ?: true
                }

            }
        currentSearchResult = newResult
        return newResult
    }

    private fun getGithubReposData() {
        viewModelScope.launch {
            _state
                .combine(myquery) { pagingData, query ->
                println(query)
                pagingData.githubRepos.filter { repo ->
                    if (query.isEmpty())
                        return@filter true
                    repo.name?.lowercase()?.startsWith(query.lowercase()) ?: true
                }

            }.cachedIn(viewModelScope)

            try {

                repository.getGithubRepos().cachedIn(viewModelScope)
                    .collect { data ->
                        println(data.toString())
                        val repos = data.map { it.toGitgubRepo() }

                        _state.value =
                            GithubRepoListState(githubRepos = repos)
                    }

            } catch (e: HttpException) {
                print("error usecase: ")
                println(e.localizedMessage ?: "An unexpected error occured")
                _state.value = GithubRepoListState(
                    error = e.localizedMessage ?: "An unexpected error occured"
                )


            } catch (e: IOException) {
                println("Couldn't reach server. Check your internet connection.")
                _state.value = GithubRepoListState(
                    error = e.localizedMessage
                        ?: "Couldn't reach server. Check your internet connection."
                )
            } catch (e: Exception) {
                println("Couldn't reach server. Check your internet connection.")
                _state.value = GithubRepoListState(
                    error = e.localizedMessage
                        ?: "Couldn't reach server. Check your internet connection."
                )
            }

        }

//        getGithubReposUseCase(viewModelScope)
//            .onEach { result ->
//                when (result) {
//                    is Resource.Success -> {
//                        _state.value =
//                            GithubRepoListState(githubRepos = result.data ?: PagingData.empty())
//                    }
//                    is Resource.Error -> {
//                        _state.value = GithubRepoListState(
//                            error = result.message ?: "An unexpected error occured"
//                        )
//                    }
//                    is Resource.Loading -> {
//                        _state.value = GithubRepoListState(isLoading = true)
//                    }
//                }
//            }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }

//        _state.cachedIn(this).combine(myquery) { pagingData, query ->
//            println(query)
//            pagingData.githubRepos.filter { repo ->
//                if (query.isEmpty())
//                    return@filter true
//                repo.name?.lowercase()?.startsWith(query.lowercase()) ?: true
//            }
//        }.cachedIn(this)
}