package hu.bme.aut.android.moviecollection.data

import android.content.Context
import androidx.room.*

@Database(entities = [Movie::class], version = 1)
@TypeConverters(value = [Movie.Genre::class])
abstract class MovieListDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO

    companion object {
        fun getDatabase(applicationContext: Context): MovieListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                MovieListDatabase::class.java,
                "movie-list"
            ).build();
        }
    }
}