package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class eBookActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);

        webView = (WebView) findViewById(R.id.web_pdf);
        //final ProgressDialog dialog = ProgressDialog.show(eBookActivity.this, "", getString(R.string.loading), true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
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
                //dialog.dismiss();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_asset/no-internet.html");
                Toast.makeText(eBookActivity.this, description, Toast.LENGTH_SHORT).show();
            }

        }); //End WebViewClient
        webView.loadUrl("file:///android_asset/pdf_viewer.html");
        //webView.loadData("<html><body><iframe src=\"https://docs.google.com/viewer?url=" + Uri.encode(url) + "&embedded=true\" width=\"100%\" height=\"100%\" style=\"border: none;\"></iframe></body></html>", "text/html", "UTF-8");
    }

    @Override
    public void onBackPressed() {
        WebView view = findViewById(R.id.web_pdf);
        if(view.canGoBack()) {
            view.goBack();
        } else {
            super.onBackPressed();
        }
    }
}