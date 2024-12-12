package com.example.greetingcard.pages

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.map
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.greetingcard.database.Manga
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.database.MangaViewModelFactory
import com.example.greetingcard.sources.manganelo.ChapterReader
import com.example.greetingcard.sources.manganelo.ItemDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(modifier: Modifier = Modifier,mangaViewModel: MangaViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "library"){
        composable("library") {
            Column (
                modifier = Modifier
                    .padding(bottom = 75.dp, top = 25.dp)
            ){
                Row (modifier = modifier
                    .fillMaxWidth()
                    .background(Color.Yellow),
                ){
                    Text(
                        text = "Library",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }


                val allMangas by mangaViewModel.allMangas.observeAsState(initial = emptyList())





                val mangas = ""

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .padding(bottom = 90.dp)
                ) {
                    items(allMangas) { manga ->

                        val imageRequest = ImageRequest.Builder(LocalContext.current)
                            .data(manga.cover)
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

                        val encodedMangaUrl = Uri.encode(manga.mangaUrl)


                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            ImageCard(
                                painter = painter,
                                contentDescription = "devil",
                                title = manga.name,
                                onClick = {
                                    navController.navigate("itemDetail/${encodedMangaUrl}")
                                }
                            )
                        }




                    }
                }


            }
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



}




    /*LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(20) {
            Box(modifier = Modifier
                //.fillMaxWidth(0.5f)
                .padding(5.dp)
            ){
                val painter = painterResource(id = R.drawable.shadow)
                val description = "Sunny"
                val title = "Shadow Slave"
                ImageCard(
                    painter = painter,
                    contentDescription = description,
                    title = title,
                    onClick = {}
                )
            }
        }
    }*/


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title:String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
            //.size(300.dp,100.dp),
        shape = RoundedCornerShape(15.dp),
    ){
        Box(modifier = Modifier.height(300.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                )
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(title,style = androidx.compose.ui.text.TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                )
                )
            }
        }
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(Color(0xFF01034F)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        val painter = painterResource(id = R.drawable.shadow)
        val description = "Sunny"
        val title = "Shadow Slave"


        Box(modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(5.dp)
        ){
            ImageCard(
                painter = painter,
                contentDescription = description,
                title = title
            )

        }

    }


}*/