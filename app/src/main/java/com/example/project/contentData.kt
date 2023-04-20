package com.example.project

data class titleData(
    val titles: List<title>
)

data class title(
    val id: Int,
    val title: String,
    val year: Int,
    val imdb_id: String,
    val type: String
)
//{
//    "titles": [
//    {
//        "id": 3183276,
//        "title": "Beef",
//        "year": 2023,
//        "imdb_id": "tt14403178",
//        "tmdb_id": 154385,
//        "tmdb_type": "tv",
//        "type": "tv_series"
//    },
