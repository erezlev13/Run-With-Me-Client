package com.runwithme.runwithme.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivitySignupBinding
import com.runwithme.runwithme.model.network.SignupRequest
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.SessionManager
import com.runwithme.runwithme.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignupBinding
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var sessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        sessionManager = SessionManager(this)
        onTextChangeAllFields()



        binding.signupButton.setOnClickListener {
            onClickSignupButton()
        }
        binding.loginQuestionTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun onClickSignupButton(){
        var firstNameFilled : Boolean = true
        var lastNameFilled : Boolean = true
        var emailFilled : Boolean = true
        var passwordFilled : Boolean = true

        if(binding.firstNameTextInputEditText.text!!.isEmpty()){
            binding.firstNameTextInputLayout.error = getString(R.string.first_name_empty_error)
            emailFilled = false
        }
        if(binding.lastNameTextInputEditText.text!!.isEmpty()){
            binding.lastNameTextInputLayout.error = getString(R.string.last_name_empty_error)
            emailFilled = false
        }
        if(binding.emailTextInputEditText.text!!.isEmpty()){
            binding.emailTextInputLayout.error = getString(R.string.email_empty_error)
            emailFilled = false
        }
        if(binding.passwordTextInputEditText.text!!.isEmpty()){
            binding.passwordTextInputLayout.error = getString(R.string.password_empty_error)
            passwordFilled = false
        }
        if(firstNameFilled && lastNameFilled &&emailFilled && passwordFilled){
            binding.firstNameTextInputLayout.error = null
            binding.lastNameTextInputLayout.error = null
            binding.emailTextInputLayout.error = null
            binding.passwordTextInputLayout.error = null
            signup()
        }
    }
    private fun signup(){
        val firstName= binding.firstNameTextInputEditText.text.toString()
        val lastName = binding.lastNameTextInputEditText.text.toString()
        val email= binding.emailTextInputEditText.text.toString()
        val password = binding.passwordTextInputEditText.text.toString()

        loginViewModel.signup(SignupRequest(firstName,lastName,email,password))
        binding.signUpProgressBar.show()
        loginViewModel.loginResponse.observeOnce(this, { response ->
            when(response){
                is NetworkResult.Success -> {
                    binding.signUpProgressBar.hide()
                    if(response.data?.user != null) {
                        sessionManager.saveAuthToken(response.data?.token)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                is NetworkResult.Error -> {
                    binding.signUpProgressBar.hide()
                    binding.emailTextInputLayout.error = getString(R.string.email_exist)
                }
                else -> {
                    binding.signUpProgressBar.hide()
                    Snackbar.make(binding.root, "Oops... something went wrong", Snackbar.LENGTH_LONG)
                }
            }
        })
    }
    private fun onTextChangeAllFields(){
        binding.firstNameTextInputEditText.doOnTextChanged { text, start, before, count ->
            if(binding.firstNameTextInputLayout.error == getString(R.string.first_name_empty_error)){
                binding.firstNameTextInputLayout.error = null
            }
        }
        binding.lastNameTextInputEditText.doOnTextChanged { text, start, before, count ->
            if(binding.lastNameTextInputLayout.error == getString(R.string.last_name_empty_error)){
                binding.lastNameTextInputLayout.error = null
            }
        }
        binding.emailTextInputEditText.doOnTextChanged { text, start, before, count ->
            if(binding.emailTextInputLayout.error == getString(R.string.email_empty_error) ||
               binding.emailTextInputLayout.error == getString(R.string.email_exist)){
                binding.emailTextInputLayout.error = null
            }
        }
        binding.passwordTextInputEditText.doOnTextChanged { text, start, before, count ->
            if(binding.passwordTextInputLayout.error == getString(R.string.password_empty_error)){
                binding.passwordTextInputLayout.error = null
            }
        }
    }
}