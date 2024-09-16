package com.trios2024evsd.superpodcast.service

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import com.trios2024evsd.superpodcast.BuildConfig
import com.trios2024evsd.superpodcast.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RssFeedService private constructor() {
    suspend fun getFeed(xmlFileURL: String): RssFeedResponse? {
        // 1
        val service: FeedService
// 2
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
// 3
        val client = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            client.addInterceptor(interceptor)
        }
        client.build()
// 4
        val retrofit = Retrofit.Builder()
            .baseUrl("${xmlFileURL.split("?")[0]}/")
            .build()
        service = retrofit.create(FeedService::class.java)
// 5
        try {
            val result = service.getFeed(xmlFileURL)
            if (result.code() >= 400) {
                println("server error, ${result.code()}, ${result.errorBody()}")
                return null
            } else {
                var rssFeedResponse : RssFeedResponse? = null
                // return success result
                val dbFactory = DocumentBuilderFactory.newInstance()
                val dBuilder = dbFactory.newDocumentBuilder()
                withContext(Dispatchers.IO) {
                    val doc = dBuilder.parse(result.body()?.byteStream())
                    val rss = RssFeedResponse(episodes = mutableListOf())
                    domToRssFeedResponse(doc, rss)
                    println(rss)
                    rssFeedResponse = rss

                }

                return rssFeedResponse
            }
        } catch (t: Throwable) {
            println("error, ${t.localizedMessage}")
        }
        return null

    }
    companion object {
        val instance: RssFeedService by lazy {
            RssFeedService()
        }
    }

    private fun domToRssFeedResponse(node: Node, rssFeedResponse: RssFeedResponse) {
        // 1
        if (node.nodeType == Node.ELEMENT_NODE) {
            // 2
            val nodeName = node.nodeName
            val parentName = node.parentNode.nodeName

            // 1
            val grandParentName = node.parentNode.parentNode?.nodeName ?: ""
// 2
            if (parentName == "item" && grandParentName == "channel") {
                // 3
                val currentItem = rssFeedResponse.episodes?.last()
                if (currentItem != null) {
                    // 4
                    when (nodeName) {
                        "title" -> currentItem.title = node.textContent
                        "description" -> currentItem.description = node.textContent
                        "itunes:duration" -> currentItem.duration = node.textContent
                        "guid" -> currentItem.guid = node.textContent
                        "pubDate" -> currentItem.pubDate = node.textContent
                        "link" -> currentItem.link = node.textContent
                        "enclosure" -> {
                            currentItem.url = node.attributes.getNamedItem("url")
                                .textContent
                            currentItem.type = node.attributes.getNamedItem("type")
                                .textContent
                        }
                    }
                }
            }


            // 3
            if (parentName == "channel") {
                // 4
                when (nodeName) {
                    "title" -> rssFeedResponse.title = node.textContent
                    "description" -> rssFeedResponse.description = node.textContent
                    "itunes:summary" -> rssFeedResponse.summary = node.textContent
                    "item" -> rssFeedResponse.episodes?.
                    add(RssFeedResponse.EpisodeResponse())
                    "pubDate" -> rssFeedResponse.lastUpdated =
                        DateUtils.xmlDateToDate(node.textContent)
                }
            }
        }
        // 5
        val nodeList = node.childNodes
        for (i in 0 until nodeList.length) {
            val childNode = nodeList.item(i)
            // 6
            domToRssFeedResponse(childNode, rssFeedResponse)
        }
    }

}

interface FeedService {
    @Headers(
        "Content-Type: application/xml; charset=utf-8",
        "Accept: application/xml"
    )
    @GET
    suspend fun getFeed(@Url xmlFileURL: String): Response<ResponseBody>
}
