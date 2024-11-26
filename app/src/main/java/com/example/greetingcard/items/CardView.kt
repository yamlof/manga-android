package com.example.greetingcard.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun CardView(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String,
    title:String,
) {
    Row(
        modifier = Modifier
            .fillMaxHeight(0.5f)
    ) {

    }
}