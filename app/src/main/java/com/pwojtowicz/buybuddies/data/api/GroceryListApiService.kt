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
    @GET("api/grocery-lists")
    suspend fun getMyLists(): List<GroceryListDTO>

    @GET("api/grocery-lists/{id}")
    suspend fun getGroceryList(@Path("id") id: String): GroceryListDTO

    @POST("api/grocery-lists")
    suspend fun createGroceryList(@Body groceryListDTO: GroceryListDTO): GroceryListDTO

    @PUT("api/grocery-lists/{id}")
    suspend fun updateGroceryList(
        @Path("id") id: String,
        @Body groceryListDTO: GroceryListDTO
    ): GroceryListDTO

    @DELETE("api/grocery-lists/{id}")
    suspend fun deleteGroceryList(@Path("id") id: String)

    @POST("api/grocery-lists/{listId}/members/{memberUserId}")
    suspend fun addMember(
        @Path("listId") listId: String,
        @Path("memberUserId") memberUserId: String
    ): GroceryListDTO

    @DELETE("api/grocery-lists/{listId}/members/{memberUserId}")
    suspend fun removeMember(
        @Path("listId") listId: String,
        @Path("memberUserId") memberUserId: String
    ): GroceryListDTO

    @PUT("api/grocery-lists/{listId}/name")
    suspend fun updateListName(
        @Path("listId") listId: String,
        @Body name: String
    ): GroceryListDTO

    @GET("api/grocery-lists/{listId}/members")
    suspend fun getListMembers(
        @Path("listId") listId: String
    ): List<String>

    @POST("api/grocery-lists/members/email")
    suspend fun addMemberByEmail(
        @Body listDto: GroceryListDTO,
        @Query("memberEmail") email: String
    ): GroceryListDTO

    @DELETE("api/grocery-lists/{listId}/members")
    suspend fun removeMemberByEmail(
        @Path("listId") listId: String,
        @Query("email") email: String
    ): GroceryListDTO
}
