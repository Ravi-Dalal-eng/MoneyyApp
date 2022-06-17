package com.messaging.movieapp.models

data class Movie(val id:Int,
                 val original_title:String,
                 val overview:String,
                 val poster_path:String,
                 val release_date:String,
                 val vote_average:Float){
    constructor():this(0,"","","","",0.0F)
}
