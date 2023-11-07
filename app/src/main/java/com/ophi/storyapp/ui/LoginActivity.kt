package com.ophi.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.ophi.storyapp.R
import com.ophi.storyapp.databinding.ActivityLoginBinding
import com.ophi.storyapp.model.LoginViewModel
import com.ophi.storyapp.model.ViewModelFactory
import com.ophi.storyapp.pref.UserModel
import com.ophi.storyapp.repository.Result

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnLogin -> {
                login()
            }

            binding.btnSignUp -> {
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun login() {
        val edEmail = binding.edLoginEmail.text.toString()
        val edPassword = binding.edLoginPassword.text.toString()

        viewModel.login(edEmail, edPassword).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        viewModel.saveSession(
                            UserModel(
                                result.data.loginResult.name,
                                result.data.loginResult.token
                            )
                        )

                        Toast.makeText(this, result.data.message, Toast.LENGTH_LONG).show()
                        Log.d("Success", result.data.message)

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                        Log.d("Error", result.error)
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}