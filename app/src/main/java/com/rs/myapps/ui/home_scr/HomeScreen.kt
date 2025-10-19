package com.rs.myapps.ui.home_scr

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rs.myapps.domain.model.AppInf
import com.rs.myapps.ui.main_scr.LocalSnackBarState
import com.rs.myapps.ui.main_scr.OnClickHandle
import com.rs.myapps.ui.theme.MyAppsTheme
import com.rs.myapps.utils.StringValue
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(vm: HomeScreenVM = hiltViewModel(),
               onAppNavigate: (String,String,String,String?)-> Unit ){

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.getAppList()
        vm.navEvent.collectLatest {
            app -> onAppNavigate(
                app.name,
                app.version,
                app.pack,
                app.sourceDir
            )
        }
    }


    val state = vm.viewState.value
    when(state){
        is HomeViewState.Loading -> { LoadingIndicator() }
        is HomeViewState.Success -> {
            AppColumn(
                apps = state.apps,
                onClick = vm::onAppClick,
            )
        }
        is HomeViewState.Error -> { PopUpMessage(state.message) }
    }
}

@Composable
fun LoadingIndicator(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}

@Composable
fun PopUpMessage(message: StringValue) {
    val snackBar = LocalSnackBarState.current.value
    val msg = message.asString()
    LaunchedEffect(message) {
        launch {
            snackBar.showSnackbar(msg)
        }
    }
}


@Composable
fun AppColumn(apps: List<AppInf>, modifier: Modifier = Modifier, onClick: OnClickHandle) {

    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 8.dp
        ),) {
        itemsIndexed(apps){ index, item ->
            AppItem(index,item, onClick)
        }
    }
}


@Composable
fun AppItem(index: Int, appInf: AppInf, onClick: OnClickHandle){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(index) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()) {
            Text(fontSize = 20.sp, text = appInf.name)
//            Spacer(Modifier.height(8.dp))
            HorizontalDivider(Modifier.padding(horizontal = 0.dp, vertical = 4.dp), thickness = 1.dp)

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right,
                text = appInf.pack,
                fontSize = 14.sp
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AppColumnPreview() {
    MyAppsTheme {
        val appS = listOf(AppInf("Android", "1.0", "com.pack.android"))
        AppColumn(appS){}
    }
}