package com.zsasko.rawg_kmp.ui.common.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.zsasko.rawg_kmp.ui.common.AppDrawer
import com.zsasko.rawg_kmp.ui.game_details.GameDetailsScreen
import com.zsasko.rawg_kmp.ui.games.GamesScreen
import com.zsasko.rawg_kmp.ui.select_genre.SelectGenreScreen
import com.zsasko.rawg_kmp.ui.settings.SettingsScreen
import com.zsasko.rawg_kmp.ui.splash.SplashScreen
import com.zsasko.rawg_kmp.viewmodel.GameDetailsViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainNavigator(innerPadding: PaddingValues) {
    val backStack = rememberNavBackStack(Routes.Splash)
    val currentRoute = backStack.lastOrNull() ?: Routes.Splash

    val coroutineScope = rememberCoroutineScope()
    val sizeAwareDrawerState = rememberDrawerState(DrawerValue.Closed)

    val gesturesEnabled = currentRoute !is Routes.GameDetails && currentRoute !is Routes.SelectGenres

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                drawerState = sizeAwareDrawerState,
                currentRoute = currentRoute,
                navigateToHome = {
                    navigateAndClearStackUntilRoot(backStack, null)
                },
                navigateToSettings = {
                    navigateAndClearStackUntilRoot(backStack, Routes.Settings)
                },
                closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } },
            )
        },
        drawerState = sizeAwareDrawerState,
        gesturesEnabled = gesturesEnabled
    ) {

        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                // used in order to inject viewmodel for Routes.GameDetails route
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Routes.Splash> {
                    SplashScreen(onDisplayMainScreen = {
                        backStack.removeLastOrNull()
                        backStack.add(Routes.Games)
                    }, onDisplaySelectGenresScreen = {
                        backStack.removeLastOrNull()
                        backStack.add(Routes.SelectGenres(showUpButton = false, showNextButton = true))
                    })
                }
                entry<Routes.SelectGenres> {
                    SelectGenreScreen(
                        showUpButton = it.showUpButton,
                        showNextButton = it.showNextButton,
                        onUpButtonClicked = {
                            backStack.removeLastOrNull()
                        },
                        onNextButtonClicked = {
                            backStack.removeLastOrNull()
                            backStack.add(Routes.Games)
                        })
                }
                entry<Routes.Games> {
                    GamesScreen(
                        onGameClicked = { gameId ->
                            backStack.removeAll { it is Routes.GameDetails }
                            backStack.add(Routes.GameDetails(gameId))
                        }, openDrawer = {
                            coroutineScope.launch { sizeAwareDrawerState.open() }
                        })
                }
                entry<Routes.GameDetails> {
                    val viewModel = koinViewModel<GameDetailsViewModel>(
                        parameters =  { parametersOf(it.gameId) }
                    )
                    GameDetailsScreen(
                        viewModel = viewModel,
                        onUpButtonClicked = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                entry<Routes.Settings> {
                    SettingsScreen(openDrawer = {
                        coroutineScope.launch { sizeAwareDrawerState.open() }
                    }, onSelectGenres = {
                        backStack.add(Routes.SelectGenres(showUpButton = true, showNextButton = false))
                    })
                }
            }
        )
    }
}

private fun navigateAndClearStackUntilRoot(backStack: MutableList<NavKey>, nextRouteToOpen: NavKey?) {
    while (backStack.size > 1) {
        backStack.removeLastOrNull()
    }
    nextRouteToOpen?.let {
        backStack.add(it)
    }
}
