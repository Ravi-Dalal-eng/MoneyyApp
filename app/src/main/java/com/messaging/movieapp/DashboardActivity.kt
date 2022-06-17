package com.messaging.movieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.messaging.movieapp.databinding.ActivityDashboardBinding
import com.messaging.movieapp.databinding.ActivityMainBinding
import com.messaging.movieapp.fragments.FavouritesFragment
import com.messaging.movieapp.fragments.MoviesFragment
import com.messaging.movieapp.fragments.WatchlistFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id,MoviesFragment()).commit()
        
        binding.bottomNavigationView
            .setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    if (!item.isChecked) {
                        when (item.itemId) {
                            R.id.movies -> {
                                supportFragmentManager.beginTransaction()
                                    .replace(binding.frameLayout.id,MoviesFragment()).commit()
                                return true
                            }
                            R.id.favourites -> {
                                supportFragmentManager.beginTransaction()
                                    .replace(binding.frameLayout.id,FavouritesFragment()).commit()
                                return true
                            }
                            R.id.watchlist -> {
                                supportFragmentManager.beginTransaction()
                                    .replace(binding.frameLayout.id,WatchlistFragment()).commit()
                                return true
                            }
                        }
                    }
                    return false
                }
            })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
