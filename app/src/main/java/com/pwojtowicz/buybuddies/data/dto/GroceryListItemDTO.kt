package com.pwojtowicz.buybuddies.data.dto

import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus

data class GroceryListItemDTO(
    val id: String = "",
    val groceryListId: String,
    val groceryItemName: String,
    val quantity: Double,
    val unit: String,
    val status: PurchaseStatus = PurchaseStatus.PENDING,
    override val updatedAt: String?,
    override val createdAt: String?
) : BaseDto
