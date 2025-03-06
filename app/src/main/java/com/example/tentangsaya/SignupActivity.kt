package com.example.tentangsaya

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.login_signup.databaseHelper
import com.example.tentangsaya.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private lateinit var dbHelper: databaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = databaseHelper(this)

        binding.button.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            signupDatabase(username, password)
        }

        binding.textView5.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    private fun signupDatabase(username : String, password : String){
        val insertRowId = dbHelper.insertUser(username, password)

        if(username.isEmpty() && password.isEmpty()) {
            showError("Username dan Password tidak boleh kosong!")
            return
        }

        if(insertRowId != -1L){
            Toast.makeText(this, "Sign Up Berhasil!", Toast.LENGTH_SHORT). show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else {
            Toast.makeText(this, "Sign Up Gagal!", Toast.LENGTH_SHORT).show()
        }
    }

    private  fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}