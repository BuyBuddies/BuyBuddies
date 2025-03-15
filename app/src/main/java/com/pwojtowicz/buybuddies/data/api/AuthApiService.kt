package com.pwojtowicz.buybuddies.data.api

import com.pwojtowicz.buybuddies.data.dto.UserDTO
import com.pwojtowicz.buybuddies.data.entity.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {
    @GET("api/users")
    suspend fun getUserData(): UserDTO

    @POST("api/users/create_update")
    suspend fun createOrUpdateUser(@Body userDTO: UserDTO): UserDTO

    @POST("api/users/create")
    suspend fun createUser(@Body userDTO: UserDTO): UserDTO

    @PUT("api/users/update")
    suspend fun updateUserData(@Body userDTO: UserDTO): UserDTO

    @DELETE("api/users/delete")
    suspend fun deleteUserData(@Body userDTO: UserDTO): UserDTO
}