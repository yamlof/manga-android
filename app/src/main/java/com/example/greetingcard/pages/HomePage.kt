package com.example.greetingcard.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
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
                    title = title
                )

            }
        }



    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title:String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {

            },
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