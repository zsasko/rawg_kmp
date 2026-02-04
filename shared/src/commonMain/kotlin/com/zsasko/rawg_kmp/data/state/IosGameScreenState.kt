package com.zsasko.rawg_kmp.data.state

import com.zsasko.rawg.data.model.GameResponseItem


sealed class IosGameScreenState {
    open val items: List<GameResponseItem> = emptyList()
    open val currentPage: Int = 1

    data class Loading(
        override val items: List<GameResponseItem>,
        override val currentPage: Int = 1
    ) : IosGameScreenState()

    data class Success(
        override val items: List<GameResponseItem>,
        override val currentPage: Int = 1,
        val hasMore: Boolean
    ) : IosGameScreenState()

    data class Error(
        override val items: List<GameResponseItem>,
        override val currentPage: Int = 1,
        val errorMessage: String
    ) : IosGameScreenState()
}
