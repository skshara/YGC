package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        webView.loadUrl("https://app.Lumi.education/run/YFjP75");
    }
}