package com.example.greetingcard.models

data class MangaInfo(
    val title : String,
    val cover : String,
    val author : String,
    val status : String,
    val chapters : List<Chapter>
)