package com.pwojtowicz.buybuddies.data.enums

enum class MeasurementUnit {
    PIECE,
    KILOGRAM,
    GRAM,
    LITER,
    MILLILITER,
    CUP,
    TABLESPOON,
    TEASPOON,
    POUND,
    OUNCE,
    PACKAGE;

    fun toShortString(): String {
        return when (this) {
            PIECE -> "pc"
            KILOGRAM -> "kg"
            GRAM -> "g"
            LITER -> "L"
            MILLILITER -> "ml"
            CUP -> "cup"
            TABLESPOON -> "tbsp"
            TEASPOON -> "tsp"
            POUND -> "lb"
            OUNCE -> "oz"
            PACKAGE -> "pkg"
        }
    }
   companion object {
       fun fromString(value: String): MeasurementUnit? {
           return try {
               valueOf(value.uppercase())
           } catch (e: IllegalArgumentException) {
               null
           }
       }
   }
}