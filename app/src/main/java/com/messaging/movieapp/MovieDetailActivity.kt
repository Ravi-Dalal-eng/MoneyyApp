package com.messaging.movieapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.messaging.movieapp.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val channelId = "12345"
    private val description = "Movie is Playing"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val extras=intent.extras
        val id=extras!!.get("id")
        val original_title=extras!!.get("original_title")
        val overview=extras!!.get("overview")
        val poster_path=extras!!.get("poster_path")
        val release_date=extras!!.get("release_date")
        val vote_average=extras!!.get("vote_average")

        Glide.with(this).load("https://image.tmdb.org/t/p/w342".plus(poster_path))
            .into(binding.poster)
        binding.originalTitle.setText(original_title.toString())
        binding.overview.setText(overview.toString())
        binding.releaseDate.setText(binding.releaseDate.text.toString().plus(release_date.toString()))
        binding.userRatings.setText(binding.userRatings.text.toString().plus(vote_average.toString()))
        binding.progressBar.setProgressWithAnimation(vote_average.toString().toFloat(),1500)
        binding.playNow.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager .IMPORTANCE_HIGH)
                notificationChannel.lightColor = Color.BLUE
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
                builder = Notification.Builder(this, channelId).setContentTitle("Movie App")
                    .setContentText(description).setSmallIcon(R.drawable.ic_launcher_foreground).setContentIntent(pendingIntent)
            }
            notificationManager.notify(12345, builder.build())
        }
    }
}
