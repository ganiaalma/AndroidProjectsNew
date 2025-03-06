package com.example.tentangsaya

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login_signup.databaseHelper
import com.example.tentangsaya.databinding.ActivityLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var dbHelper : databaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = databaseHelper(this)

        if(dbHelper.isUserTableEmpty()) {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }else{
            binding.button.setOnClickListener{
                val username = binding.etUsername.text.toString()
                val password = binding.etPassword.text.toString()

                loginDatabase(username, password)
            }

            binding.textView5.setOnClickListener{
                startActivity(Intent(this, SignupActivity::class.java))
            }
        }
    }

    private fun loginDatabase(username : String, password : String){
        val userExists = dbHelper.readUser(username, password)

        if(username.isEmpty() && password.isEmpty()) {
            showError("Username dan Password tidak boleh kosong!")
            return
        }

        if(userExists) {
            Toast.makeText(this, "Anda Berhasil Login!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else {
            MaterialAlertDialogBuilder(this)
                .setTitle("Login Gagal")
                .setMessage("Username atau Password salah. Coba lagi!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private  fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}