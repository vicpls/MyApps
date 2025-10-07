package com.rs.myapps.ui.app_scr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rs.myapps.R

@Composable
fun AppScreen(
    vm: AppScreenVM = hiltViewModel(),
    name: String,
    version: String,
    pack: String,
    sourceDir: String?
) {

    val checksum by remember {
        vm.getCheckSum(sourceDir)
        vm.viewState
        }

    AppScreen(
        appVS = checksum,
        name = name,
        version = version,
        pack = pack,
    )
}

@Composable
fun AppScreen(
    appVS: AppViewState,
    name: String,
    version: String,
    pack: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        val checkSumText = if (appVS.checkSumInProcess) stringResource(R.string.calculating)
        else appVS.checksum?.asString(LocalContext.current) ?: "?"

        Box {
            Column(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                ParameterLine(R.string.name_title, name)

                ParameterLine(R.string.version_title, version)

                ParameterLine(R.string.package_title, pack)
                ParameterLine(R.string.check_sum_title, checkSumText)
                Button({}) { Text(stringResource(R.string.launch)) }
            }

            if (appVS.checkSumInProcess) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

val titleModifier = Modifier.width(100.dp)

@Composable
fun ParameterLine(titleId: Int, value: String?){
    Row{
        Text(stringResource(titleId), titleModifier)
        Text(value ?: stringResource(R.string.na))
    }
}

@Preview
@Composable
fun AppScreenPreview(){
    AppScreen(AppViewState(checkSumInProcess = true),
        "NamePreview","VerPreview","package.preview.test")
}