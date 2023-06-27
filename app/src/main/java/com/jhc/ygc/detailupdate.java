package com.jhc.ygc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class detailupdate extends AppCompatActivity {

    public boolean isGradeChecked = false;
    public boolean isContentChecked = false;
    public boolean isSubjectChecked = false;
    Button login,upload;
    public TextInputEditText fName,fLink;
    FirebaseFirestore db;
    DocumentReference interactiveVideoUpload;
    DocumentReference eBookUpload;
    DocumentReference quizzesUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatialupdate);
        login = (Button) findViewById(R.id.back);
        upload = (Button) findViewById(R.id.upload);
        fName = (TextInputEditText) findViewById(R.id.Title_det);
        fLink = (TextInputEditText) findViewById(R.id.link);
        db = FirebaseFirestore.getInstance();

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });

        Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });

        Spinner spinner3 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.subject_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                interactiveVideoUpload.update(interactiveVideo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(detailupdate.this, "Successfully added new resource", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(detailupdate.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return;
                            case "Ebook":
                                // DO
                                eBookUpload = db.collection("pdf-viewer").document(grade).collection(subject).document("units");
                                Map<String, Object> eBook = new HashMap<>();
                                eBook.put(name, link);
                                eBookUpload.update(eBook).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(detailupdate.this, "Successfully added new resource", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(detailupdate.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            case "Quiz":
                                // DO
                                quizzesUpload = db.collection("quizzes").document(grade).collection(subject).document("units");
                                Map<String, Object> quizzes = new HashMap<>();
                                quizzes.put(name, link);
                                quizzesUpload.update(quizzes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(detailupdate.this, "Successfully added new resource", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(detailupdate.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            default:
                                // default
                                Toast.makeText(detailupdate.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(detailupdate.this, "Fill all information", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        
        
    }

}
