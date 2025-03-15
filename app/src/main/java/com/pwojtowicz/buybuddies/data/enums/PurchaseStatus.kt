package com.pwojtowicz.buybuddies.data.enums

enum class PurchaseStatus {
    PENDING,
    PURCHASED;

    fun toBoolean(): Boolean {
        return when (this) {
            PENDING -> false
            PURCHASED -> true
        }
    }

    companion object {
        fun fromBoolean(value: Boolean): PurchaseStatus {
            return if (value) PURCHASED else PENDING
        }
    }
}