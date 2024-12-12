package com.example.greetingcard.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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

    @Update
    suspend fun update(manga: Manga)

    @Delete
    suspend fun delete(manga: Manga)



    @Query("SELECT * FROM mangas")
    fun getAllMangas() : Flow<List<Manga>>
}

@Database(entities = [Manga::class] , version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mangaDao() : MangaDao

    companion object {

        @Volatile
        private var INSTANCE:AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "manga_database" // The name of your database
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
