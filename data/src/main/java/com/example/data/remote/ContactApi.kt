package com.example.data.remote

import com.example.data.remote.models.ContactDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ContactApi {
    @GET("api/1.3/")
    suspend fun getContacts(
        @Query("seed") seed: String = "lydia",
        @Query("results") results: Int = 20,
        @Query("page") page: Int = 1,
    ): ContactDto
}
