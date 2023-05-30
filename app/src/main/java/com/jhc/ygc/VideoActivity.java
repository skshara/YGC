package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class VideoActivity extends AppCompatActivity {
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
            super.onBackPressed();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl(String url)
    {
        final ProgressDialog dialog = ProgressDialog.show(VideoActivity.this, "Loading ...",  getString(R.string.loading), true);
        final WebView myWebView = findViewById(R.id.weblay);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);


        myWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if(url.contains("youtube")) {
                    view.goBack();
                }
                if(url.startsWith("https://m.youtube.com/")|| url.startsWith("https://www.youtube.com/") || url.startsWith("https://youtube.com/")) {
                    view.goBack();
                }
                //Toast.makeText(VideoActivity.this, url, Toast.LENGTH_SHORT).show(); Debugging purposes
                if (url.endsWith(".mp4"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "video/mp4");
                    view.getContext().startActivity(intent);
                }
                else
                {
                    view.loadUrl(url);
                }

                return true;
            }

            public void onPageFinished(WebView view, String url)
            {
                //Toast.makeText(myActivity.this, "Oh no!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                Toast.makeText(VideoActivity.this, description, Toast.LENGTH_SHORT).show();
                String summary = "<html><body><strong>" + getString(R.string.lost_connection) + "</body></html>";
                myWebView.loadData(summary, "text/html", "utf-8");
            }

        }); //End WebViewClient

        myWebView.loadUrl(url);
    }
}
