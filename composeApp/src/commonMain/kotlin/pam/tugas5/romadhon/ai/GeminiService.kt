package pam.tugas5.romadhon.ai

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.request.parameter
import pam.tugas5.romadhon.ApiConfig

class GeminiService(private val client: HttpClient) {
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val model = "gemini-2.5-flash"

    suspend fun extractTextFromImage(imageBase64: String): Result<String> = runCatching {
        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = SystemPrompts.SCAN_TO_NOTE),
                        Part(inlineData = InlineData(mimeType = "image/jpeg", data = imageBase64))
                    )
                )
            ),
            generationConfig = GenerationConfig(temperature = 0.4)
        )

        val response: HttpResponse = client.post("$baseUrl/models/$model:generateContent") {
            contentType(ContentType.Application.Json)
            parameter("key", ApiConfig.geminiApiKey)
            setBody(request)
        }

        if (response.status.value in 200..299) {
            val geminiResponse: GeminiResponse = response.body()
            geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Gagal membaca teks"
        } else {
            val errorText = response.bodyAsText()
            throw Exception("Ditolak Google: $errorText")
        }
    }
}