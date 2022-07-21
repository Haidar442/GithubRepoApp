package com.coding.githubrepoapp.presentation.splash_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.coding.githubrepoapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor
    (
) : ViewModel() {

    fun goToListFragment(splashCallBack: () -> Unit) {
        viewModelScope.launch {
            delay(3000)
            splashCallBack()
        }

    }

}