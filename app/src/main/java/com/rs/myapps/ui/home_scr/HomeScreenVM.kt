package com.rs.myapps.ui.home_scr

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.myapps.domain.AppInf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val LTAG = com.rs.myapps.LTAG + ".HomeScrVM"

@HiltViewModel
class HomeScreenVM @Inject constructor() : ViewModel() {

    var viewState : MutableState<HomeViewState> = mutableStateOf(HomeViewState())
        private set

    private val _navEvent = MutableSharedFlow<AppInf>()
    val navEvent = _navEvent.asSharedFlow()

    fun getAppList(context: Context) {
        val pm: PackageManager = context.packageManager
        val pack = pm.getInstalledPackages(0)

        viewState.value = HomeViewState(pack.map{
            AppInf(
                name = it.applicationInfo?.loadLabel(pm)?.toString() ?: "?",
                version = it.versionName ?: "?",
                pack = it.packageName,
                sourceDir = it.applicationInfo?.sourceDir
            )
        }.sortedBy { it.name })
    }

    fun onAppClick(index: Int){
        viewState.value.apps[index].apply {
            Log.d(com.rs.myapps.ui.home_scr.LTAG,"$name : $pack")
            viewModelScope.launch { _navEvent.emit(viewState.value.apps[index]) }
        }
    }
}


data class HomeViewState(
    val apps: List<AppInf> = emptyList(),
)