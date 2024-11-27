package com.example.greetingcard.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greetingcard.requests.RetrofitClient

@Composable
fun NotificationPage(modifier: Modifier = Modifier) {

    var notificationText by remember { mutableStateOf("Loading...") }

    // Use LaunchedEffect to perform network call
    LaunchedEffect(Unit) {
        try {
            val text = RetrofitClient.apiService.getHelloMessage()
            notificationText = text
        } catch (e: Exception) {
            notificationText = "Error fetching message"
        }
    }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list"){
        composable("list") {
            LazyColumn {
                item {
                    Text(
                        text = notificationText,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                item {
                    Text(
                        text = "Manganelo",
                        modifier = Modifier
                            .clickable {
                                navController.navigate("detail/Manganelo")
                            }
                        /*fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White*/
                    )
                }
            }
        }
        composable("detail/{source}") {
            MangaNelo()
        }

    }

    Column(
        modifier = modifier.fillMaxWidth()
            .background(Color(0xFF01034F)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = notificationText,
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

    }
}


