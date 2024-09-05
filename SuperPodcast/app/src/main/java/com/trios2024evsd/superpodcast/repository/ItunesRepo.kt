package com.trios2024evsd.superpodcast.repository

import com.trios2024evsd.superpodcast.service.ItunesService

// 1
class ItunesRepo(private val itunesService: ItunesService) {
    // 2
    suspend fun searchByTerm(term: String) = itunesService.searchPodcastByTerm(term) // 3
}
