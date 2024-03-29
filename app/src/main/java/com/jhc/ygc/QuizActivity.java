package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    Button submit;
    FirebaseFirestore db,db2;
    DocumentReference documentRef,documentRef2;
    ListView listView;
    List<String> linksList;
    Map<String, Object> data;
    LinearLayout linearLayout;
    List<String> namesLink;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String selectedLink = "";

    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed() {
        WebView view = findViewById(R.id.web_quiz);
        linearLayout = findViewById(R.id.linearFun2);
        if(view.canGoBack()) {
            view.goBack();
        } else {
            if(view.getVisibility()== View.VISIBLE) {
                view.setVisibility(View.INVISIBLE);
                view.clearView();
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                super.onBackPressed();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        submit = findViewById(R.id.submit2);
        listView = findViewById(R.id.unitsListView2);
        linearLayout = findViewById(R.id.linearFun2);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        //loadUrl("file:///android_asset/quiz.html");
        Spinner spinner3 = findViewById(R.id.subjectSpinner2);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.subject_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        Spinner spinner1 = findViewById(R.id.gradeSpinner2);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        db2 = FirebaseFirestore.getInstance();
        documentRef2 = db2.collection("user").document(fUser.getUid());
        documentRef2.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> userData = document.getData();
                    if(userData!=null) {
                        String grade = (String) userData.get("grade");
                        if(grade!=null) {
                            int gradeInt = Integer.parseInt(grade);
                            int position = gradeInt - 6;
                            spinner1.setSelection(position);
                        } else {
                            Toast.makeText(this, "Grade retrieval failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Grade retrieval failed", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(view -> {
            String grade = spinner1.getSelectedItem().toString();
            String subject = spinner3.getSelectedItem().toString();
            db = FirebaseFirestore.getInstance();
            documentRef = db.collection("quizzes").document(grade).collection(subject).document("units"); // Replace with your collection name
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
                            Log.d("TAG",value);
                        }
                        if(linksList.isEmpty()) {
                            linksList.add("No resource available in current selection");
                            namesLink.add("eduTrix://nothing");
                        }
                        // Create an ArrayAdapter to populate the ListView with the links
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(QuizActivity.this, android.R.layout.simple_list_item_1, linksList);

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
            selectedLink = namesLink.get(i);
            if(!selectedLink.equals("eduTrix://nothing")&&!selectedLink.equals("")) {
                if(selectedLink.contains("forms")) {
                    selectedLink = selectedLink + "?hl=en";
                }
                WebView webView = findViewById(R.id.web_quiz);
                webView.setVisibility(View.VISIBLE);
                int new1 = webView.getVisibility();
                String new2 = Integer.toString(new1);
                Log.d("TAG",new2);
                linearLayout.setVisibility(View.INVISIBLE);
                loadUrl(selectedLink);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl(String url) {
        Log.d("TAG", "loadUrl: " + url);
        final WebView myWebView = findViewById(R.id.web_quiz);
        final ProgressDialog dialog = ProgressDialog.show(QuizActivity.this, "", getString(R.string.loading), true);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        myWebView.clearCache(true);
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
                Log.d("TAG", "onPageFinished: " + url);
                dialog.dismiss();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("TAG", "onReceivedError: " + description);
                view.loadUrl("file:///android_asset/no-internet.html");
                Toast.makeText(QuizActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        }); // End WebViewClient
        myWebView.loadUrl(url);
    }
}