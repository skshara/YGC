package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class detailupdate extends AppCompatActivity {
    Button login,upload;
    public TextInputEditText fName,fLink;
    FirebaseFirestore db;
    DocumentReference interactiveVideoUpload;
    DocumentReference eBookUpload;
    DocumentReference quizzesUpload;
    FirebaseAuth fAuth;

    @Override
    protected void onDestroy() {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatialupdate);
        login = findViewById(R.id.back);
        upload = findViewById(R.id.upload);
        fName = findViewById(R.id.Title_det);
        fLink = findViewById(R.id.link);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        Spinner spinner3 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.subject_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        login.setOnClickListener(view -> {
            fAuth.signOut();
            startActivity(new Intent(getApplicationContext(),login.class));
            finish();
        });

        upload.setOnClickListener(view -> {
                if(fName.getText()!=null&&fLink.getText()!=null) {
                    String name = fName.getText().toString().trim();
                    String link = fLink.getText().toString().trim();
                    String content = spinner.getSelectedItem().toString();
                    String grade = spinner1.getSelectedItem().toString();
                    String subject = spinner3.getSelectedItem().toString();
                    // Do something
                    switch(content) {
                        case "Interactive Videos":
                            // DO
                            interactiveVideoUpload = db.collection("interactive-video").document(grade).collection(subject).document("units");
                            Map<String, Object> interactiveVideo = new HashMap<>();
                            interactiveVideo.put(name, link);
                            interactiveVideoUpload.update(interactiveVideo).addOnSuccessListener(unused -> {
                                Toast.makeText(detailupdate.this, "Successfully added new resource", Toast.LENGTH_SHORT).show();
                                fName.setText("");
                                fLink.setText("");
                            }).addOnFailureListener(e -> Toast.makeText(detailupdate.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                            return;
                        case "Ebook":
                            // DO
                            eBookUpload = db.collection("pdf-viewer").document(grade).collection(subject).document("units");
                            Map<String, Object> eBook = new HashMap<>();
                            eBook.put(name, link);
                            eBookUpload.update(eBook).addOnSuccessListener(unused -> {
                                Toast.makeText(detailupdate.this, "Successfully added new resource", Toast.LENGTH_SHORT).show();
                                fName.setText("");
                                fLink.setText("");
                            }).addOnFailureListener(e -> Toast.makeText(detailupdate.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            return;
                        case "Quiz":
                            // DO
                            quizzesUpload = db.collection("quizzes").document(grade).collection(subject).document("units");
                            Map<String, Object> quizzes = new HashMap<>();
                            quizzes.put(name, link);
                            quizzesUpload.update(quizzes).addOnSuccessListener(unused -> {
                                Toast.makeText(detailupdate.this, "Successfully added new resource", Toast.LENGTH_SHORT).show();
                                fName.setText("");
                                fLink.setText("");
                            }).addOnFailureListener(e -> Toast.makeText(detailupdate.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            return;
                        default:
                            // default
                            Toast.makeText(detailupdate.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(detailupdate.this, "Fill all information", Toast.LENGTH_SHORT).show();
                }
            });
        
        
    }

}
