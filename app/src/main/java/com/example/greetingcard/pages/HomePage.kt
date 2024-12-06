package com.example.greetingcard.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greetingcard.R
import com.example.greetingcard.sources.manganelo.ChapterReader


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "library"){
        composable("library") {
            Column (
                modifier = Modifier
                    .padding(bottom = 75.dp)
            ){



                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier
                        .height( height = 280.dp)
                        .padding(top = 25.dp)
                        //.width(400.dp),
                        .fillMaxWidth(),
                    shape = RectangleShape
                ) {
                    val painter = painterResource(id = R.drawable.shadow)
                    val description = "Sunny"
                    val title = "Shadow Slave"
                    Row (
                        modifier = modifier
                            .height(600.dp)
                            .fillMaxWidth()
                            .padding(5.dp),

                        //fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ){

                        ImageCard(
                            painter = painter,
                            modifier = Modifier
                                .fillMaxHeight()
                                .height(25.dp)
                                .size(height = 20.dp, width = 120.dp)
                                .padding(top = 20.dp, bottom = 20.dp),
                            contentDescription = description,
                            contentScale = ContentScale.FillBounds,
                            title = title,
                            onClick = {}
                        )



                        Column (
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxHeight()
                                //.fillMaxWidth()
                                //.width(100.dp)
                                .padding(start = 0.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Shadow Slave",
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                            Text(
                                text = "Guiltythree",
                                textAlign = TextAlign.End,
                                color = Color.Black
                            )
                            Text(
                                text = "Ongoing",
                                textAlign = TextAlign.End,
                                color = Color.Black
                            )
                            ElevatedButton(
                                onClick = {},
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
                    items(5) { index ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(Color(0xFFA4C2D7))
                        ) {
                            Text(text = "Item: $index")

                        }

                    }



                    // Add another single item
                    item {
                        Text(text = "Last item",
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("chapter")

                                }
                        )

                    }
                }
            }
        }

        composable("chapter") {
            ChapterReader(chapterUrl = "https://chapmanganelo.com/manga-qq130784/chapter-17", navController = navController)
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