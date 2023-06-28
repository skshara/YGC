package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoActivity extends AppCompatActivity {
    Button submit;
    FirebaseFirestore db;
    DocumentReference documentRef;
    ListView listView;
    List<String> linksList;
    Map<String, Object> data;
    LinearLayout linearLayout;
    List<String> namesLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vids);
        submit = findViewById(R.id.submit);
        listView = findViewById(R.id.unitsListView);
        linearLayout = findViewById(R.id.linearFun);

        //loadUrl("file:///android_asset/interactive-video.html");

        Spinner spinner3 = findViewById(R.id.subjectSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.subject_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        Spinner spinner1 = findViewById(R.id.gradeSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        submit.setOnClickListener(view -> {
            String grade = spinner1.getSelectedItem().toString();
            String subject = spinner3.getSelectedItem().toString();
            db = FirebaseFirestore.getInstance();
            documentRef = db.collection("interactive-video").document(grade).collection(subject).document("units"); // Replace with your collection name
            documentRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        data = document.getData();
                        linksList = new ArrayList<>();
                        namesLink = new ArrayList<>();

                        // Iterate over the fields and values
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String field = entry.getKey();
                            String value = entry.getValue().toString();

                            // Do something with the field and value
                            linksList.add(field);
                            namesLink.add(value);
                        }
                        // Create an ArrayAdapter to populate the ListView with the links
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(VideoActivity.this, android.R.layout.simple_list_item_1, linksList);

                        // Set the adapter to the ListView
                        listView.setAdapter(adapter);
                    } else {
                        Log.d("TAG", "Document not found");
                    }
                } else {
                    Log.d("TAG", "Error getting document: ", task.getException());
                }
            });
        });
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedLink = namesLink.get(i);
            WebView webView = findViewById(R.id.weblay);
            webView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);
            loadUrl(selectedLink);
        });
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
        myWebView.setVisibility(View.VISIBLE);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true); // Enable access to file URLs
        myWebView.setWebChromeClient(new WebChromeClient());
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
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                myWebView.loadUrl("file:///android_asset/no-internet.html");
                Toast.makeText(VideoActivity.this, description, Toast.LENGTH_SHORT).show();
            }

        }); //End WebViewClient
        myWebView.loadUrl(url);
    }
}
