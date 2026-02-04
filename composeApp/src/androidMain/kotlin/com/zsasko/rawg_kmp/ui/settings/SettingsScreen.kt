package com.zsasko.rawg_kmp.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg_kmp.R
import com.zsasko.rawg_kmp.ui.settings.views.SettingsClickableItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSelectGenres: () -> Unit,
    openDrawer: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title)
                    )
                },
                navigationIcon = {
                        IconButton(onClick = openDrawer) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                modifier = Modifier.size(32.dp),
                                contentDescription = stringResource(R.string.general_open_navigation_drawer),
                            )
                        }
                },

                )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .padding(bottom = innerPadding.calculateBottomPadding())
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            SettingsClickableItem(
                stringResource(R.string.select_genres_title),
                stringResource(R.string.settings_select_genres_subtitle),
                onSelectGenres
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
     SettingsScreen(onSelectGenres = {}, openDrawer = {})
}