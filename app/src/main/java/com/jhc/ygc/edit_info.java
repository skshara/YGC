package com.jhc.ygc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

    TextInputEditText mEmail,mPassword,mGrade,mFname;
    Button update;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    String grade,email,fname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        update = findViewById(R.id.updatebtn);
        mEmail = findViewById(R.id.chemail);
        mPassword = findViewById(R.id.chpwd);
        mGrade = findViewById(R.id.chgrade);
        mFname = findViewById(R.id.Usernamech);
        fAuth = fAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        grade = intent.getStringExtra("grade");
        email = intent.getStringExtra("email");
        fname = intent.getStringExtra("fname");

        if(!grade.isEmpty()) {
            mGrade.setHint(grade);
        } else {
            mGrade.setHint("Give the new grade");
        }
        if(!email.isEmpty()) {
            mEmail.setText(email);
        } else {
            mEmail.setText("Give the new email");
        }
        if(!fname.isEmpty()) {
            mFname.setHint(fname);
        } else {
            mFname.setText("Give the new name");
        }



       update.setOnClickListener(view -> {
           final ProgressDialog progress = ProgressDialog.show(getApplicationContext(),"","Loading...",true);
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
                                       Map<String, Object> user = new HashMap<>();
                                       user.put("email", email);
                                       db.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               progress.dismiss();
                                               Toast.makeText(edit_info.this, "Successfully changed email", Toast.LENGTH_SHORT).show();
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(edit_info.this, "Failed to change email", Toast.LENGTH_SHORT).show();
                                               progress.dismiss();
                                           }
                                       });
                                   } else {
                                       Toast.makeText(edit_info.this, "Successfully changed email", Toast.LENGTH_SHORT).show();
                                       progress.dismiss();
                                   }
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(edit_info.this, "Failed to change email", Toast.LENGTH_SHORT).show();
                                   progress.dismiss();
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
                               progress.dismiss();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(edit_info.this, "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                               progress.dismiss();
                           }
                       });
                   }
               }
               if (!TextUtils.isEmpty(grade)) {
                   // Update grade
                   Integer gradeInt = Integer.parseInt(grade);
                   if(gradeInt>5&&gradeInt<12) {
                   String fUserUid = fUser.getUid();
                   DocumentReference db = fStore.collection("user").document(fUserUid);
                   db.get().addOnSuccessListener(documentSnapshot -> {
                       if (documentSnapshot.exists()) {
                           Map<String, Object> userData = documentSnapshot.getData();
                           if (userData != null) {
                               Map<String, Object> user = new HashMap<>();
                               user.put("grade", grade);
                               db.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(edit_info.this, "Successfully changed grade", Toast.LENGTH_SHORT).show();
                                       progress.dismiss();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(edit_info.this, "Failed to change grade", Toast.LENGTH_SHORT).show();
                                       progress.dismiss();
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
                                   progress.dismiss();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(edit_info.this, "Failed to change grade", Toast.LENGTH_SHORT).show();
                                   progress.dismiss();
                               }
                           });

                       }
                   }).addOnFailureListener(e -> {
                       progress.dismiss();
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
               } else {
                       Toast.makeText(this, "Choose valid grade", Toast.LENGTH_SHORT).show();
                   }
           }}
       });
       }
}
