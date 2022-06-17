package com.messaging.movieapp.api

import com.messaging.movieapp.models.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY="b6a5b9d856d6373f1b2c8a6c5082a6cc"

interface MovieService {

       @GET("popular?api_key=$API_KEY")
       fun getMovies(@Query("page") page:Int): Call<MovieList>

}