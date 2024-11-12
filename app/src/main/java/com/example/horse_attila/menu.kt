package com.example.horse_attila

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.horse_attila.databinding.ActivityMenuBinding

class menu : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)
        if (isMusicEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.toon)
            mediaPlayer.start()
        }
        val isThemeDark = sharedPreferences.getBoolean("isNightTheme", true)
        if (isThemeDark) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        with(binding){
           if (isDarkTheme()) {
                icMenu.setImageResource(R.drawable.ic_attila_horse_menu_night)

            } else {
               icMenu.setImageResource(R.drawable.ic_attila_horse_menu)
            }

            buttonStart.setOnClickListener {
                val intent = Intent(this@menu, MainActivity::class.java)
                startActivity(intent)
            }
            buttonSettings.setOnClickListener {
                val intent = Intent(this@menu, SettingsActivity::class.java)
                startActivity(intent)
            }
        }


    }
    override fun onDestroy() {
        super.onDestroy()

        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
    private fun isDarkTheme(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

}