package com.zsasko.rawg_kmp.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zsasko.rawg_kmp.R
import com.zsasko.rawg_kmp.viewmodel.SplashViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    viewmodel: SplashViewModel  = koinViewModel<SplashViewModel>(),
    onDisplayMainScreen: () -> Unit, onDisplaySelectGenresScreen: () -> Unit
) {

    val isGameCategoriesLoaded = viewmodel.isGameCategoriesLoaded.collectAsStateWithLifecycle()

    LaunchedEffect(isGameCategoriesLoaded.value) {
        isGameCategoriesLoaded.value?.let {
            if (it) {
                onDisplayMainScreen()
            } else {
                onDisplaySelectGenresScreen()
            }
        }
    }

    SplashScreenLayout()
}

@Composable
private fun SplashScreenLayout() {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_rawg_colorful),
                contentDescription = stringResource(R.string.splash_logo_content_description),
                modifier = Modifier
                    .size(150.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSplashScreen() {
    SplashScreenLayout()
}