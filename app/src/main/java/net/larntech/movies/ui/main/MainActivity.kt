package net.larntech.movies.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.Call.Details
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import net.larntech.movies.R
import net.larntech.movies.databinding.ActivityMainBinding
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.ui.details.MovieDetailsActivity
import net.larntech.movies.util.Status
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var customAdapter: CustomAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var movieList: MutableList<MoviesItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData(){
        handleViewModel()
    }

    private fun getAllFavouriteMovies(){
        viewModel.getFavoritesMovies()
    }

    private fun handleViewModel(){
        viewModel.allFavouriteMovies.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    handleProgressBar(false)
                    movieList = ArrayList()

                    if(it.data != null && it.data.isNotEmpty()) {
                        for (movie in it.data){
                            if(movie.favorite){
                                movieList.add(movie)
                            }
                        }
                        handleView(movieList)
                    }else{
                        showMessage("No favourite movie found ...")
                    }
                }
                Status.ERROR -> {
                    handleProgressBar(false)

                    showMessage(it.message!!)
                }
                Status.LOADING -> {
                    handleProgressBar(true)
                }
                else -> {
                    handleProgressBar(false)

                    Log.e(" else ", " ====> auth")
                }
            }
        }
    }

    private fun handleView(moviesItem: List<MoviesItem>) {

        customAdapter = CustomAdapter(moviesItem, this)
        binding.gridView.adapter = customAdapter;

        binding.gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                handleClickedMovie(movieList[position])
            }

    }

    private fun handleClickedMovie(movieItem: MoviesItem){
        startActivity(Intent(this, MovieDetailsActivity::class.java).putExtra("data",movieItem))
    }

    private fun showMessage(message: String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    class CustomAdapter(var data: List<MoviesItem>, var context: Context) : BaseAdapter() {

        private var layoutInflater: LayoutInflater? = null

        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (layoutInflater == null) {
                layoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            if (convertView == null) {
                convertView = layoutInflater!!.inflate(R.layout.row_movies, null)
            }


            val imageUri = data[position]
            val imageView = convertView?.findViewById<ImageView>(R.id.imageView)
            val tvTitle = convertView?.findViewById<TextView>(R.id.tvTitle)
            val tvScore = convertView?.findViewById<TextView>(R.id.tvScore)
            val tvYear = convertView?.findViewById<TextView>(R.id.tvYear)
            val uri = imageUri.posterPath
            val movieTitle = imageUri.title
            val movieScore = imageUri.myScore.toString() + " / 10 "
            val movieYear = imageUri.releaseDate
            tvTitle!!.text = movieTitle
            tvScore!!.text = movieScore
            val df = SimpleDateFormat("yyyy-MM-dd")
            val date = df.parse(movieYear)
            val calendar: Calendar = GregorianCalendar()
            calendar.time = date!!
            val year: Int = calendar.get(Calendar.YEAR)
            tvYear!!.text = year.toString()
            loadImages(Uri.parse(uri), imageView!!, context)
            return convertView!!
        }


        private fun loadImages(file: Uri, imageView: ImageView, context: Context) {
            //load image
            Glide.with(context)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.baseline_image_24))
                .into(imageView)
        }


    }

    private fun handleProgressBar(show: Boolean){
        if(show) {
            binding.progressBarInclude.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBarInclude.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        getAllFavouriteMovies()
    }
}