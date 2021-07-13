package com.runwithme.runwithme.network

interface TokenStore {

    fun getJwt() : String
    fun setJwt(token:String)
}