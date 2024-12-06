package com.example.greetingcard.sources.manganelo

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.greetingcard.items.Manga
import com.example.greetingcard.models.LatestManga
import com.example.greetingcard.pages.ImageCard
import com.example.greetingcard.requests.RetrofitClient
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("NewApi")
@Composable
fun MangaNeloPopular(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val viewModel :MainSearchModel = viewModel()

    val searchQuery by remember { viewModel.searchQuery }
    val isLoading by remember { viewModel.isLoading }
    val errorMessage by remember { viewModel.errorMessage }

    val itemsList = remember { mutableStateOf<List<LatestManga>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val fetchedItems = RetrofitClient.apiService.getPopular()
            itemsList.value = fetchedItems
            Log.d("MangaNelo", "Fetched items: $itemsList")  //

        } catch (e:Exception) {
            Log.d("MangaNelo", "Error Fetched items: $e.message")  //
        }
    }

    Column {
        Row (
            modifier = Modifier
                .background(color = Color.Yellow)
                .fillMaxWidth()
                .padding(25.dp),
            Arrangement.Center
        ){
            Column(
                Modifier.fillMaxWidth()
            ) {

                StyledTextField(viewModel)

                Row {
                    Button(
                        onClick = {
                            navController.navigate("popular")
                        }) {
                        Text("Popular Manga")
                    }
                    Button(
                        onClick = {
                            navController.navigate("detail/Manganelo")
                        }) {
                        Text("Latest Manga")
                    }
                }

            }


        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .padding(bottom = 90.dp)
        ) {

            items(itemsList.value) { item ->

                val imageUrl = item.cover
                val title = item.title
                val description = "sunny"
                val mangaUrl = item.manga_url

                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0")
                    .addHeader("Accept", "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5")
                    .addHeader("Accept-Language", "en-GB,en;q=0.5")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Referer", "https://chapmanganelo.com/")
                    .addHeader("Sec-Fetch-Dest", "image")
                    .addHeader("Sec-Fetch-Mode", "no-cors")
                    .addHeader("Sec-Fetch-Site", "cross-site")
                    .addHeader("Priority", "u=5, i")
                    .addHeader("Pragma", "no-cache")
                    .addHeader("Cache-Control", "no-cache")
                    .build()

                val painter = rememberImagePainter(imageRequest)
                val encodedMangaUrl = URLEncoder.encode(mangaUrl, StandardCharsets.UTF_8.toString())

                //val navController = rememberNavController()

                Box(modifier = Modifier
                    //.fillMaxWidth(0.5f)
                    .padding(5.dp)

                ){

                    val manga = Manga(title, cover = imageUrl, mangaUrl = mangaUrl)
                    val gson = Gson()
                    val mangaJson = gson.toJson(manga)

                    ImageCard(
                        painter = painter,
                        contentDescription = description,
                        title = title,
                        contentScale = ContentScale.FillBounds,
                        onClick = {
                            navController.navigate("itemDetail/${encodedMangaUrl}")
                        }

                    )
                }
            }
        }
    }
}
