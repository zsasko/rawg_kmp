package com.zsasko.rawg_kmp.di.modules

import com.zsasko.rawg_kmp.data.db.AppDatabase
import com.zsasko.rawg_kmp.data.db.SelectedGenreDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DaoModule {
    @Single
    fun provideSelectedGenreDao(database: AppDatabase): SelectedGenreDao {
        return database.selectedGenreDao()
    }
}