package com.zsasko.rawg_kmp

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zsasko.rawg_kmp.data.db.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("rawg_kmp.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

actual fun platformDbModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getRoomDatabase(builder)
    }
}