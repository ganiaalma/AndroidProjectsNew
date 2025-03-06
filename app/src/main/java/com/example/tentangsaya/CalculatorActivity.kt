package com.example.tentangsaya

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalculatorActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
    private var isNewOperation = true
    private lateinit var mediaPlayer1: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer
    private lateinit var mediaPlayer3: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvResult = findViewById(R.id.tvResult)

        // Initialize MediaPlayer
        mediaPlayer1 = MediaPlayer.create(this, R.raw.audio1)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.audio2)
        mediaPlayer3 = MediaPlayer.create(this, R.raw.audio5)

        val numberButtons = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtons.forEachIndexed { index, buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                mediaPlayer1.start()
                onNumberClick(index.toString())
            }
        }

        findViewById<Button>(R.id.buttonKembali).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Operation buttons
        findViewById<Button>(R.id.btnPlus).setOnClickListener {
            mediaPlayer2.start()
            onOperationClick("+")
        }
        findViewById<Button>(R.id.btnMinus).setOnClickListener {
            mediaPlayer2.start()
            onOperationClick("-")
        }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener {
            mediaPlayer2.start()
            onOperationClick("*")
        }
        findViewById<Button>(R.id.btnDivide).setOnClickListener {
            mediaPlayer2.start()
            onOperationClick("÷")
        }
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            mediaPlayer3.start()
            onEqualsClick()
        }
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            mediaPlayer3.start()
            onClearClick()
        }
    }

    private fun onNumberClick(number: String) {
        if (tvResult.text == "0" || isNewOperation) {
            tvResult.text = number
        } else {
            tvResult.append(number)
        }
        isNewOperation = false
    }

    private fun onOperationClick(op: String) {
        val currentText = tvResult.text.toString()

        if (currentText.isEmpty() || currentText.last().toString() in arrayOf("+", "-", "*", "÷")) {
            return
        }

        tvResult.append(" $op ")
        isNewOperation = false
    }

    private fun onEqualsClick() {
        try {
            val expression = tvResult.text.toString()
            val result = evaluateExpression(expression)

            tvResult.text = if (result.toLong().toDouble() == result) {
                result.toLong().toString()
            } else {
                result.toString()
            }

            isNewOperation = true
        } catch (e: Exception) {
            tvResult.text = "Error"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split(" ").toMutableList()

        // Process multiplication and division first
        var i = 0
        while (i < tokens.size) {
            if (tokens[i] == "*" || tokens[i] == "÷") {
                val left = tokens[i - 1].toDouble()
                val right = tokens[i + 1].toDouble()
                val result = if (tokens[i] == "*") left * right else left / right

                tokens[i - 1] = result.toString()
                tokens.removeAt(i)
                tokens.removeAt(i)
                i--
            }
            i++
        }

        // Process addition and subtraction
        i = 0
        while (i < tokens.size) {
            if (tokens[i] == "+" || tokens[i] == "-") {
                val left = tokens[i - 1].toDouble()
                val right = tokens[i + 1].toDouble()
                val result = if (tokens[i] == "+") left + right else left - right

                tokens[i - 1] = result.toString()
                tokens.removeAt(i)
                tokens.removeAt(i)
                i--
            }
            i++
        }

        return tokens[0].toDouble()
    }

    private fun onClearClick() {
        tvResult.text = "0"
        isNewOperation = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer1.release()
        mediaPlayer2.release()
        mediaPlayer3.release()
    }
}