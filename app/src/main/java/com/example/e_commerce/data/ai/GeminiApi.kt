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
        // gemini-1.5-flash is universally available at the v1beta endpoint
        // and has its own free-tier rate-limit pool (15 RPM, 1500 RPD).
        // Separate quota from gemini-2.0-flash — good fallback when 2.0 is
        // throttled. Swap to "gemini-2.0-flash" or "gemini-1.5-pro" here
        // if your account/region supports them.
        @Path("model") model: String = "gemini-1.5-flash",
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