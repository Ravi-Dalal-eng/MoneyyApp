package com.messaging.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.messaging.movieapp.databinding.MovieItemBinding
import com.messaging.movieapp.databinding.SavedMovieItemBinding
import com.messaging.movieapp.models.Movie

class SelectedMovieAdapter(private val context: Context,
                           private val movieList:List<Movie>,
                           private val onMovieClick:(position:Int)-> Unit)
                          : RecyclerView.Adapter<SelectedMovieAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMovieAdapter.ViewHolder {
        val binding = SavedMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,onMovieClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList[position]

        holder.itemBinding.apply {
            originalTitle.setText(movie.original_title)
            Glide.with(context).load("https://image.tmdb.org/t/p/w342".plus(movie.poster_path)).into(poster)
        }

    }


    override fun getItemCount() = movieList.size

    class ViewHolder(val itemBinding: SavedMovieItemBinding,
                     private val onMovieClick: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{
        init {
            itemBinding.movie.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position=adapterPosition
                onMovieClick(position)
        }
    }
}