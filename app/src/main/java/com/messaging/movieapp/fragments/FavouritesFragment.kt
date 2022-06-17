package com.messaging.movieapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.messaging.movieapp.MovieDetailActivity
import com.messaging.movieapp.R
import com.messaging.movieapp.adapters.SelectedMovieAdapter
import com.messaging.movieapp.databinding.FragmentFavouritesBinding
import com.messaging.movieapp.models.Movie
import java.util.ArrayList

class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding?=null
    private val binding get()=_binding!!
    private val movies= ArrayList<Movie>()
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentFavouritesBinding.bind(view)
        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()
        binding.favoutiteMoviesList.layoutManager=LinearLayoutManager(requireContext())
        binding.favoutiteMoviesList.adapter= SelectedMovieAdapter(requireContext(),movies,
            {position -> onMovieClick(position)})
        loadMovie()
    }

    private fun loadMovie() {
        database.reference.child(auth.currentUser!!.uid).child("favourite")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    movies.clear()
                    snapshot.children.forEach {
                        it.getValue(Movie::class.java)?.let { it1 -> movies.add(it1) }
                    }
                    binding.favoutiteMoviesList.adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun onMovieClick(position: Int) {
        val movie=movies.get(position)
        val intent= Intent(requireContext(), MovieDetailActivity::class.java)
        intent.putExtra("id",movie.id)
        intent.putExtra("original_title",movie.original_title)
        intent.putExtra("overview",movie.overview)
        intent.putExtra("poster_path",movie.poster_path)
        intent.putExtra("release_date",movie.release_date)
        intent.putExtra("vote_average",movie.vote_average)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}