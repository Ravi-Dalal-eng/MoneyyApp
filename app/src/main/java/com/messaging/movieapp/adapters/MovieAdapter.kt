package com.messaging.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messaging.movieapp.databinding.MovieItemBinding
import com.messaging.movieapp.models.Movie

class MovieAdapter (private val context: Context,
                   private val movieList:List<Movie>,
                   private val onMovieClick:(position:Int)-> Unit,
                    private val onFavouriteClick:(position:Int)-> Unit,
                    private val onWatchLaterClick:(position:Int)-> Unit):
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,onMovieClick,onFavouriteClick,onWatchLaterClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList[position]

        holder.itemBinding.apply {
            originalTitle.setText(movie.original_title)
            Glide.with(context).load("https://image.tmdb.org/t/p/w342".plus(movie.poster_path)).into(poster)
        }

    }


    override fun getItemCount() = movieList.size

    class ViewHolder(val itemBinding: MovieItemBinding,
                    private val onMovieClick: (position: Int) -> Unit,
                   private val onFavouriteClick: (position: Int) -> Unit,
                   private val onWatchLaterClick: (position: Int) -> Unit)
                   : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{
                       init {
                           itemBinding.movie.setOnClickListener(this)
                           itemBinding.addFavourites.setOnClickListener(this)
                           itemBinding.addWatchlist.setOnClickListener(this)
                       }

        override fun onClick(view: View) {
            val position=adapterPosition
            if(view.id.equals(itemBinding.addFavourites.id))
                onFavouriteClick(position)
            else if(view.id.equals(itemBinding.addWatchlist.id))
                onWatchLaterClick(position)
            else
                onMovieClick(position)
        }
                   }

}