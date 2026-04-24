package com.example.e_commerce.data.repository

import com.example.e_commerce.BuildConfig
import com.example.e_commerce.data.ai.Content
import com.example.e_commerce.data.ai.GeminiClient
import com.example.e_commerce.data.ai.GenerateRequest
import com.example.e_commerce.data.ai.GenerationConfig
import com.example.e_commerce.data.ai.Part
import com.example.e_commerce.data.local.Goal

// Wraps the Gemini API with fitness-specific prompts. Returns a Result
// so callers can distinguish "no API key configured" from network errors.
class AiCoachRepository {

    sealed class Outcome {
        data class Success(val text: String) : Outcome()
        data object NoApiKey : Outcome()
        data class Error(val message: String) : Outcome()
    }

    private val apiKey: String = BuildConfig.GEMINI_API_KEY

    suspend fun generateDailyPlan(
        goal: Goal,
        daysPerWeek: Int = 4,
        experience: String = "beginner"
    ): Outcome = call(
        """
        You are a concise, no-nonsense strength-and-conditioning coach.
        Build a single gym workout for today. The user's goal is
        "${goal.label}", they train ${daysPerWeek} days/week, they are a
        ${experience}.

        Respond in this exact format:
          • Warm-up: 3 bullet points
          • Main session: 4–6 exercises, each as
              "Name — sets × reps @ intensity (cue)"
          • Cool-down: 2 bullet points
          • One short motivational line.

        Keep it under 180 words. No intro, no disclaimer.
        """.trimIndent()
    )

    suspend fun motivationalNudge(
        streak: Int,
        name: String
    ): Outcome = call(
        """
        Write a 2-sentence motivational message for someone named $name
        on a ${streak}-day gym streak. Energetic but grounded; no emoji
        overload, no "queen/king" clichés. Under 40 words.
        """.trimIndent()
    )

    suspend fun tipsForExercise(exerciseName: String): Outcome = call(
        """
        Give 4 form tips for "$exerciseName". Prioritize safety and the
        single most common mistake. Format as bullet points, one sentence
        each. No intro.
        """.trimIndent()
    )

    private suspend fun call(prompt: String): Outcome {
        if (apiKey.isBlank()) return Outcome.NoApiKey
        return try {
            val response = GeminiClient.api.generate(
                apiKey = apiKey,
                body = GenerateRequest(
                    contents = listOf(Content(parts = listOf(Part(prompt)))),
                    generationConfig = GenerationConfig()
                )
            )
            val text = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.joinToString("") { it.text }
                .orEmpty()
                .trim()
            if (text.isBlank()) Outcome.Error("Empty response from Gemini")
            else Outcome.Success(text)
        } catch (http: retrofit2.HttpException) {
            val message = when (http.code()) {
                404 -> "Model not found (404). The model name in GeminiApi.kt may not be available for your account/region — try 'gemini-1.5-flash' or 'gemini-2.0-flash'."
                429 -> "Free-tier rate limit. This usually clears in 60 seconds — if it keeps happening, you may have hit today's daily quota. Try again tomorrow or add billing in Google AI Studio."
                400 -> "Bad request (400). Often means the prompt or API version is off."
                401, 403 -> "Key rejected (${http.code()}). Check that your Gemini key is active in AI Studio."
                503 -> "Gemini is overloaded right now. Try again in a moment."
                else -> "HTTP ${http.code()} from Gemini: ${http.message()}"
            }
            Outcome.Error(message)
        } catch (t: Throwable) {
            Outcome.Error(t.message ?: "Network error")
        }
    }
}