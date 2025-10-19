package com.rs.myapps.ui.home_scr

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.myapps.domain.GetAppListUseCase
import com.rs.myapps.domain.model.AppInf
import com.rs.myapps.utils.StringValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenVM @Inject constructor(
    private val getAppListUC : GetAppListUseCase
) : ViewModel() {

    var viewState : MutableState<HomeViewState> = mutableStateOf(HomeViewState.Loading)
        private set

    private val _navEvent = MutableSharedFlow<AppInf>()
    val navEvent = _navEvent.asSharedFlow()

    fun getAppList() {
        viewModelScope.launch {
            viewState.value = HomeViewState.Success(
                getAppListUC().sortedBy { it.name })
        }
    }

    fun onAppClick(index: Int){
        val currState = viewState.value
        if (currState is HomeViewState.Success) {
            currState.apps[index].apply {
                viewModelScope.launch { _navEvent.emit(this@apply) }
            }
        }
    }
}


sealed interface HomeViewState {
    object Loading : HomeViewState
    class Success(val apps: List<AppInf>) : HomeViewState

    @JvmInline
    value class Error(val message: StringValue) : HomeViewState
}