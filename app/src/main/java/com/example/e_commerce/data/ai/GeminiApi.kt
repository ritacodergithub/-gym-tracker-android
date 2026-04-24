package com.example.e_commerce.data.ai

import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Minimal slice of the Gemini REST API — the `:generateContent` endpoint
// on a text model. The API key goes in the query string. Model name is a
// path param so we can swap (gemini-2.0-flash is the free, fast one).
//
// Docs: https://ai.google.dev/gemini-api/docs/text-generation
interface GeminiApi {

    @POST("v1beta/models/{model}:generateContent")
    suspend fun generate(
        // `gemini-flash-latest` is a Google-maintained alias that always
        // points to the current stable Flash model. Safer than hardcoding
        // a specific version since minor releases get deprecated every
        // ~6 months. Concrete alternatives if this ever 404s:
        //   "gemini-2.5-flash", "gemini-2.0-flash", "gemini-2.5-pro"
        // Discover what your key can use:
        //   GET https://generativelanguage.googleapis.com/v1beta/models?key=<K>
        @Path("model") model: String = "gemini-flash-latest",
        @Query("key") apiKey: String,
        @Body body: GenerateRequest
    ): GenerateResponse
}

// --- Request ---

@JsonClass(generateAdapter = true)
data class GenerateRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val role: String = "user",
    val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(val text: String)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val temperature: Float = 0.7f,
    val maxOutputTokens: Int = 800
)

// --- Response ---

@JsonClass(generateAdapter = true)
data class GenerateResponse(val candidates: List<Candidate>?)

@JsonClass(generateAdapter = true)
data class Candidate(val content: Content?)