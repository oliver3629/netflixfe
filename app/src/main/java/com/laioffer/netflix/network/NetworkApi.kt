package com.laioffer.netflix.network

import com.laioffer.netflix.datamodel.HomeResponse
import retrofit2.Call
import retrofit2.http.GET

// Defines the backend endpoints the app can call in this class.
interface NetworkApi {
    @GET("/home")
    fun getHome(): Call<HomeResponse>
}