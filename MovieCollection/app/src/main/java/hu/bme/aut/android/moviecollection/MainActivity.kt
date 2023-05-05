package hu.bme.aut.android.moviecollection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.moviecollection.adapter.MovieAdapter
import hu.bme.aut.android.moviecollection.data.Movie
import hu.bme.aut.android.moviecollection.data.MovieListDatabase
import hu.bme.aut.android.moviecollection.databinding.ActivityMainBinding
import hu.bme.aut.android.moviecollection.details.DetailsActivity
import hu.bme.aut.android.moviecollection.fragments.NewMovieDialogFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), MovieAdapter.MovieClickListener, NewMovieDialogFragment.NewMovieDialogListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: MovieListDatabase
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        setTheme(R.style.Theme_MovieCollection)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = MovieListDatabase.getDatabase(applicationContext)

        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val toolbarMenu: Menu = binding.toolbar.menu
        menuInflater.inflate(R.menu.menu_toolbar, toolbarMenu)
        for (i in 0 until toolbarMenu.size()) {
            val menuItem: MenuItem = toolbarMenu.getItem(i)
            menuItem.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
            if (menuItem.hasSubMenu()) {
                val subMenu: SubMenu = menuItem.subMenu
                for (j in 0 until subMenu.size()) {
                    subMenu.getItem(j)
                        .setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fab -> {
                NewMovieDialogFragment(null).show(
                    supportFragmentManager,
                    NewMovieDialogFragment.TAG
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.movieDAO().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: Movie) {
        NewMovieDialogFragment(item).show(
            supportFragmentManager,
            NewMovieDialogFragment.TAG
        )
        thread {
            database.movieDAO().update(item)
            Log.d("MainActivity", "Movie update was successful")
        }
    }

    override fun onItemDelete(item: Movie) {
        thread {
            database.movieDAO().deleteItem(item)
            runOnUiThread {
                adapter.delete(item)
            }
            Log.d("MainActivity", "Movie delete was successful")
        }
    }

    override fun onItemSelected(item: Movie) {
        val showDetailsIntent = Intent()
        showDetailsIntent.setClass(this@MainActivity, DetailsActivity::class.java)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_NAME, item.title)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_YEAR, item.year.toString())

        var hour: Int = item.length/60
        var min: Int = item.length - hour * 60

        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_LENGTH, "${hour} h ${min} m")
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_GENRE, item.genre.getAltName())
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_DESCRIPTION, item.description)
        startActivity(showDetailsIntent)
    }

    override fun onMovieCreated(newItem: Movie) {
        thread {
            val insertId = database.movieDAO().insert(newItem)
            newItem.id = insertId
            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    override fun onMovieEdited(item: Movie) {
        thread{
            val updateId = database.movieDAO().update(item)
            runOnUiThread {
                adapter.update(item)
            }
        }
    }
}