package com.example.project

data class SearchData (
    val title_results: List<title_results>
)

data class title_results(
    val id: Int,
    val name: String,
    val year: Int,
    val imdb_id: String,
    val type: String
)

/* Example Response From API

{
    "title_results": [
    {
        "id": 1114888,
        "name": "Ed Wood",
        "type": "movie",
        "year": 1994,
        "imdb_id": "tt0109707",
        "tmdb_id": 522,
        "tmdb_type": "movie"
    }
    ],
    "people_results": [
    {
        "id": 710125611,
        "name": "Ed Wood",
        "main_profession": "cinematographer",
        "imdb_id": "nm7903892",
        "tmdb_id": 2901757
    }
    ]
}
*/