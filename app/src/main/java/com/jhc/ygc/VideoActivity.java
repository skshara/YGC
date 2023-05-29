package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jhc.ygc.R;

import java.util.HashMap;
import java.util.Map;

public class VideoActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vids);

        webView = findViewById(R.id.weblay);
        webView.setWebViewClient(new CustomWebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webView.loadUrl("https://app.lumi.education/run/zZDuba");
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String requestUrl = request.getUrl().toString();
                if (requestUrl.startsWith("https://lumieducation.onfastspring.com")) {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Access-Control-Allow-Origin", "*");
                    headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                    headers.put("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
                    return new WebResourceResponse(null, null, 200, "OK", headers, null);
                }
            }
            return super.shouldInterceptRequest(view, request);
        }
    }
}
