package com.ophi.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.ophi.storyapp.databinding.ActivitySignupBinding
import com.ophi.storyapp.model.SignupViewModel
import com.ophi.storyapp.model.ViewModelFactory
import com.ophi.storyapp.repository.Result

class SignupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignupBinding

    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnSignUp -> {
                signup()
            }

            binding.btnLogin -> {
                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signup() {
        val edName = binding.edRegisterName.text.toString()
        val edEmail = binding.edRegisterEmail.text.toString()
        val edPassword = binding.edRegisterPassword.text.toString()

        viewModel.signup(edName, edEmail, edPassword).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(this, result.data.message, Toast.LENGTH_LONG).show()
                        Log.d("Success", result.data.message)

                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)
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