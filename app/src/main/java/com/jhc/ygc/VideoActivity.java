package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class VideoActivity extends AppCompatActivity {
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View customView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vids);

        loadUrl("file:///android_asset/interactive-video.html");

    }

    @Override
    public void onBackPressed() {
        WebView view = findViewById(R.id.weblay);
        if(view.canGoBack()) {
            view.goBack();
        } else {
            loadUrl("file:///android_asset/interactive-video.html");
            super.onBackPressed();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl(String url) {
        final WebView myWebView = findViewById(R.id.weblay);
        fullscreenContainer = findViewById(R.id.fullscreen);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        myWebView.setWebViewClient(new WebViewClient() {

            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                if (customView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                customView = view;
                customViewCallback = callback;
                fullscreenContainer.addView(customView);
                fullscreenContainer.setVisibility(View.VISIBLE);
                myWebView.setVisibility(View.GONE);
                setFullscreen(true);
            }

            public void onHideCustomView() {
                if (customView == null) {
                    return;
                }

                fullscreenContainer.setVisibility(View.GONE);
                myWebView.setVisibility(View.VISIBLE);
                customViewCallback.onCustomViewHidden();
                customView = null;
                setFullscreen(false);
            }
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
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                myWebView.loadUrl("file:///android_asset/no-internet.html");
                Toast.makeText(VideoActivity.this, description, Toast.LENGTH_SHORT).show();
            }

        }); //End WebViewClient
        myWebView.loadUrl(url);
    }

    private void setFullscreen(boolean fullscreen) {
        Window window = getWindow();
        if (fullscreen) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            window.addFlags(Window.FEATURE_ACTION_BAR_OVERLAY);
            getActionBar().hide();
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            window.clearFlags(Window.FEATURE_ACTION_BAR_OVERLAY);
            getActionBar().show();
        }
    }
}
