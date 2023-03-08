package net.larntech.movies.ui.new_movie_details

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import net.larntech.movies.R
import net.larntech.movies.databinding.ActivityNewMovieDetailsBinding
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.ui.main.MainActivity
import net.larntech.movies.ui.rating.RateUsBottomDialog
import net.larntech.movies.util.Status

class NewMovieDetailsActivity : AppCompatActivity(),RateUsBottomDialog.RatingInterface {

    private lateinit var binding: ActivityNewMovieDetailsBinding
    private lateinit var moviesItem: MoviesItem
    private val viewModel: NewMovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMovieDetailsBinding.inflate(layoutInflater)
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
        viewModel.addFavourite.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    handleProgressBar(false)

                    if(it.data != null) {
                        moviesItem = it.data
                        showMessage("Added successfully ...")
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
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },1500)

    }

    private fun handleClickListener(){
        binding.llFirst.setOnClickListener {
            var rateDialog = RateUsBottomDialog(this, moviesItem.myScore.toString())
            rateDialog.show(supportFragmentManager, "")
        }

        binding.llSecond.setOnClickListener {
//            if(moviesItem.favorite){
//                var newMovieItem = moviesItem
//                newMovieItem.favorite = false
//                viewModel.addFavourite(newMovieItem)
//
//            }else{
//                var newMovieItem = moviesItem
//                newMovieItem.favorite = true
//                viewModel.addFavourite(newMovieItem)
//            }
            var rateDialog = RateUsBottomDialog(this, moviesItem.myScore.toString())
            rateDialog.show(supportFragmentManager, "")
        }
    }

    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showData(){
        loadImages("https://image.tmdb.org/t/p/original/${moviesItem.posterPath}")
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

    private fun loadImagesA(file: String) {
        //load image
        Glide.with(this)
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(ContextCompat.getDrawable(this, R.drawable.baseline_image_24))
            .into(binding.imageView)
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
        newMovieItem.favorite = true
        newMovieItem.posterPath = "https://image.tmdb.org/t/p/original/${moviesItem.posterPath}"
        viewModel.addFavourite(newMovieItem)
    }

    private fun handleProgressBar(show: Boolean){
        if(show) {
            binding.progressBarInclude.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBarInclude.progressBar.visibility = View.GONE
        }
    }
}