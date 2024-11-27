package com.example.greetingcard.pages

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.greetingcard.R
import com.example.greetingcard.requests.RetrofitClient

data class ApiResponse(
    val imageUrl:String,
    val title:String
)


@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MangaNelo(modifier: Modifier = Modifier) {

    val itemsList = remember { mutableStateOf<List<Map<String,String>>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val fetchedItems = RetrofitClient.apiService.getLatest()
            itemsList.value = fetchedItems
            Log.d("MangaNelo", "Fetched items: $itemsList")  //

        } catch (e:Exception) {
            Log.d("MangaNelo", "Error Fetched items: $e.message")  //
        }


    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(itemsList.value) { item ->

            val imageUrl = item["cover"] ?: ""
            val title = item["title"] ?: "unkwnown"
            val description = "sunny"

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

            Box(modifier = Modifier
                //.fillMaxWidth(0.5f)
                .padding(5.dp)

            ){

                ImageCard(
                    painter = painter,
                    contentDescription = description,
                    title = title,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}