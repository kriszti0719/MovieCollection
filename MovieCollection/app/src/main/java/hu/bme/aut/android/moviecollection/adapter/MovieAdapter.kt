package hu.bme.aut.android.moviecollection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.moviecollection.R
import hu.bme.aut.android.moviecollection.data.Movie
import hu.bme.aut.android.moviecollection.databinding.MovieListBinding
import hu.bme.aut.android.moviecollection.fragments.NewMovieDialogFragment

class MovieAdapter (private val listener: MovieClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private val items = mutableListOf<Movie>()

    interface MovieClickListener {
        fun onItemChanged(item: Movie)
        fun onItemDelete(item: Movie)
        fun onItemSelected(item: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        MovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = items[position]

        holder.binding.ivIcon.setImageResource(getImageResource(movie.genre))
        holder.binding.tvTitle.text = movie.title
        holder.binding.tvYear.text = movie.year.toString()
        holder.binding.tvGenre.text = movie.genre.getAltName()
        var hour: Int = movie.length/60
        var min: Int = movie.length - hour * 60
        holder.binding.tvLength.text = "${hour} h ${min} m"

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemDelete(movie)
        }

        holder.binding.ibEdit.setOnClickListener {
            listener.onItemChanged(movie)
        }

        holder.binding.root.setOnClickListener {
            listener.onItemSelected(movie)
        }
    }
    @DrawableRes()
    private fun getImageResource(genre: Movie.Genre): Int {
        return when (genre) {
            Movie.Genre.ACTION -> R.drawable.action
            Movie.Genre.COMEDY -> R.drawable.comedy
            Movie.Genre.DRAMA -> R.drawable.drama
            Movie.Genre.FANTASY -> R.drawable.fantasy
            Movie.Genre.HORROR -> R.drawable.horror
            Movie.Genre.ROMANCE -> R.drawable.romance
            Movie.Genre.SCI_FI -> R.drawable.sci_fi
        }
    }

    override fun getItemCount(): Int = items.size

    fun addItem(item: Movie) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(movies: List<Movie>) {
        items.clear()
        items.addAll(movies)
        notifyDataSetChanged()
    }

    fun update(movie: Movie) {
        var oldMovie = items.find { m -> m.id == movie.id }.apply {
            this?.id = movie.id
            this?.title = movie.title
            this?.year = movie.year
            this?.length = movie.length
            this?.description = movie.description
            this?.genre = movie.genre
            this?.isSeen = movie.isSeen
        }
        notifyItemChanged(items.indexOf(oldMovie))
    }

    fun delete(item: Movie) {
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
    }

    inner class MovieViewHolder (val binding: MovieListBinding) : RecyclerView.ViewHolder(binding.root)
}