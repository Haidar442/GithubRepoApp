package com.coding.githubrepoapp.presentation.splash_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.coding.githubrepoapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.goToListFragment{
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRepoListFragment())
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}