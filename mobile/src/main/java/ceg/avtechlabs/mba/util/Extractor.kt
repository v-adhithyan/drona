package ceg.avtechlabs.mba.util

import com.chimbori.crux.articles.Article
import com.chimbori.crux.articles.ArticleExtractor
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by Adhithyan V on 18-11-2017.
 */

class Extractor(link: String) {
    var title = ""
    var description = ""
    var image = ""
    var url = ""
    init {
        url = link
    }
    private fun getHtml(): String {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(url)
                    .build()
            val response = client.newCall(request).execute()
            return response.body()!!.string()
    }

    fun extract(): Article {
        val article = ArticleExtractor.with(url, getHtml())
                .extractContent()
                .extractMetadata()
                .article()
        return article
    }
}