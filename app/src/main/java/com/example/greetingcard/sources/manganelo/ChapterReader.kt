package com.example.greetingcard.sources.manganelo

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.greetingcard.models.ImageManga
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.ViewModel
import com.example.greetingcard.requests.RetrofitClient

class ImageViewerViewModel : ViewModel() {

}


@Composable
fun ImageViewer(
    modifier: Modifier = Modifier,
    pagerState : PagerState,
    imgManga: List<ImageManga> // [it parameter]

) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .padding(top = 75.dp, bottom = 200.dp)
    ){ page ->

        val imgManga = imgManga[page]

        val imageLink = imgManga.imgLink

        val imageTitle = imgManga.imgTitle

        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(imageLink)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Yellow)
        ) {
            Image(
                painter = painter,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "image",
                contentScale = ContentScale.FillBounds
            )
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {},
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Horizontal")
        }
        Button(
            onClick = {},
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Vertical")
        }
    }

}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun ChapterReader(
    modifier: Modifier = Modifier,
     chapterUrl : String,
    navController: NavController
){

    val decodedChapterUrl = Uri.decode(chapterUrl)

    val fetchedItem  = remember { mutableStateOf<List<ImageManga>>(emptyList()) }

    val isLoading = remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        try {
            val fetchedItems = RetrofitClient.apiService.getChapterInfo(decodedChapterUrl)
            fetchedItem.value = fetchedItems
            isLoading.value = false
            Log.d("MangaNelo", "Fetched items: $fetchedItems")  //
        } catch (e:Exception) {
            Log.e("MangaNelo", "error fetching chapter info",e)
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (fetchedItem.value.isEmpty()) {
        Text("No images Available")
    } else {
        val pagerState = rememberPagerState(pageCount = {
            fetchedItem.value.size
        })

        val imgManga = fetchedItem.value

        ImageViewer(modifier= Modifier,pagerState = pagerState, imgManga = imgManga)
    }
}