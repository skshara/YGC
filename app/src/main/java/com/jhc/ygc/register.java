package com.jhc.ygc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    Button backtoLogin;
    ProgressBar progressBar2;
    TextInputEditText mFullName,mEmail,mPassword,mPass2,mGrade;

    Button mRegisterBtn;

    FirebaseFirestore fstore;

    String userID;

    public FirebaseAuth fAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = "TAG";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backtoLogin = (Button)findViewById(R.id.back);
        mFullName = (TextInputEditText)findViewById(R.id.name);
        mEmail = (TextInputEditText)findViewById(R.id.email);
        mPassword = (TextInputEditText)findViewById(R.id.pwd);
        mPass2 = (TextInputEditText)findViewById(R.id.repwd);
        mGrade = (TextInputEditText)findViewById(R.id.grd);
        mRegisterBtn = (Button)findViewById(R.id.regbtn);
        progressBar2 = findViewById(R.id.progressBar2);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        backtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar2.setVisibility(View.VISIBLE);
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String password2 = mPass2.getText().toString().trim();
                final String fullname = mFullName.getText().toString().trim();
                final String grade = mGrade.getText().toString().trim();
                Integer Grade = Integer.parseInt(grade);

                if(TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if(TextUtils.isEmpty(fullname)) {
                    mEmail.setError("Name is required");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if(TextUtils.isEmpty(grade)) {
                    mGrade.setError("Type your grade");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if(TextUtils.isEmpty(password2)) {
                    mGrade.setError("Type your password again");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if(!password.equals(password2)) {
                    mPassword.setError("The passwords don't match");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if(password.length() < 8) {
                    mPassword.setError("The password length should be 8 or more than 8");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if(Grade == 0 || Grade > 13) {
                    mGrade.setError("Enter valid grade");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }



                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressBar2.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Register Successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Onfailure: Email Not Sent" + e.getMessage());
                                    progressBar2.setVisibility(View.GONE);
                                }
                            });
                            Toast.makeText(getApplicationContext(),"User created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("user").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fname", fullname);
                            user.put("email",email);
                            user.put("grade",grade);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onsuccess: user profile is created for" + userID);
                                    progressBar2.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: " + e.toString());
                                    progressBar2.setVisibility(View.GONE);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                           finish();
                        } else {
                            Toast.makeText(register.this,"Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}