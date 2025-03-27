package com.example.greetingcard.models



data class Mangadex(
    val result: String,
    val response: String,
    val data : MangadexManga,
    val limit : Int,
    val offset: Int,
    val total : Int,
)

data class MangadexManga(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: List<Relationships>
)

data class Attributes(
    val title: Title
)

data class Title(
    val property1:String,
    val property2:String
)

data class Relationships (
    val id:String,
    val type:String,
    val related: String,
    val attributes : String
)