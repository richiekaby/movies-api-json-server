package net.larntech.movies.ui.new_movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import net.larntech.movies.config.BaseUrl
import net.larntech.movies.databinding.ActivityNewMovieBinding
import net.larntech.movies.model.movies.favourites.MoviesItem
import net.larntech.movies.ui.new_movie_details.NewMovieDetailsActivity
import net.larntech.movies.util.Status
import java.text.SimpleDateFormat
import java.util.*


class NewMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewMovieBinding
    private lateinit var customAdapter: CustomAdapter
    private lateinit var movieList: List<MoviesItem>
    private val viewModel: NewMovieViewModel by viewModels()
    private var sortByTitle = "asc"
    private var sortByPopularity = "asc"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData(){
        handleClick()
        handleSpinner()
        handleViewModel()
    }

    private fun handleSpinner(){
        ArrayAdapter.createFromResource(
            this,
            R.array.sort_popular,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.sortPopular.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.sort_title,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.sortTitle.adapter = adapter
        }
    }

    private fun handleClick(){
        binding.btnSearch.setOnClickListener {
            if(binding.edTitle.text != null && binding.edTitle.text.toString().isNotEmpty()){
                var title = binding.edTitle.text.toString()
                viewModel.getAllNewMovies(BaseUrl.MOVIE_API_KEY,title,null)
            }else{
                showMessage("Title Required ...")
            }
        }

        binding.sortPopular.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>, v: View?, i: Int, lng: Long) {
                when(i){
                    0 ->{
                        sortByPopularity = "asc"
                    }
                    1 ->{
                        sortByPopularity = "desc"
                    }
                    else->{

                    }
                }
                if(binding.edTitle.text != null && binding.edTitle.text.toString().isNotEmpty()){
                    var newItem: List<MoviesItem>;
                    if(sortByPopularity == "asc"){
                        newItem = movieList.sortedWith(compareBy { it.popularity })
                    }else{
                        newItem =  movieList.sortedWith(compareByDescending { it.popularity })
                    }
                    handleView(newItem)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }


        binding.sortTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>, v: View?, i: Int, lng: Long) {
                when(i){
                    0 ->{
                        sortByTitle = "asc"
                    }
                    1 ->{
                        sortByTitle = "desc"
                    }
                    else->{

                    }
                }

                var newItem: List<MoviesItem>;
                if(binding.edTitle.text != null && binding.edTitle.text.toString().isNotEmpty()){
                    if(sortByTitle == "asc"){
                        newItem = movieList.sortedWith(compareBy { it.title })
                    }else{
                        newItem = movieList.sortedWith(compareByDescending { it.title })
                    }
                    handleView(newItem)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }



    }


    private fun handleViewModel(){
        viewModel.allNewMovies.observe(this){
            when (it.status) {
                Status.SUCCESS -> {
                    handleProgressBar(false)
                    movieList = ArrayList()
                    if(it.data?.movies != null && it.data.movies.isNotEmpty()){
                        movieList = it.data?.movies
                        val sortedList =  movieList.sortedWith(compareBy { it.title })
                        handleView(sortedList)
                    }else{
                        showMessage("No movie found ...")
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

    private fun showMessage(message: String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    private fun handleView(moviesItem: List<MoviesItem>) {

        movieList = moviesItem


        customAdapter = CustomAdapter(movieList, this)
        binding.gridView.adapter = customAdapter;

        binding.gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                handleClickedMovie(movieList[position])
            }

    }

    private fun handleClickedMovie(movieItem: MoviesItem){
        startActivity(Intent(this, NewMovieDetailsActivity::class.java).putExtra("data",movieItem))
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
                convertView = layoutInflater!!.inflate(R.layout.row_new_movies, null)
            }


            val imageUri = data[position]
            val imageView = convertView?.findViewById<ImageView>(R.id.imageView)
            val tvTitle = convertView?.findViewById<TextView>(R.id.tvTitle)
            val tvScore = convertView?.findViewById<TextView>(R.id.tvPopularity)
            val tvYear = convertView?.findViewById<TextView>(R.id.tvYear)
            val uri = imageUri.posterPath
            val movieTitle = imageUri.title
            val moviePopularity = imageUri.popularity.toString()
            val movieYear = imageUri.releaseDate
            tvTitle!!.text = movieTitle
            tvScore!!.text = "Popularity $moviePopularity"
            val df = SimpleDateFormat("yyyy-MM-dd")
            val date = df.parse(movieYear)
            val calendar: Calendar = GregorianCalendar()
            calendar.time = date!!
            val year: Int = calendar.get(Calendar.YEAR)
            tvYear!!.text = "Year $year"
            loadImages("https://image.tmdb.org/t/p/original/$uri", imageView!!, context)
            return convertView!!
        }


        private fun loadImages(file: String, imageView: ImageView, context: Context) {
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

}