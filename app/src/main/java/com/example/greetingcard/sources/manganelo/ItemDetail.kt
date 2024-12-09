package com.example.greetingcard.sources.manganelo

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.greetingcard.database.Manga
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.models.Chapter
import com.example.greetingcard.models.LatestManga
import com.example.greetingcard.pages.ImageCard
import com.example.greetingcard.requests.RetrofitClient


@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemDetail(
    modifier: Modifier = Modifier,
    mangaJson:String,
    navController: NavController,
    viewModel: MangaViewModel
) {
    //val mangaInfo = manga.mangaUrl
    val itemsList = remember { mutableStateOf<List<LatestManga>>(emptyList()) }

    Column (
        modifier = Modifier
            .padding(bottom = 75.dp)
    ){
        val fetcheditem  = remember { mutableStateOf<String?>(null) }
        val fetchedTitle = remember { mutableStateOf<String?>(null) }
        val fetchedStatus = remember { mutableStateOf<String?>(null) }
        val fetchedAuthor = remember { mutableStateOf<String?>(null) }
        val fetchedChapters = remember {mutableStateOf<List<Chapter>>(emptyList()) }


        LaunchedEffect(Unit) {
            val fetchedItems = RetrofitClient.apiService.getMangaInfo(mangaJson)
            Log.d("MangaNelo", "Fetched items: $itemsList")  //
            fetcheditem.value = fetchedItems.cover
            fetchedTitle.value = fetchedItems.title
            fetchedAuthor.value = fetchedItems.author
            fetchedStatus.value = fetchedItems.status
            fetchedChapters.value = fetchedItems.chapters

        }
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .height( height = 280.dp)
                .fillMaxWidth()
                .padding(top = 25.dp),
            shape = RectangleShape
        ) {


            val description = "Sunny"
            Row (
                modifier = modifier
                    .height(600.dp)
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){



                val cover = fetcheditem.value

                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(cover)
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
                ImageCard(
                    painter = painter,
                    modifier = Modifier
                        .fillMaxHeight()
                        .height(25.dp)
                        .size(height = 20.dp, width = 120.dp)
                        .padding(top = 20.dp, bottom = 20.dp),
                    contentDescription = description,
                    contentScale = ContentScale.FillBounds,
                    title = fetchedTitle.value ?: "Loading title...",
                    onClick = {}
                )

                Column (
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxHeight()
                        .padding(start = 0.dp),

                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = fetchedTitle.value ?: "Loading title...",
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                    Text(
                        text = fetchedAuthor.value ?: "Loading title...",
                        textAlign = TextAlign.End,
                        color = Color.Black
                    )
                    Text(
                        text = fetchedStatus.value ?: "Loading title...",
                        textAlign = TextAlign.End,
                        color = Color.Black
                    )

                    val newManga = Manga(
                        name =fetcheditem.value ?: "Not Found Yet",
                        cover = cover ?: "Not Found Yet",
                        mangaUrl = mangaJson
                    )

                    ElevatedButton(
                        onClick = {
                            viewModel.addManga(newManga)
                        },
                        modifier = Modifier
                            .padding(top = 25.dp)
                    ) {
                        Text(
                            "Add to Library",
                            textAlign = TextAlign.End)
                    }
                }

            }

        }
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
        ){

            // Add 5 items

            items(fetchedChapters.value) { item ->

                val chapterName = item.chapterTitle
                val chapterLink = item.chapterLink

                val encodedChapterUrl = Uri.encode(chapterLink)


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable {
                            navController.navigate("chapter/$encodedChapterUrl")
                        },
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(Color(0xFFA4C2D7)),
                ) {
                    Text(
                        text = chapterName ?: "Loading Title",
                        color = Color.White
                    )

                }

            }

            // Add another single item
            item {
                Text(text = "Last item")
            }
        }
    }
}