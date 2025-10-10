package com.rs.myapps.ui.home_scr

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rs.myapps.domain.AppInf
import com.rs.myapps.ui.main_scr.OnClickHandle
import com.rs.myapps.ui.theme.MyAppsTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(vm: HomeScreenVM = hiltViewModel(),
               onAppNavigate: (String,String,String,String?)-> Unit ){

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.getAppList(context)
        vm.navEvent.collectLatest {
            app -> onAppNavigate(
                app.name,
                app.version,
                app.pack,
                app.sourceDir
            )
        }
    }

    AppColumn(
        apps = vm.viewState.value.apps,
        onClick = vm::onAppClick,
    )
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
            .clickable{ onClick(index) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()) {
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