package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class vids extends AppCompatActivity {
private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vids);
        webView = (WebView) findViewById(R.id.weblay);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://app.lumi.education/run/zZDuba");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Landscape
        handler.proceed(); // Ignore SSL certificate errors
    }

}

