package com.example.greetingcard

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label : String,
    val icon : ImageVector,
    val route: String,
    val badgeCount : Int,

)
