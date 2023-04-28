package com.example.project

data class contentData(
    val titles: List<content_title>
)

data class content_title(

    var sourceID: Int,
    var id: Int,
    var title: String,
    var year: Int,
    var imdb_id: String,
    var type: String
)
// new line
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
