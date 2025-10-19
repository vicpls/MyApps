package com.rs.myapps.ui.app_scr

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.myapps.LTAG
import com.rs.myapps.R
import com.rs.myapps.domain.ICheckSumUseCases
import com.rs.myapps.domain.IoDispatcher
import com.rs.myapps.utils.StringValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppScreenVM @Inject constructor(
    @param:ApplicationContext val context: Context,
    private val checkSumUC: ICheckSumUseCases,
    @param:IoDispatcher private val corDispatcher: CoroutineDispatcher
) : ViewModel() {

    var viewState : MutableState<AppViewState> = mutableStateOf(AppViewState())
        private set


    fun getCheckSum(sourceDir: String?) {

        Log.d(LTAG, "getCheckSum($sourceDir)")

        if (sourceDir==null){
            viewState.value = AppViewState(
                checksum = StringValue.ResId(R.string.na),
                checkSumInProcess = false,
                errMsg = StringValue.ResId(R.string.cant_read_apk)
            )
            return
        }

        viewState.value = AppViewState(checkSumInProcess = true)

        viewModelScope.launch(corDispatcher) {

            checkSumUC.checkSum(sourceDir)
                .onSuccess {
                    Log.d(LTAG, "success")
                    viewState.value = AppViewState(
                        checksum = StringValue.Text(checkSumUC.checkSumFormat(it)),
                        checkSumInProcess = false
                    )
                }
                .onFailure {
                    Log.d(LTAG, "failure")
                    viewState.value = AppViewState(
                        checksum = StringValue.ResId(R.string.na),
                        checkSumInProcess = false,
                        errMsg = if (it.message != null) StringValue.Text(it.message!!)
                        else
                            StringValue.ResId(R.string.unknown_error)

                    )
                }



        }
    }

    fun onStart(pack: String){
        val intent = context.packageManager.getLaunchIntentForPackage(pack)
        if (intent != null){
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            viewState.value = viewState.value
                .copy(errMsg = StringValue.ResId(R.string.cant_start))
        }
    }
}


data class AppViewState(
    val checksum: StringValue? = null,
    val checkSumInProcess: Boolean = false,
    val errMsg: StringValue? = null,
)