package com.laioffer.netflix.network

import com.laioffer.netflix.datamodel.HomeResponse
import com.laioffer.netflix.datamodel.VideoDetailRequest
import com.laioffer.netflix.datamodel.VideoDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Defines the backend endpoints the app can call in this class.
interface NetworkApi {
    @GET("/home")
    fun getHome(): Call<HomeResponse>

    @POST("/videoDetail")
    fun getDetail(@Body request: VideoDetailRequest): Call<VideoDetailResponse>
}