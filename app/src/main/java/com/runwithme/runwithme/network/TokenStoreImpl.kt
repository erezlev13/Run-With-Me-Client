package com.runwithme.runwithme.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStoreImpl @Inject constructor() : TokenStore {
    var token: String = ""
    override fun getJwt(): String {
        return token
    }

    override fun setJwt(token: String) {
        this.token = token
    }
}