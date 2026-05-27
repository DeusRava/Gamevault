package com.gamevault.app.data.db

import android.content.Context
import androidx.room.*
import com.gamevault.app.data.model.FavoriteGame

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY name ASC")
    suspend fun getAll(): List<FavoriteGame>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: FavoriteGame)

    @Delete
    suspend fun delete(game: FavoriteGame)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE gameId = :id)")
    suspend fun isFavorite(id: Int): Boolean

    @Query("DELETE FROM favorites WHERE gameId = :id")
    suspend fun deleteById(id: Int)
}

@Database(entities = [FavoriteGame::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gamevault.db"
                ).build().also { INSTANCE = it }
            }
    }
}
