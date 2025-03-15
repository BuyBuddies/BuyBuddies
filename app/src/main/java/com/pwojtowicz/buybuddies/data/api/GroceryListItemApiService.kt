package com.pwojtowicz.buybuddies.data.api

import com.pwojtowicz.buybuddies.data.dto.GroceryListItemDTO
import com.pwojtowicz.buybuddies.data.dto.UserDTO
import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroceryListItemApiService {
    @POST("api/grocery_list_items")
    suspend fun createListItem(
        @Body listItemDTO: GroceryListItemDTO
    ): GroceryListItemDTO

    @POST("api/grocery_list_items/create_or_update")
    suspend fun createOrUpdateGroceryListItem(@Body listItemDTO: GroceryListItemDTO): GroceryListItemDTO

    @GET("api/grocery_list_items/{id}")
    suspend fun getListItem(
        @Path("id") id: Long
    ): GroceryListItemDTO

    @GET("api/grocery_list_items/user")
    suspend fun getListItemByUser(
    ): List<GroceryListItemDTO>

    @GET("api/grocery_list_items/list/{listId}")
    suspend fun getItemsByList(
        @Path("listId") listId: Long
    ): List<GroceryListItemDTO>

    @GET("api/grocery_list_items/list/{listId}/status/{status}")
    suspend fun getItemsByListAndStatus(
        @Path("listId") listId: Long,
        @Path("status") status: PurchaseStatus
    ): List<GroceryListItemDTO>

    @PUT("api/grocery_list_items/{id}")
    suspend fun updateListItem(
        @Path("id") id: Long,
        @Body listItemDTO: GroceryListItemDTO
    ): GroceryListItemDTO

    @PATCH("api/grocery_list_items/{id}/status")
    suspend fun updateItemStatus(
        @Path("id") id: Long,
        @Body status: PurchaseStatus
    ): GroceryListItemDTO

    @HTTP(method = "DELETE", path = "api/grocery_list_items/delete", hasBody = true)
    suspend fun deleteGroceryItem(@Body itemDTO: GroceryListItemDTO): Response<Unit>
}