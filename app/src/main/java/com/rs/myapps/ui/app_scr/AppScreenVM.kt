package com.rs.myapps.ui.app_scr

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.myapps.LTAG
import com.rs.myapps.R
import com.rs.myapps.data.CheckSumFormatUseCase
import com.rs.myapps.data.CheckSumUseCase
import com.rs.myapps.data.StringValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppScreenVM @Inject constructor(@param:ApplicationContext val context: Context) : ViewModel() {

    var viewState : MutableState<AppViewState> = mutableStateOf(AppViewState())
        private set


    fun getCheckSum(sourceDir: String?) {

        if (sourceDir==null){
            viewState.value = AppViewState(
                checksum = StringValue.StringResource(R.string.na),
                checkSumInProcess = false,
                errMsg = StringValue.StringResource(R.string.cant_read_apk)
            )
            return
        }

        viewState.value = AppViewState(checkSumInProcess = true)

        viewModelScope.launch(Dispatchers.Default) {

            CheckSumUseCase()(sourceDir)
                .onSuccess {
                    Log.d(LTAG, "success")
                    viewState.value = AppViewState(
                        checksum = StringValue.DynamicString(CheckSumFormatUseCase()(it)),
                        checkSumInProcess = false
                    )
                }
                .onFailure {
                    Log.d(LTAG, "failure")
                    viewState.value = AppViewState(
                        checksum = StringValue.StringResource(R.string.na),
                        checkSumInProcess = false,
                        errMsg = StringValue.DynamicString(it.message)
                    )
                }



        }
    }

    fun onStart(pack: String){
        val intent = context.packageManager.getLaunchIntentForPackage(pack)
        if (intent != null){
            context.startActivity(intent)
        } else {
            viewState.value = viewState.value
                .copy(errMsg = StringValue.StringResource(R.string.cant_start))
        }
    }
}


data class AppViewState(
    val checksum: StringValue? = null,
    val checkSumInProcess: Boolean = false,
    val errMsg: StringValue? = null,
)