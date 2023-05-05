package hu.bme.aut.android.moviecollection.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import hu.bme.aut.android.moviecollection.R
import hu.bme.aut.android.moviecollection.data.Movie
import hu.bme.aut.android.moviecollection.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var movieStr: String? = null
    private var movie: Movie? = null

    companion object {
        private const val TAG = "DetailsActivity"
        const val EXTRA_MOVIE_NAME = "extra.movie_name"
        const val EXTRA_MOVIE_YEAR = "extra.movie_year"
        const val EXTRA_MOVIE_LENGTH = "extra.movie_length"
        const val EXTRA_MOVIE_GENRE = "extra.movie_genre"
        const val EXTRA_MOVIE_DESCRIPTION = "extra.movie_description"
    }

    fun setMovie(item: Movie) {this.movie = item}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MovieCollection_Details)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieStr = intent.getStringExtra(EXTRA_MOVIE_NAME)

        supportActionBar?.title = movieStr
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        movieStr = intent.getStringExtra(EXTRA_MOVIE_YEAR)
        binding.tvYear.setText(movieStr)

        movieStr = intent.getStringExtra(EXTRA_MOVIE_LENGTH)
        binding.tvLength.setText(movieStr)

        movieStr = intent.getStringExtra(EXTRA_MOVIE_GENRE)
        binding.tvGenre.setText(movieStr)

        movieStr = intent.getStringExtra(EXTRA_MOVIE_DESCRIPTION)
        binding.tvDescription.setText(movieStr)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}