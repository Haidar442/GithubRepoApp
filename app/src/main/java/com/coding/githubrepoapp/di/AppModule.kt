package com.coding.githubrepoapp.di

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.coding.githubrepoapp.R
import com.coding.githubrepoapp.common.Constants
import com.coding.githubrepoapp.data.remote.GithubReposApi
import com.coding.githubrepoapp.data.repository.GithubRepoRepositoryImpl
import com.coding.githubrepoapp.domain.repository.GithubRepoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGithubReposApi(): GithubReposApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubReposApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGithubRepoRepository(api: GithubReposApi): GithubRepoRepository {
        return GithubRepoRepositoryImpl(api)
    }

}