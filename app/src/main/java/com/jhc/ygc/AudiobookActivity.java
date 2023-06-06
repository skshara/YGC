package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AudiobookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiobook);

        loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        WebView view = findViewById(R.id.audioweb);
        if(view.canGoBack()) {
            view.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl(String url) {
        final WebView myWebView = findViewById(R.id.audioweb);
        final ProgressDialog dialog = ProgressDialog.show(AudiobookActivity.this, "", getString(R.string.loading), true);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        myWebView.setWebViewClient(new WebViewClient() {
            private static final String BLOCKED_URL = "youtube.com";

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(BLOCKED_URL)) {
                    view.loadUrl("file:///android_asset/blocked-page.html");
                    return true;
                } else {
                    // Allow other websites to load
                    return false;
                }
            }

            public void onPageFinished(WebView view, String url) {
                //Toast.makeText(myActivity.this, "Oh no!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                myWebView.loadUrl("file:///android_asset/no-internet.html");
                Toast.makeText(AudiobookActivity.this, description, Toast.LENGTH_SHORT).show();
            }

        }); //End WebViewClient
        myWebView.loadUrl(url);
    }
}