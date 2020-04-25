package com.example.factortestapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_game.*
import java.text.DecimalFormat
import java.text.NumberFormat


class GameActivity : AppCompatActivity() {

    var randomFactor: Int = 0
    var factorArray : ArrayList<Int> = ArrayList<Int>()
    var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

            var sp: SharedPreferences = getSharedPreferences("HighScore", Context.MODE_PRIVATE)
            highScoreText.setText(sp.getString("HighScore", scoreText.text.toString()))

        setInvisible()

        goBtn.setOnClickListener {
            hideKeyboard()
            if (enteredNo.text.trim().isNotEmpty()){
                setVisible()
                question.setText("Which of the following is the factor of ${enteredNo.text.toString()} ?")
                factorArray = factor(enteredNo.text.toString().toInt())
                randomFactor = factorArray[(0 until factorArray.size).random()]
                val randomBtn = (1..3).random()
                when(randomBtn){
                    1 -> {
                        opt1Btn.setText(randomFactor.toString())
                        opt2Btn.text = randomOption(randomFactor)
                        opt3Btn.text = randomOption(randomFactor)
                    }
                    2 -> {
                        opt1Btn.text = randomOption(randomFactor)
                        opt2Btn.setText(randomFactor.toString())
                        opt3Btn.text = randomOption(randomFactor)
                    }
                    3 -> {
                        opt1Btn.text = randomOption(randomFactor)
                        opt2Btn.text = randomOption(randomFactor)
                        opt3Btn.setText(randomFactor.toString())
                    }
                }
                myCountDownTimer.start()
            }else {
                Toast.makeText(this@GameActivity, "Enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }

        opt1Btn.setOnClickListener {
            if (opt1Btn.text == randomFactor.toString()){
                myResponse(true)

            }else{
                myResponse(false)
            }
        }

        opt2Btn.setOnClickListener {
            if (opt2Btn.text == randomFactor.toString()){
                myResponse(true)
            }else{
                myResponse(false)
            }
        }

        opt3Btn.setOnClickListener {
            if (opt3Btn.text == randomFactor.toString()){
                myResponse(true)
            }else{
                myResponse(false)
            }
        }

        nxtQnBtn.setOnClickListener {
            enableOptBtns()
            setInvisible()
            resetTextAndColor()
        }

        exitBtn.setOnClickListener {
            gameOver()
        }

    }

    override fun onPause() {
        super.onPause()
        if(scoreText.text.isNotEmpty()){
            if (highScoreText.text.toString().toInt()<scoreText.text.toString().toInt()){
                var sp: SharedPreferences = getSharedPreferences("HighScore", Context.MODE_PRIVATE)
                var editor: SharedPreferences.Editor = sp.edit()
                editor.putString("HighScore", scoreText.text.toString())
                editor.commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(scoreText.text.isNotEmpty()){
            if (highScoreText.text.toString().toInt()<scoreText.text.toString().toInt()){
                highScoreText.setText(scoreText.text.toString())
                outState.putString("HighScore", scoreText.text.toString())
            }
        }
    }

    fun setInvisible(){
        countDown.visibility = View.INVISIBLE
        question.visibility = View.INVISIBLE
        btnLayout.visibility = View.INVISIBLE
        response.visibility = View.INVISIBLE
        nxtQnBtn.visibility = View.INVISIBLE
    }

    fun setVisible(){
        countDown.visibility = View.VISIBLE
        question.visibility = View.VISIBLE
        btnLayout.visibility = View.VISIBLE
        response.visibility = View.VISIBLE
    }

    fun resetTextAndColor(){
        opt1Btn.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        opt2Btn.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        opt3Btn.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        response.setText("")
        enteredNo.setText("")
        myBg.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
    }

    fun factor(num: Int): ArrayList<Int> {
        val arrayOfFactors : ArrayList<Int> = ArrayList<Int>()
            for (i in 1..num) {
                if (num % i == 0) {
                    arrayOfFactors.add(i)
                }
            }
        return arrayOfFactors

    }

    fun randomOption(rightAns: Int): String{
        if (rightAns == 1 || rightAns == 2){
            return "3"
        }
        var x:Int = (1 until enteredNo.text.toString().toInt()).random()
        while(x==rightAns || (x in factorArray)){
            x = (1 until enteredNo.text.toString().toInt()).random()
        }
        return  x.toString()
    }

    fun myResponse(ansRes: Boolean){

        myCountDownTimer.cancel()
        if (ansRes){
            myBg.setBackgroundColor(ContextCompat.getColor(this, R.color.rightAnswer))
            btnColor()
            score = score + 5
            scoreText.setText(score.toString())
            disableOptBtns()
            response.setText("Yay...! You are Right..!")
            nxtQnBtn.visibility = View.VISIBLE
        }else{
            myBg.setBackgroundColor(ContextCompat.getColor(this, R.color.wrongAnswer))
            btnColor()
            disableOptBtns()
            response.setText("Oops..! Better luck next time...!")
            vibrate(1000)
            nxtQnBtn.visibility = View.VISIBLE
        }
    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        // else {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        // }
    }

    fun btnColor(){
        if (opt1Btn.text == randomFactor.toString()){
            opt1Btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        }else if(opt2Btn.text == randomFactor.toString()){
            opt2Btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        }else{
            opt3Btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        }
    }

    fun disableOptBtns(){
        goBtn.isEnabled = false
        opt1Btn.isEnabled = false
        opt2Btn.isEnabled = false
        opt3Btn.isEnabled = false
    }

    fun enableOptBtns(){
        goBtn.isEnabled = true
        opt1Btn.isEnabled = true
        opt2Btn.isEnabled = true
        opt3Btn.isEnabled = true
    }

    fun Context.vibrate(milliseconds:Long = 500){
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Check whether device/hardware has a vibrator
        val canVibrate:Boolean = vibrator.hasVibrator()

        if(canVibrate){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                // void vibrate (VibrationEffect vibe)
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        // The default vibration strength of the device.
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }else{
                // This method was deprecated in API level 26
                vibrator.vibrate(milliseconds)
            }
        }
    }

    val Context.hasVibrator:Boolean
        get() {
            val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            return vibrator.hasVibrator()
        }


    val myCountDownTimer = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val f: NumberFormat = DecimalFormat("00")
            val sec = millisUntilFinished / 1000 % 60
            countDown.setText("00:00" + ":" + f.format(sec))
        }

        override fun onFinish() {
            countDown.setText("00:00:00")
            gameOver()
        }
    }

    fun gameOver(){
        val intent = Intent(this@GameActivity, GameOver::class.java)
        if (scoreText.text.toString().toInt() > highScoreText.text.toString().toInt()){
            intent.putExtra("HIGH_SCORE", scoreText.text.toString())
        }else{
            intent.putExtra("HIGH_SCORE", highScoreText.text.toString())
        }
        intent.putExtra("YOUR_SCORE", scoreText.text.toString())
        startActivity(intent)
    }
}

