package com.example.greetingcard.pages

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil3.compose.AsyncImage
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.requests.RetrofitClient
import com.example.greetingcard.sources.manganelo.ChapterReader
import com.example.greetingcard.sources.manganelo.ItemDetail
import com.example.greetingcard.sources.manganelo.MangaNelo
import io.ktor.client.statement.HttpResponse


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationPage(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val mangaViewModel: MangaViewModel = hiltViewModel()

    var notificationText by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        try {
            Log.d("KtorClient", "Attempting to fetch hello message...")
            val text = RetrofitClient.apiService.getHelloMessage()
            Log.d("KtorClient", "Received: $text")
            notificationText = text
        } catch (e: Exception) {
            Log.e("KtorClient", "Error fetching message: ${e.message}", e)
            notificationText = "Error: ${e.message}"
        }
    }

            LazyColumn {
                item {
                    Text(
                        text = "",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    Row (
                        Modifier
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(start = 25.dp)
                            .height(50.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                    ){

                        val imageUrl = "https://www.mangabats.com/images/favicon-bat.webp"

                        Text(
                            text = "01"
                        )

                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Translated description of what the image contains",
                            modifier
                                .padding(horizontal = 25.dp)
                                .height(25.dp)
                                .width(25.dp)
                        )

                        Text(
                            text = "MangaBat",
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

}


