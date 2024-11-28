package com.example.greetingcard.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.greetingcard.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemDetail(
    modifier: Modifier = Modifier,
    chapterId : String
) {
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
                val title = chapterId
                ImageCard(
                    painter = painter,
                    contentDescription = description,
                    title = title,
                    onClick = {}

                )

            }
        }

    }
}