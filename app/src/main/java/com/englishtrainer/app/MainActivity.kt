package com.englishtrainer.app

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.webkit.WebViewAssetLoader
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var webView: WebView
    private lateinit var root: View
    private var tts: TextToSpeech? = null
    private var ttsReady = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()                       // behaves the same on Android 10 … 16
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        root = webView
        setContentView(webView)
        webView.setBackgroundColor(Color.parseColor("#FFF4F8"))
        window.decorView.setBackgroundColor(Color.parseColor("#FFF4F8"))

        // Keep content clear of status bar, cutout, gesture bar and keyboard.
        ViewCompat.setOnApplyWindowInsetsListener(webView) { v, insets ->
            val bars: Insets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val ime: Insets = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(bars.left, bars.top, bars.right, maxOf(bars.bottom, ime.bottom))
            WindowInsetsCompat.CONSUMED
        }

        initTts()

        val loader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true             // progress is saved in localStorage
            allowFileAccess = false
            allowContentAccess = false
            mediaPlaybackRequiresUserGesture = false
            cacheMode = WebSettings.LOAD_NO_CACHE
            textZoom = 100                       // ignore system font scale breaking the layout
        }
        webView.overScrollMode = View.OVER_SCROLL_NEVER
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView, request: WebResourceRequest
            ): WebResourceResponse? = loader.shouldInterceptRequest(request.url)
        }
        webView.addJavascriptInterface(Bridge(), "Android")
        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html")

        // Back button: let the app navigate first, exit only from the home screen.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                webView.evaluateJavascript("window.__back && window.__back()") { handled ->
                    if (handled != "true") {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })
    }

    private fun initTts() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val r = tts?.setLanguage(Locale("lt", "LT"))
                ttsReady = r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }
    }

    inner class Bridge {
        @JavascriptInterface
        fun speak(text: String, rate: Float) {
            runOnUiThread {
                if (!ttsReady) return@runOnUiThread
                tts?.setSpeechRate(rate)
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "et")
            }
        }

        @JavascriptInterface
        fun setTheme(bgColor: String, dark: Boolean) {
            runOnUiThread {
                val c = Color.parseColor(bgColor)
                webView.setBackgroundColor(c)
                window.decorView.setBackgroundColor(c)
                val ctl = WindowCompat.getInsetsController(window, window.decorView)
                ctl.isAppearanceLightStatusBars = !dark
                ctl.isAppearanceLightNavigationBars = !dark
            }
        }
    }

    override fun onPause() { tts?.stop(); super.onPause() }

    override fun onDestroy() {
        tts?.shutdown()
        webView.destroy()
        super.onDestroy()
    }
}
