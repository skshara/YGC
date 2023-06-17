package com.jhc.ygc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class edit_info extends AppCompatActivity {

    TextInputEditText mEmail,mPassword,mGrade;
    Button update;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        update = findViewById(R.id.updatebtn);
        mEmail = findViewById(R.id.chemail);
        mPassword = findViewById(R.id.chpwd);
        mGrade = findViewById(R.id.chgrade);
        fAuth = fAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();


       update.setOnClickListener(view -> {
           if(fUser!=null) {
               String email = mEmail.getText().toString().trim();
               String password = mPassword.getText().toString().trim();
               String grade = mGrade.getText().toString().trim();
               if (!TextUtils.isEmpty(email)) {
                   // Update email
                   fUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           String fUserUid = fUser.getUid();
                           DocumentReference db = fStore.collection("user").document(fUserUid);
                           db.get().addOnSuccessListener(documentSnapshot -> {
                               if (documentSnapshot.exists()) {
                                   Map<String, Object> userData = documentSnapshot.getData();
                                   if (userData != null) {
                                       String vFname = (String) userData.get("fname");
                                       String vGrade = (String) userData.get("grade");
                                       Map<String, Object> user = new HashMap<>();
                                       user.put("grade", vGrade);
                                       user.put("fname", vFname);
                                       user.put("email", email);
                                       db.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               Toast.makeText(edit_info.this, "Successfully changed email", Toast.LENGTH_SHORT).show();
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(edit_info.this, "Failed to change email", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   } else {
                                       Toast.makeText(edit_info.this, "Successfully changed email", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(edit_info.this, "Failed to change email", Toast.LENGTH_SHORT).show();
                               }
                           });
                       }
                   });
               }
               if (!TextUtils.isEmpty(password)) {
                   // Update password
                   if (password.length() < 8) {
                       mPassword.setError("Password should be at least 8 characters long");
                   } else {
                       fUser.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Toast.makeText(edit_info.this, "Successfully updated password", Toast.LENGTH_SHORT).show();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(edit_info.this, "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               }
               if (!TextUtils.isEmpty(grade)) {
                   // Update grade
                   String fUserUid = fUser.getUid();
                   DocumentReference db = fStore.collection("user").document(fUserUid);
                   db.get().addOnSuccessListener(documentSnapshot -> {
                       if (documentSnapshot.exists()) {
                           Map<String, Object> userData = documentSnapshot.getData();
                           if (userData != null) {
                               String vFname = (String) userData.get("fname");
                               String vEmail = (String) userData.get("email");
                               Map<String, Object> user = new HashMap<>();
                               user.put("grade", grade);
                               user.put("fname", vFname);
                               user.put("email", vEmail);
                               db.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(edit_info.this, "Successfully changed grade", Toast.LENGTH_SHORT).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(edit_info.this, "Failed to change grade", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                       } else {
                           String userID = fAuth.getCurrentUser().getUid();
                           DocumentReference documentReference = fStore.collection("user").document(userID);
                           Map<String, Object> user = new HashMap<>();
                           user.put("grade", grade);
                           documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Toast.makeText(edit_info.this, "Successfully changed grade", Toast.LENGTH_SHORT).show();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(edit_info.this, "Failed to change grade", Toast.LENGTH_SHORT).show();
                               }
                           });

                       }
                   }).addOnFailureListener(e -> {
                       // Failed with error code e
                       String userID = fAuth.getCurrentUser().getUid();
                       DocumentReference documentReference = fStore.collection("user").document(userID);
                       Map<String, Object> user = new HashMap<>();
                       user.put("grade", grade);
                       documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Toast.makeText(edit_info.this, "Successfully changed grade", Toast.LENGTH_SHORT).show();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(edit_info.this, "Failed to change grade", Toast.LENGTH_SHORT).show();
                           }
                       });
                   });
               }
           }
       });
       }
}
