package com.pwojtowicz.buybuddies.utility

import java.time.Instant

fun String?.toEpochMillis(): Long =
    if (this != null) Instant.parse(this).toEpochMilli()
    else System.currentTimeMillis()

fun Long.toIsoString(): String =
    Instant.ofEpochMilli(this).toString()
