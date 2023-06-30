package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText mEmail;
    EditText mPassword;
    Button mLogin, signUp, rstPass;

    FloatingActionButton gleBtn;
    ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    public final static int RC_SIGN_IN = 123;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = findViewById(R.id.username);
        signUp = findViewById(R.id.signup);
        rstPass = findViewById(R.id.rstpwd);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);
        gleBtn = findViewById(R.id.glebtn);

        fAuth = FirebaseAuth.getInstance();

        createRequest();

        gleBtn.setOnClickListener(view -> signIn());

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        rstPass.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), passReset.class));
            finish();
        });

        signUp.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), register.class));
            finish();
        });

        mLogin.setOnClickListener(view -> {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();


            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required");
                return;
            }


            progressBar.setVisibility(View.VISIBLE);

            if (email.equals("admin@edutrix.lk")) {
                fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(login.this, "Admin Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), detailupdate.class));
                    finish();
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(login.this, "Login Failure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        if (task.getException() != null) {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @SuppressWarnings("deprecation")
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        progressBar.setVisibility(View.VISIBLE);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        progressBar.setVisibility(View.GONE);
                        FirebaseUser fUser = fAuth.getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        String fUserUid;
                        if (fUser != null) {
                            fUserUid = fUser.getUid();
                            DocumentReference userDocRef = db.collection("user").document(fUserUid);
                            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Toast.makeText(login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(this, "New User must sign up before using google sign-in", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> Toast.makeText(this, "New User must sign up before using google sign-in", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(login.this, "Sorry login failed.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }


                    // ...
                }).addOnFailureListener(e -> Toast.makeText(login.this, "Google sign-in error", Toast.LENGTH_SHORT).show());
    }
}