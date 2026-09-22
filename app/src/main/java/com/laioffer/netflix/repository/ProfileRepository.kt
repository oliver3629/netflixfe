package com.laioffer.netflix.repository

import com.laioffer.netflix.datamodel.ProfileResponse
import com.laioffer.netflix.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// Data layer object that fetches profile and recently viewed data.
@Singleton
class ProfileRepository @Inject constructor(
    private val networkApi: NetworkApi
) {
    suspend fun getProfile(): ProfileResponse = withContext(Dispatchers.IO) {
        val response = networkApi.getProfile().execute()

        if (response.isSuccessful) {
            response.body() ?: error("Empty profile response")
        } else {
            error("Profile request failed: ${response.code()}")
        }
    }
}