package com.example.greetingcard.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.requests.RetrofitClient
import com.example.greetingcard.sources.manganelo.ChapterReader
import com.example.greetingcard.sources.manganelo.ItemDetail
import com.example.greetingcard.sources.manganelo.MangaNelo


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationPage(modifier: Modifier = Modifier) {

    val mangaViewModel: MangaViewModel = viewModel()

    var notificationText by remember { mutableStateOf("Loading...") }

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

                                notificationText = ""

                                navController.navigate("detail/Manganelo")
                            }
                        /*fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White*/
                    )
                }
            }
        }
        composable("detail/{source}") { navBackStackEntry ->
            val source = navBackStackEntry.arguments?.getString("source")
            MangaNelo(navController =navController)
        }

        composable("itemDetail/{manga_url}"){ navBackStackEntry ->
            val itemName = navBackStackEntry.arguments?.getString("manga_url")
            if (itemName != null) {
                ItemDetail(mangaJson = itemName, navController = navController, viewModel = mangaViewModel)
            }
        }

        composable("chapter/{chapterUrl}") { navBackStackEntry ->
            val chapterName = navBackStackEntry.arguments?.getString("chapterUrl")
            if (chapterName != null) {
                ChapterReader(chapterUrl = chapterName, navController = navController)
            }
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


