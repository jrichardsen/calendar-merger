package com.gmail.jrichardsen.calendar_merger.entities

import android.graphics.Color

data class LocalCalendar(
    val id: Long,
    val name: String,
    val color: Color,
    val ownerAccount: String?,
)