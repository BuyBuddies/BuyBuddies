package com.pwojtowicz.buybuddies.data.dto

import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus


data class GroceryListItemDTO(
    val id: Long = 0,
    val groceryListId: Long,
    val groceryItemName: String,
    val quantity: Double,
    val unit: String,
    val status: PurchaseStatus = PurchaseStatus.PENDING,
    override val updatedAt: Long?,
    override val createdAt: String?
) : BaseDto