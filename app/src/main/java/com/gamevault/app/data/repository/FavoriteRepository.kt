package com.gamevault.app.data.repository

import com.gamevault.app.data.db.FavoriteDao
import com.gamevault.app.data.model.FavoriteGame
import com.gamevault.app.data.model.Game
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val dao: FavoriteDao
) {
    suspend fun getAll(): List<FavoriteGame> = dao.getAll()

    suspend fun toggle(game: Game) {
        if (dao.isFavorite(game.id)) {
            dao.deleteById(game.id)
        } else {
            dao.insert(
                FavoriteGame(
                    gameId = game.id,
                    name = game.name,
                    backgroundImage = game.backgroundImage,
                    rating = game.rating,
                    slug = game.slug
                )
            )
        }
    }

    suspend fun isFavorite(gameId: Int): Boolean = dao.isFavorite(gameId)
}
