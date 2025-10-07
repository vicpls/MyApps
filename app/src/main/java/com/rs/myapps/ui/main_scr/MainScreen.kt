package com.rs.myapps.ui.main_scr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rs.myapps.R
import com.rs.myapps.ui.getNavGraph
import com.rs.myapps.ui.theme.MyAppsTheme

typealias OnClickHandle = (Int)->Unit

val LocalSnackBarState = compositionLocalOf <MutableState<SnackbarHostState>> {
    throw IllegalStateException("SnackBar state did not define")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val snackBarHostState = remember { mutableStateOf(SnackbarHostState()) }

    CompositionLocalProvider(
        LocalSnackBarState.provides(snackBarHostState)
    ) {

        MyAppsTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) })
                },

                snackbarHost = {
                    SnackbarHost(
                        LocalSnackBarState.current.value,
                    )
                },

                ) { innerPadding ->

                NavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    graph = getNavGraph(navController)
                )


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
    MainScreen()
}