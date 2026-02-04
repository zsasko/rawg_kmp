package com.zsasko.rawg_kmp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "selected_genre")
data class SelectedGenre(
    @PrimaryKey
    @ColumnInfo(name = "genre_id") val genreId: Int
)