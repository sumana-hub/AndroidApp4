package com.trios2024evsd.superpodcast.model

import java.util.Date

data class Podcast(
    var feedUrl: String = "",
    var feedTitle: String = "",
    var feedDesc: String = "",
    var imageUrl: String = "",
    var lastUpdated: Date = Date(),
    var country: String = "",
    var episodes: List<Episode> = listOf()
)
