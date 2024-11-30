package com.example.greetingcard.items

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import java.io.StringBufferInputStream

@Entity(tableName = "mangas")
data class Manga(
    @PrimaryKey val name :String,
    val cover : String,
    val mangaUrl : String
)

@Dao
interface MangaDao {
    @Insert
    suspend fun insertManga(manga : Manga)

    @Query("SELECT * FROM mangas")
    suspend fun getAllMangas() : List<Manga>
}

@Database(entities = [Manga::class] , version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MangaDao() :MangaDao
}
