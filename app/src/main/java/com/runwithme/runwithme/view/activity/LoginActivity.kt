package com.runwithme.runwithme.view.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.runwithme.runwithme.databinding.ActivityLoginBinding
import com.runwithme.runwithme.model.network.LoginRequest
import com.runwithme.runwithme.network.NetworkResult
import com.runwithme.runwithme.network.TokenStoreImpl
import com.runwithme.runwithme.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var tokenStore: TokenStoreImpl
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenStore = TokenStoreImpl()
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.buttonLogin.setOnClickListener {
            login()
        }

    }
    private fun login(){
        val email= binding.email.text.toString()
        val password = binding.password.text.toString()

        loginViewModel.login(LoginRequest(email,password))
        loginViewModel.loginResponse.observe(this, { response ->
            when(response){
                is NetworkResult.Success -> {
                    if(response.data?.user != null) {
                        tokenStore.setJwt(response.data?.token)
                        changeScreen()
                    }
                }
                is NetworkResult.Error -> {
                    binding.errorTextView.text = response.message
                    binding.errorTextView.visibility = View.VISIBLE
                }
            }
        })
    }

    fun changeScreen(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
