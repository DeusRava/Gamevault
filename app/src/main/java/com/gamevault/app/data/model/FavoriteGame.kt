package com.gamevault.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteGame(
    @PrimaryKey val gameId: Int,
    val name: String,
    val backgroundImage: String?,
    val rating: Double,
    val slug: String
)
