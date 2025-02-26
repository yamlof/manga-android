package com.example.greetingcard.models

import com.example.greetingcard.database.ChapterRoom

data class MangaInfo(
    val title : String,
    val cover : String,
    val author : String,
    val status : String,
    val chapters : List<ChapterRoom>
)