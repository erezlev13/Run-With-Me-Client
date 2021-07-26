package com.runwithme.runwithme.view.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivityLoginBinding
import com.runwithme.runwithme.model.network.LoginRequest
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.SessionManager
import com.runwithme.runwithme.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var sessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        sessionManager = SessionManager(this)

        binding.emailTextInputEditText.doOnTextChanged { text, start, before, count ->
            if(binding.emailTextInputLayout.error == getString(R.string.email_empty_error)){
                binding.emailTextInputLayout.error = null
            }
        }
        binding.passwordTextInputEditText.doOnTextChanged { text, start, before, count ->
            if(binding.passwordTextInputLayout.error == getString(R.string.password_empty_error)){
                binding.passwordTextInputLayout.error = null
            }
        }

        binding.loginButton.setOnClickListener {
            onClickLoginButton()
        }
        binding.signupQuestionTextView.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        readLocalUserData()
    }
    private fun readLocalUserData() {
        loginViewModel.readUser.observeOnce(this,{ database ->
            if(database.isNotEmpty()){
                loginViewModel.isValidToken(database[0].token)
                loginViewModel.tokenResponse.observeOnce(this,{ response ->
                    when(response){
                        is NetworkResult.Success -> {
                            if(response.data?.isValidToken != null) {
                                if(response.data.isValidToken){
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                })
            }
        })
    }

    private fun login(){
        val email= binding.emailTextInputEditText.text.toString()
        val password = binding.passwordTextInputEditText.text.toString()

        loginViewModel.login(LoginRequest(email,password))
        loginViewModel.loginResponse.observeOnce(this, { response ->
            when(response){
                is NetworkResult.Success -> {
                    if(response.data?.user != null) {
                        sessionManager.saveAuthToken(response.data?.token)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                is NetworkResult.Error -> {
                    binding.errorTextView.text = response.message
                    binding.errorTextView.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun onClickLoginButton(){
        var emailFilled : Boolean = true
        var passwordFilled : Boolean = true
        if(binding.emailTextInputEditText.text!!.isEmpty()){
            binding.emailTextInputLayout.error = getString(R.string.email_empty_error)
            emailFilled = false
        }
        if(binding.passwordTextInputEditText.text!!.isEmpty()){
            binding.passwordTextInputLayout.error = getString(R.string.password_empty_error)
            passwordFilled = false
        }
        if(emailFilled && passwordFilled ){
            binding.emailTextInputLayout.error = null
            binding.passwordTextInputLayout.error = null
            login()
        }
    }
}
