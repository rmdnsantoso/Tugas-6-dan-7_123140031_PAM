package pam.tugas6dan7.romadhon.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import pam.tugas6dan7.romadhon.database.NewsDatabase

class NewsRepository(
    private val client: HttpClient,
    private val database: NewsDatabase
) {
    private val apiKey = "0f818bc2fc3545628facf14e0b198c87"
    private val baseUrl = "https://newsapi.org/v2"

    private val queries = database.newsDatabaseQueries

    suspend fun getTopHeadlines(): Result<List<Article>> = try {
        val response: NewsResponse = client.get("$baseUrl/top-headlines") {
            url {
                parameters.append("country", "us")
                parameters.append("category", "technology")
                parameters.append("apiKey", apiKey)
            }
        }.body()
        Result.success(response.articles)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun saveBookmark(article: Article) {
        queries.insertBookmark(
            url = article.url,
            title = article.title,
            description = article.description,
            urlToImage = article.urlToImage,
            publishedAt = article.publishedAt
        )
    }

    fun removeBookmark(url: String) {
        queries.deleteBookmark(url)
    }

    fun isBookmarked(url: String): Boolean {
        return queries.isBookmarked(url).executeAsOne() > 0
    }

    fun getAllBookmarks(): List<Article> {
        return queries.selectAllBookmarks().executeAsList().map {
            Article(
                title = it.title,
                description = it.description,
                urlToImage = it.urlToImage,
                url = it.url,
                publishedAt = it.publishedAt
            )
        }
    }
}