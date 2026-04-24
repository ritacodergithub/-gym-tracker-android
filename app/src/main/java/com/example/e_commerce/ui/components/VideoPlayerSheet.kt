package com.example.e_commerce.ui.components

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.e_commerce.data.catalog.Exercise

// Plays a free YouTube tutorial inline via WebView.
// - If the exercise has a curated `videoId`, we embed that specific video.
// - Otherwise we load YouTube search results for the exercise name — the
//   user picks whichever tutorial they like best.
// No API key required. No Play Services dependency. 100% free.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerSheet(
    exercise: Exercise,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row {
                Icon(Icons.Default.PlayCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        exercise.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        if (exercise.videoId != null) "Curated tutorial"
                        else "Pick a free YouTube tutorial",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                YouTubeWebView(url = videoUrlFor(exercise))
            }

            TextButton(
                onClick = { openInYouTubeApp(context, exercise) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Open in YouTube app")
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

// Standalone WebView host. `AndroidView` bridges legacy Android views into
// Compose. DisposableEffect cleans up the WebView when the sheet closes,
// otherwise it can leak and keep playing audio in the background.
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun YouTubeWebView(url: String) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.stopLoading()
            webView.loadUrl("about:blank")
            webView.destroy()
        }
    }

    AndroidView(
        factory = { webView.apply { loadUrl(url) } },
        modifier = Modifier.fillMaxWidth(),
        update = { /* URL is loaded once; sheet recomposition shouldn't reload */ }
    )
}

private fun videoUrlFor(exercise: Exercise): String {
    // `autoplay=1` starts the video immediately when the WebView loads — paired
    // with `mediaPlaybackRequiresUserGesture = false` on the WebView so Android
    // doesn't block the autoplay. `playsinline=1` keeps it inside the WebView
    // (no fullscreen hijack). `modestbranding=1` strips the big YouTube logo.
    // `rel=0` hides unrelated "up next" suggestions.
    val id = exercise.videoId
    return if (id != null) {
        "https://www.youtube.com/embed/$id" +
            "?autoplay=1&playsinline=1&modestbranding=1&rel=0"
    } else {
        // listType=search turns the embed into a playlist of YouTube search
        // hits and auto-plays the first one — zero extra taps for the user.
        val query = Uri.encode("${exercise.name} proper form tutorial")
        "https://www.youtube.com/embed?listType=search&list=$query" +
            "&autoplay=1&playsinline=1&modestbranding=1&rel=0"
    }
}

private fun openInYouTubeApp(context: Context, exercise: Exercise) {
    val query = "${exercise.name} proper form tutorial"
    // YouTube app accepts ACTION_SEARCH with a "query" extra.
    val ytIntent = Intent(Intent.ACTION_SEARCH).apply {
        setPackage("com.google.android.youtube")
        putExtra("query", query)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(ytIntent)
    } catch (_: ActivityNotFoundException) {
        // Fall back to default browser if YouTube app isn't installed.
        val browser = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.youtube.com/results?search_query=${Uri.encode(query)}")
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browser)
    }
}

// Top-level launcher used by the Exercise Library "Watch video" button.
// Opens the YouTube app directly (or browser fallback) — no in-app WebView,
// no autoplay shenanigans. Tries intents in order and uses the first that
// resolves.
fun launchExerciseVideo(context: Context, exercise: Exercise) {
    val intents: List<Intent> = if (exercise.videoId != null) {
        // Curated video: open the specific watch URL.
        val id = exercise.videoId
        listOf(
            Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id")),
            Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$id"))
        )
    } else {
        // No curated video: send the user to a search.
        val query = "${exercise.name} proper form tutorial"
        listOf(
            Intent(Intent.ACTION_SEARCH).apply {
                setPackage("com.google.android.youtube")
                putExtra("query", query)
            },
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/results?search_query=${Uri.encode(query)}")
            )
        )
    }
    for (intent in intents) {
        try {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            return
        } catch (_: ActivityNotFoundException) {
            // Try the next fallback.
        }
    }
}