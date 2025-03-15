package com.pwojtowicz.buybuddies.data.api

import com.pwojtowicz.buybuddies.data.dto.HomeDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HomeApiService {
    @GET("api/homes/owner/{ownerId}/")
    suspend fun getHomesByOwner(
        @Path("ownerId") ownerId: String
    ): List<HomeDTO>

    @GET("api/homes/member/{userId}")
    suspend fun getHomesByMember(
        @Path("userId") userId: String
    ): List<HomeDTO>
}
