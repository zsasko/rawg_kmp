package com.joel.ktorfitdemo.core.di.modules

import com.zsasko.rawg_kmp.viewmodel.GameDetailsViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@Module
@ComponentScan("com.zsasko.rawg_kmp.viewmodel")
class ViewModelModule

fun customViewModelModule(): org.koin.core.module.Module = module {
    viewModel { (gameId: Int) -> GameDetailsViewModel(gameId, get()) }
}