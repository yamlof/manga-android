package com.example.greetingcard.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "mangas",
    indices = [Index(value = ["mangaUrl"],unique = true)]
)
data class Manga(
    @PrimaryKey val name :String,
    val cover : String,
    val mangaUrl : String
)

@Entity(tableName = "chapters",
    foreignKeys = [ForeignKey(
        entity = Manga::class,
        parentColumns = ["mangaUrl"],
        childColumns = ["mangaUrl"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ChapterRoom(
    @PrimaryKey val imgTitle: String,
    val imgLink : String,
    val mangaUrl: String
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

@Dao
interface ChapterDao{
    @Query("SELECT * FROM chapters WHERE mangaUrl = :mangaId")
    fun getChaptersForManga(mangaId: String): Flow<List<ChapterRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterRoom)

    @Delete
    suspend fun deleteChapter(chapter: ChapterRoom)
}


@Database(entities = [Manga::class,ChapterRoom::class] , version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mangaDao() : MangaDao
    abstract fun chapterDao(): ChapterDao

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
