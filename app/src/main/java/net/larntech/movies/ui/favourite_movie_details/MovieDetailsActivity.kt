package net.larntech.movies.ui.favourite_movie_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import net.larntech.movies.R
import net.larntech.movies.databinding.ActivityMovieDetailsBinding
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.ui.main.MainActivity
import net.larntech.movies.ui.rating.RateUsBottomDialog
import net.larntech.movies.util.Status

class MovieDetailsActivity : AppCompatActivity(), RateUsBottomDialog.RatingInterface {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var moviesItem: MoviesItem
    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData(){
        handleViewModel()
        getMovieData()
        handleClickListener()
    }

    private fun getMovieData(){
        var intent = intent
        moviesItem = intent.getSerializableExtra("data") as MoviesItem
        if(moviesItem != null) {
            showData()
        }else{
            showMessage("No movie details found")
        }

    }

    private fun handleViewModel(){
        viewModel.updateFavourite.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    handleProgressBar(false)

                    if(it.data != null) {
                        moviesItem = it.data
                        showMessage("Update successful")
                        gobackToMain()
                    }else{
                        showMessage("Unable to update")
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

    private fun gobackToMain(){
        Handler().postDelayed(Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },1500)

    }


    private fun handleClickListener(){
        binding.llFirst.setOnClickListener {
            var rateDialog = RateUsBottomDialog(this, moviesItem.myScore.toString())
            rateDialog.show(supportFragmentManager, "")
        }

        binding.llSecond.setOnClickListener {
            if(moviesItem.favorite){
                var newMovieItem = moviesItem
                newMovieItem.favorite = false
                viewModel.updateFavourite(newMovieItem)

            }else{
                var newMovieItem = moviesItem
                newMovieItem.favorite = true
                viewModel.updateFavourite(newMovieItem)
            }

        }
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showData(){
        loadImages(moviesItem.posterPath)
        binding.tvTitle.text = moviesItem.title
        binding.tvOriginalLanguage.text = moviesItem.originalLanguage
        binding.tvOriginalTitle.text = moviesItem.originalTitle
        binding.tvOverview.text = moviesItem.overview
        binding.tvPopularity.text = moviesItem.popularity.toString()
        binding.tvReleaseDate.text = moviesItem.releaseDate
        binding.tvVoteAverage.text = moviesItem.voteAverage.toString()
        binding.tvVoteCount.text = moviesItem.voteCount.toString()
        binding.tvMyScore.text = moviesItem.myScore.toString() + " / 10"

        if(moviesItem.favorite){
            binding.tvFavourite.text = "Yes"
            binding.tvRemoveFavourite.text = "REMOVE FAVOURITE"
        }else{
            binding.tvFavourite.text = "No"
            binding.tvRemoveFavourite.text = "ADD FAVOURITE"
        }

        if(moviesItem.adult){
            binding.tvAdult.text = "Yes"
        }else{
            binding.tvAdult.text = "No"
        }

    }

    private fun loadImages(file: String) {
        //load image
        Glide.with(this)
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(ContextCompat.getDrawable(this, R.drawable.baseline_image_24))
            .into(binding.imageView)
    }

    override fun newRating(rateValue: Int) {
        var newMovieItem = moviesItem
        newMovieItem.myScore = rateValue
        viewModel.updateFavourite(newMovieItem)
    }

    private fun handleProgressBar(show: Boolean){
        if(show) {
            binding.progressBarInclude.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBarInclude.progressBar.visibility = View.GONE
        }
    }
}