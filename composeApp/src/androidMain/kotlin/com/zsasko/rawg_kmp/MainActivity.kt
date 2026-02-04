package com.zsasko.rawg_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zsasko.rawg_kmp.ui.common.navigation.MainNavigator
import com.zsasko.rawg_kmp.ui.common.theme.RAWGTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            RAWGTheme {
                MainNavigator()
            }
        }
    }
}