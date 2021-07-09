package com.runwithme.runwithme.view.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.runwithme.runwithme.databinding.ActivityLoginBinding
import com.runwithme.runwithme.model.LoginRequest
import com.runwithme.runwithme.model.LoginResponse
import com.runwithme.runwithme.network.ApiClient
import com.runwithme.runwithme.network.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        binding.buttonLogin.setOnClickListener {
            login()
        }

    }

    fun login(){
        val email= binding.email.text.toString()
        val password = binding.password.text.toString()

        apiClient.getApiService(this).login(LoginRequest(email,password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (response.isSuccessful) {
                        if(loginResponse?.user != null) {
                            sessionManager.saveAuthToken(loginResponse.token)
                            changeScreen()
                        }
                    } else {
                        binding.errorTextView.visibility = View.VISIBLE
                    }
                }
            })
    }

    fun changeScreen(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
