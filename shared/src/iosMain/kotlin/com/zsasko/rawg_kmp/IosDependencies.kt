package com.zsasko.rawg_kmp

import com.zsasko.rawg_kmp.viewmodel.GameDetailsViewModel
import com.zsasko.rawg_kmp.viewmodel.IosGamesViewModel
import com.zsasko.rawg_kmp.viewmodel.SelectGenreViewModel
import com.zsasko.rawg_kmp.viewmodel.SplashViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

@Suppress("unused")
object IosDependencies : KoinComponent {
    fun getSplashViewModel(): SplashViewModel = getKoin().get<SplashViewModel>()
    fun getSelectGenreViewModel(): SelectGenreViewModel = getKoin().get<SelectGenreViewModel>()
    fun getIosGamesViewModel(): IosGamesViewModel = getKoin().get<IosGamesViewModel>()
    fun getGameDetailsViewModel(gameId: Int): GameDetailsViewModel {
        return getKoin().get<GameDetailsViewModel> { parametersOf(gameId) }
    }
}

