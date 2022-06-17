package com.messaging.movieapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.messaging.movieapp.MovieDetailActivity
import com.messaging.movieapp.R
import com.messaging.movieapp.adapters.MovieAdapter
import com.messaging.movieapp.api.MovieService
import com.messaging.movieapp.api.RetrofitHelper
import com.messaging.movieapp.databinding.FragmentMoviesBinding
import com.messaging.movieapp.models.Movie
import com.messaging.movieapp.models.MovieList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding?=null
    private val binding get()=_binding!!
    private lateinit var movieService: MovieService
    private val movies=ArrayList<Movie>()
    private val selectedMovies=ArrayList<Movie>()
    private var page=1
    private var total_pages=1
    private var isScrolling=false
    private var currentItems=0
    private var totalItems=0
    private var scrollOutItems=0
    private lateinit var manager: LinearLayoutManager
    private lateinit var database:FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentMoviesBinding.bind(view)

        movieService=RetrofitHelper.getInstance().create(MovieService::class.java)
        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()

        manager=LinearLayoutManager(requireContext())
        binding.moviesList.layoutManager=manager
        binding.moviesList.adapter=MovieAdapter(requireContext(),selectedMovies,
            {position -> onMovieClick(position)},
            {position -> onFavouriteClick(position)},
            {position -> onWatchLaterClick(position)})
            loadMovie(page)

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(charSequence.length>0)
                    binding.cancelEditText.visibility=View.VISIBLE
                else
                    binding.cancelEditText.visibility=View.INVISIBLE
                selectedMovies.clear()
                movies.forEach {
                    if(it.original_title.contains(charSequence))
                        selectedMovies.add(it)
                }
               (binding.moviesList.adapter as MovieAdapter).notifyDataSetChanged()
            }
        })
        binding.cancelEditText.setOnClickListener {
            binding.searchEditText.setText("")
            binding.cancelEditText.visibility=View.INVISIBLE
            val currentFocusview = requireActivity().currentFocus

            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocusview?.windowToken, 0)
        }
        binding.moviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager.getChildCount()
                totalItems = manager.getItemCount()
                scrollOutItems = manager.findFirstVisibleItemPosition()
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    isScrolling = false
                    if(page<total_pages && binding.searchEditText.text.toString().equals("")){
                      page++
                      loadMovie(page)
                    }

                }
            }
        })
    }

    private fun loadMovie(page: Int) {
         val movieResult= movieService.getMovies(page)

        movieResult.enqueue(object : Callback<MovieList> {
            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Toast.makeText(requireContext(),"Check your internet connection",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if(response.isSuccessful && response.body()!=null){
                    total_pages=response.body()!!.total_pages
                movies.addAll(response.body()!!.results)
                    selectedMovies.addAll(response.body()!!.results)
                   binding.moviesList.adapter!!.notifyDataSetChanged()
                }
            }

        })
    }
    private fun onMovieClick(position: Int) {
        val movie=selectedMovies.get(position)
        val intent= Intent(requireContext(), MovieDetailActivity::class.java)
        intent.putExtra("id",movie.id)
        intent.putExtra("original_title",movie.original_title)
        intent.putExtra("overview",movie.overview)
        intent.putExtra("poster_path",movie.poster_path)
        intent.putExtra("release_date",movie.release_date)
        intent.putExtra("vote_average",movie.vote_average)
        startActivity(intent)
    }
    private fun onFavouriteClick(position: Int) {
        val movie=selectedMovies.get(position)
        val data=getData(position)
         database.reference.child(auth.currentUser!!.uid).child("favourite")
             .child(movie.id.toString()).setValue(data).addOnSuccessListener {
                 Toast.makeText(requireContext(),"Movie added to Favourite",Toast.LENGTH_SHORT).show()
             }
    }
    private fun onWatchLaterClick(position: Int) {
        val movie=selectedMovies.get(position)
        val data=getData(position)
        database.reference.child(auth.currentUser!!.uid).child("watchlist")
            .child(movie.id.toString()).setValue(data).addOnSuccessListener {
                Toast.makeText(requireContext(),"Movie added to Watch List",Toast.LENGTH_SHORT).show()
            }
    }
    private fun getData(position: Int):HashMap<String,Any>{
        val movie=selectedMovies.get(position)
        val data=HashMap<String,Any>()
        data.put("id",movie.id)
        data.put("original_title",movie.original_title)
        data.put("overview",movie.overview)
        data.put("poster_path",movie.poster_path)
        data.put("release_date",movie.release_date)
        data.put("vote_average",movie.vote_average)
        return data
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
