package com.example.factortestapp

import android.content.Context
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_game_over.*

class GameOver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        goHS.setText(intent.getStringExtra("HIGH_SCORE"))
        goYS.setText(intent.getStringExtra("YOUR_SCORE"))

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        Toast.makeText(this@GameOver, "You can't go back..!", Toast.LENGTH_SHORT).show()
    }
}
