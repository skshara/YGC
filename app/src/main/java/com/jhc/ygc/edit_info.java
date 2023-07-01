package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class edit_info extends AppCompatActivity {

    TextInputEditText mEmail,mPassword,mGrade,mFname;
    Button update,backBtn;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    String fGrade,fEmail,fFname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        update = findViewById(R.id.updatebtn);
        backBtn = findViewById(R.id.imageButton2);
        mEmail = findViewById(R.id.chemail);
        mPassword = findViewById(R.id.chpwd);
        mGrade = findViewById(R.id.chgrade);
        mFname = findViewById(R.id.Usernamech);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        fGrade = intent.getStringExtra("grade");
        fEmail = intent.getStringExtra("email");
        fFname = intent.getStringExtra("fname");

        if(!fGrade.isEmpty()) {
            mGrade.setHint(fGrade);
        }
        if(!fEmail.isEmpty()) {
            mEmail.setText(fEmail);
        }
        if(!fFname.isEmpty()) {
            mFname.setHint(fFname);
        }

        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });



       update.setOnClickListener(view -> {
           String email = Objects.requireNonNull(mEmail.getText()).toString().trim();
           String password = Objects.requireNonNull(mPassword.getText()).toString().trim();
           String grade = Objects.requireNonNull(mGrade.getText()).toString().trim();
           String fname = Objects.requireNonNull(mFname.getText()).toString().trim();
           if (!TextUtils.isEmpty(email) && !email.equals(fEmail)) {
               // Update email
               fUser.updateEmail(email).addOnSuccessListener(unused -> {
                   String fUserUid = fUser.getUid();
                   DocumentReference db = fStore.collection("user").document(fUserUid);
                   db.get().addOnSuccessListener(documentSnapshot -> {
                       if (documentSnapshot.exists()) {
                           Map<String, Object> userData = documentSnapshot.getData();
                           if (userData != null) {
                               Map<String, Object> user = new HashMap<>();
                               user.put("email", email);
                               db.update(user).addOnSuccessListener(unused1 -> Toast.makeText(edit_info.this, "Successfully changed email", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(edit_info.this, "Failed to change email", Toast.LENGTH_SHORT).show());
                           } else {
                               Toast.makeText(edit_info.this, "Successfully changed email", Toast.LENGTH_SHORT).show();
                           }
                       } else {
                           Toast.makeText(edit_info.this, "Failed to change name", Toast.LENGTH_SHORT).show();
                       }
                   }).addOnFailureListener(e -> Toast.makeText(edit_info.this, "Failed to change email", Toast.LENGTH_SHORT).show());
               });
           }
           if (!TextUtils.isEmpty(password)) {
               // Update password
               if (password.length() < 8) {
                   mPassword.setError("Password should be at least 8 characters long");
               } else {
                   fUser.updatePassword(password).addOnSuccessListener(unused -> Toast.makeText(edit_info.this, "Successfully updated password", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(edit_info.this, "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show());
               }
           }
           if (!TextUtils.isEmpty(grade) && !grade.equals(fGrade)) {
               // Update grade
               int gradeInt = Integer.parseInt(grade);
               if (gradeInt > 5 && gradeInt < 12) {
                   String fUserUid = fUser.getUid();
                   DocumentReference db = fStore.collection("user").document(fUserUid);
                   db.get().addOnSuccessListener(documentSnapshot -> {
                       if (documentSnapshot.exists()) {
                           Map<String, Object> userData = documentSnapshot.getData();
                           if (userData != null) {
                               Map<String, Object> user = new HashMap<>();
                               user.put("grade", grade);
                               db.update(user).addOnSuccessListener(unused -> Toast.makeText(edit_info.this, "Successfully changed grade", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(edit_info.this, "Failed to change grade", Toast.LENGTH_SHORT).show());
                           }
                       } else {
                           DocumentReference documentReference = fStore.collection("user").document(fUserUid);
                           Map<String, Object> user = new HashMap<>();
                           user.put("grade", grade);
                           documentReference.set(user).addOnSuccessListener(unused -> Toast.makeText(edit_info.this, "Successfully changed grade", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(edit_info.this, "Failed to change grade", Toast.LENGTH_SHORT).show());

                       }
                   }).addOnFailureListener(e -> {
                       // Failed with error code e
                       DocumentReference documentReference = fStore.collection("user").document(fUserUid);
                       Map<String, Object> user = new HashMap<>();
                       user.put("grade", grade);
                       documentReference.set(user).addOnSuccessListener(unused -> Toast.makeText(edit_info.this, "Successfully changed grade", Toast.LENGTH_SHORT).show()).addOnFailureListener(e1 -> Toast.makeText(edit_info.this, "Failed to change grade", Toast.LENGTH_SHORT).show());
                   });
               } else {
                   Toast.makeText(this, "Choose valid grade", Toast.LENGTH_SHORT).show();
               }
           }
           if (!TextUtils.isEmpty(fname) && !fname.equals(fFname)) {
               // Update grade;
               String fUserUid = fUser.getUid();
               DocumentReference db = fStore.collection("user").document(fUserUid);
               db.get().addOnSuccessListener(documentSnapshot -> {
                   if (documentSnapshot.exists()) {
                       Map<String, Object> userData = documentSnapshot.getData();
                       if (userData != null) {
                           Map<String, Object> user = new HashMap<>();
                           user.put("fname", fname);
                           db.update(user).addOnSuccessListener(unused -> Toast.makeText(edit_info.this, "Successfully changed name", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(edit_info.this, "Failed to change name", Toast.LENGTH_SHORT).show());
                       }
                   } else {
                       DocumentReference documentReference = fStore.collection("user").document(fUserUid);
                       Map<String, Object> user = new HashMap<>();
                       user.put("fname", fname);
                       documentReference.set(user).addOnSuccessListener(unused -> Toast.makeText(edit_info.this, "Successfully changed name", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(edit_info.this, "Failed to change name", Toast.LENGTH_SHORT).show());
// end
                   }
               }).addOnFailureListener(e -> {
                   // Failed with error code e
                   DocumentReference documentReference = fStore.collection("user").document(fUserUid);
                   Map<String, Object> user = new HashMap<>();
                   user.put("fname", fname);
                   documentReference.set(user).addOnSuccessListener(unused -> Toast.makeText(edit_info.this, "Successfully changed name", Toast.LENGTH_SHORT).show()).addOnFailureListener(e12 -> Toast.makeText(edit_info.this, "Failed to change name", Toast.LENGTH_SHORT).show());
               });
           }
       });
       }
}
