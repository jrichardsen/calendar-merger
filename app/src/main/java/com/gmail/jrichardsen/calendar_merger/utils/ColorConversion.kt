package com.gmail.jrichardsen.calendar_merger.utils

import androidx.compose.ui.graphics.toArgb

fun android.graphics.Color.toUiColor(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color(this.toArgb())
}

fun androidx.compose.ui.graphics.Color.toColor(): android.graphics.Color {
    return android.graphics.Color.valueOf(this.toArgb())
}