package com.rs.myapps.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.rs.myapps.ui.app_scr.AppScreen
import com.rs.myapps.ui.main_scr.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
data class AppScreen(
    val name: String,
    val version: String,
    val pack: String,
    val sourceDir: String?
)

@Composable
fun getNavGraph(navController: NavController): NavGraph {
    return remember(navController) {

        navController.createGraph(startDestination = HomeScreen) {

            composable<HomeScreen> {
                HomeScreen(){ name, version, pack, sourceDir ->
                    navController.navigate(AppScreen(name, version, pack, sourceDir))}
            }

            composable<AppScreen> {
                val route = it.toRoute<AppScreen>()
                AppScreen(
                    name = route.name,
                    version = route.version,
                    pack = route.pack,
                    sourceDir = route.sourceDir
                )
            }
        }
    }
}
