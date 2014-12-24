package com.example.kvarnsen.footballtracker;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by joshuapancho on 24/12/14.
 */

public class GifWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

}
