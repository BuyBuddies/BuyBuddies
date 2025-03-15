package com.pwojtowicz.buybuddies.data.api

import com.pwojtowicz.buybuddies.data.dto.GroceryListDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GroceryListApiService {
    // Get all lists (owned + member)
    @GET("api/grocery-lists")
    suspend fun getMyLists(): List<GroceryListDTO>

    @GET("api/grocery-lists/{id}")
    suspend fun getGroceryList(@Path("id") id: Long): GroceryListDTO

    @POST("api/grocery-lists")
    suspend fun createGroceryList(@Body groceryListDTO: GroceryListDTO): GroceryListDTO

    @PUT("api/grocery-lists/{id}")
    suspend fun updateGroceryList(
        @Path("id") id: Long,
        @Body groceryListDTO: GroceryListDTO
    ): GroceryListDTO

    @DELETE("api/grocery-lists/{id}")
    suspend fun deleteGroceryList(@Path("id") id: Long)

    @POST("api/grocery-lists/{listId}/members/{memberFirebaseUid}")
    suspend fun addMember(
        @Path("listId") listId: Long,
        @Path("memberFirebaseUid") memberFirebaseUid: String
    ): GroceryListDTO

    @DELETE("api/grocery-lists/{listId}/members/{memberFirebaseUid}")
    suspend fun removeMember(
        @Path("listId") listId: Long,
        @Path("memberFirebaseUid") memberFirebaseUid: String
    ): GroceryListDTO

    @PUT("api/grocery-lists/{listId}/name")
    suspend fun updateListName(
        @Path("listId") listId: Long,
        @Body name: String
    ): GroceryListDTO

    @GET("api/grocery-lists/{listId}/members")
    suspend fun getListMembers(
        @Path("listId") listId: Long
    ): List<String>

    @POST("api/grocery-lists/members/email")
    suspend fun addMemberByEmail(
        @Body listDto: GroceryListDTO,
        @Query("memberEmail") email: String
    ): GroceryListDTO


    @DELETE("api/grocery-lists/{listId}/members")
    suspend fun removeMemberByEmail(
        @Path("listId") listId: Long,
        @Query("email") email: String
    ): GroceryListDTO
}