package hu.bme.aut.android.moviecollection.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.moviecollection.R
import hu.bme.aut.android.moviecollection.data.Movie
import hu.bme.aut.android.moviecollection.databinding.DialogNewMovieBinding
import java.util.*

class NewMovieDialogFragment(movie: Movie?) : DialogFragment() {
    private val movie: Movie?
    private lateinit var genreArray: Array<String>

    init {
        this.movie = movie
    }

    interface NewMovieDialogListener {
        fun onMovieCreated(newItem: Movie)
        fun onMovieEdited(item: Movie)
    }
    private lateinit var listener: NewMovieDialogListener

    private lateinit var binding: DialogNewMovieBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewMovieDialogListener
            ?: throw RuntimeException("Activity must implement the NewMovieDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewMovieBinding.inflate(LayoutInflater.from(context))
        binding.spGenre.adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.genre_items)
        )
        if(!Objects.isNull(movie)) {
            genreArray = resources.getStringArray(R.array.genre_items)

            binding.etTitle.setText(movie?.title ?: "")
            binding.etYear.setText(movie?.year.toString() ?: "")
            binding.etLength.setText(movie?.length.toString() ?: "")
            binding.etDescription.setText(movie?.description ?: "")
            binding.spGenre.setSelection(genreArray.indexOf(movie?.genre?.getAltName()))
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_movie)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if(!Objects.isNull(movie)) {
                    if (isValid()) {
                        listener.onMovieEdited(getMovie())
                    }
                    else {
                        binding.etTitle.setText("Unknown")
                        listener.onMovieEdited(getMovie())
                    }
                }
                else{
                    if (isValid()) {
                        listener.onMovieCreated(getMovie())
                    }
                    else {
                        binding.etTitle.setText("Unknown")
                        listener.onMovieCreated(getMovie())
                    }
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etTitle.text.isNotEmpty()

    private fun getMovie() = Movie(
        id = movie?.id,
        title = binding.etTitle.text.toString(),
        year = binding.etYear.text.toString().toIntOrNull() ?:0,
        length = binding.etLength.text.toString().toIntOrNull() ?:0,
        genre = Movie.Genre.getByOrdinal(binding.spGenre.selectedItemPosition) ?: Movie.Genre.ACTION,
        description = binding.etDescription.text.toString(),
        isSeen = binding.cbAlreadySeen.isChecked
    )

    companion object {
        const val TAG = "NewMovieDialogFragment"
    }
}
