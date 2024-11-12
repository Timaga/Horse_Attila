package com.example.horse_attila

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.horse_attila.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.toon)
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)
        val IsNightTheme = sharedPreferences.getBoolean("isNightTheme", true)
        binding.switchTheme.isChecked = IsNightTheme
        binding.switchMusic.isChecked = isMusicEnabled
        with(binding) {
            switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                with(sharedPreferences.edit()) {

                    if (isChecked){
                        putBoolean("isNightTheme", isChecked)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        println("Переключатель включен")
                        apply()
                    } else {
                        putBoolean("isNightTheme", isChecked)
                        // Включаем дневную тему
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        println("Переключатель выключен")
                        apply()
                    }
            }
            }

            binding.switchMusic.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                with(sharedPreferences.edit()) {
                    if (isChecked) {
                        putBoolean("isMusicEnabled", isChecked)
                        mediaPlayer.start()
                        apply()
                    }
                    else{
                        mediaPlayer.pause()
                        putBoolean("isMusicEnabled", isChecked)
                        apply()
                    }
                }

            }
        }
    }

}