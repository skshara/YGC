package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoActivity extends AppCompatActivity {
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View customView;
    Button submit;
    FirebaseFirestore db;
    DocumentReference documentRef;
    ListView listView;
    List<String> linksList;
    Map<String, Object> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vids);
        submit = findViewById(R.id.submit);
        listView = findViewById(R.id.unitsListView);

        //loadUrl("file:///android_asset/interactive-video.html");

        Spinner spinner3 = findViewById(R.id.subjectSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.subject_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        Spinner spinner1 = findViewById(R.id.gradeSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                                    // Iterate over the fields and values
                                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                                        String field = entry.getKey();
                                        Object value = entry.getValue();

                                        // Do something with the field and value
                                        linksList.add(field);
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

            }
        });

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedKey = linksList.get(i);
                String selectedLink = (String) data.get(selectedKey);
                WebView myWebView = findViewById(R.id.weblay);
                myWebView.setVisibility(View.VISIBLE);
                loadUrl(selectedLink);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
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
