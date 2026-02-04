package com.zsasko.rawg_kmp.viewmodel

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.zsasko.rawg_kmp.data.domain.repository.GenreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
open class SplashViewModel(
    genreRepository: GenreRepository
) : ViewModel() {

    @NativeCoroutinesState
    val isGameCategoriesLoaded: StateFlow<Boolean?> = flow {
        delay(1_000)
        val hasGenres = genreRepository.hasSelectedGenres()
        emit(hasGenres)
    }.flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)


}