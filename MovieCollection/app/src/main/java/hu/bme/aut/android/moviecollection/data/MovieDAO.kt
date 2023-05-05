package hu.bme.aut.android.moviecollection.data

import androidx.room.*

@Dao
interface MovieDAO {
    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Insert
    fun insert(movie: Movie): Long

    @Update
    fun update(movie: Movie)

    @Delete
    fun deleteItem(movie: Movie)
}