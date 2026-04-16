package pam.tugas6dan7.romadhon.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NewsRepository(private val client: HttpClient) {
    private val apiKey = "0f818bc2fc3545628facf14e0b198c87"
    private val baseUrl = "https://newsapi.org/v2"

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
}