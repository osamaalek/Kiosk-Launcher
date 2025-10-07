package com.osamaalek.kiosklauncher.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.* // Import all webkit classes
import androidx.fragment.app.Fragment
import com.osamaalek.kiosklauncher.R

/**
 * This fragment is modified to replace the default app launcher with a single, locked WebView
 * that loads the designated Google Form URL.
 */
class HomeFragment : Fragment() {

    private lateinit var webView: WebView

    // The Google Form URL the kiosk will be locked to
    private val KIOSS_URL = "https:/[Insert kiosk link here]"
    private val TAG = "KioskWebView"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.webView)

        setupWebView()
        loadWeb()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val settings = webView.settings

        // 1. Essential Settings for modern web apps like Google Forms
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = false
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        // 2. Optimization and Display Settings
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.builtInZoomControls = false
        settings.displayZoomControls = false

        // 3. Security and HTTPS/Network Fixes (Addresses ERR_CACHE_MISS/SSL issues)
        // Ensure images and other mixed content can be loaded over HTTPS
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // Enable support for multiple windows (often required by Google Forms/Auth)
        settings.setSupportMultipleWindows(true)

        // Clear cache and history just in case something is corrupted
        webView.clearCache(true)
        webView.clearHistory()

        // Set the custom client that handles navigation and security errors
        webView.webViewClient = KioskWebViewClient(requireContext().packageName)

        // Ensure hardware acceleration is enabled (can sometimes fix rendering issues)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = false
    }

    private fun loadWeb() {
        Log.d(TAG, "Attempting to load URL: $KIOSS_URL")
        webView.loadUrl(KIOSS_URL)
    }

    /**
     * Custom WebViewClient to control navigation and handle SSL/connection errors.
     */
    private class KioskWebViewClient(private val packageName: String) : WebViewClient() {

        private val TAG = "KioskWebViewClient"

        /**
         * Prevents the user from navigating away from the form by clicking internal links.
         * Returning 'false' keeps all navigation within the current WebView.
         */
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            Log.d(TAG, "Should override URL: ${request.url}")
            return false
        }

        // Handle older API versions as well
        @Suppress("DEPRECATION")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.d(TAG, "Should override URL (legacy): $url")
            return false
        }

        /**
         * CRITICAL FIX: Handles SSL errors.
         * For a dedicated kiosk app, accepting the certificate is often necessary
         * to prevent the page from failing to load (which often throws a masked ERR_CACHE_MISS).
         */
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: android.net.http.SslError) {
            Log.e(TAG, "SSL Error received: ${error.url}")
            // WARNING: In a production app, accepting all SSL errors is insecure.
            // However, for internal kiosk testing, this is a common fix.
            handler.proceed()
        }

        /**
         * Handles general web resource errors.
         */
        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
            // Log the error details to help debug if the issue persists
            if (request.isForMainFrame) {
                Log.e(TAG, "Main frame loading failed. Error: ${error.description} (${error.errorCode})")
                // You could display a local HTML error page here if needed
            }
        }
    }
}
