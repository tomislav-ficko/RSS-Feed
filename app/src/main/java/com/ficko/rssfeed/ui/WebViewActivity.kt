package com.ficko.rssfeed.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ficko.rssfeed.databinding.WebViewActivityBinding
import com.ficko.rssfeed.domain.RssFeedItem
import com.ficko.rssfeed.ui.base.BaseActivity
import com.ficko.rssfeed.ui.common.AppBar

class WebViewActivity : BaseActivity(),
    AppBar.AppBarListener {

    companion object {
        private const val ITEM_KEY = "rss_feed_item"
        fun buildIntent(context: Context, item: RssFeedItem) =
            Intent(context, WebViewActivity::class.java)
                .apply { putExtra(ITEM_KEY, item) }
    }

    private val binding by lazy { WebViewActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActivity() // TODO item data inside intent is being lost
    }

    override fun backButtonClicked() {
        onBackPressed()
    }

    override fun addButtonClicked() {}

    private fun setUpActivity() {
        binding.appBar.setListener(this)
        intent.getSerializableExtra(ITEM_KEY)?.let {
            val item = it as RssFeedItem
            preventWebViewLinksRedirectingUserOutsideOfApp()
            loadWebView(item.url)
            binding.appBar.updateView(title = item.name)
        }
    }

    private fun preventWebViewLinksRedirectingUserOutsideOfApp() {
        binding.webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(url) }
                return true
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView(url: String) {
        binding.webView.apply {
            loadUrl(url)
            settings.javaScriptEnabled = true
        }
    }
}