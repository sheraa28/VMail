package com.example.parmindersingh.vmail;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ManLoginActivity extends AppCompatActivity{



    // UI references.
    private AutoCompleteTextView mEmailView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText emailEditText;
    private EditText passEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_login);
        TextView textView = (TextView) findViewById(R.id.textView5);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.emailEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passEditText = (EditText) findViewById(R.id.passEditText);
        firebaseAuth = FirebaseAuth.getInstance();


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

            //mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);

            Intent intent = getIntent();
            if (intent.hasExtra("username")) {
                emailEditText.setText(intent.getStringExtra("username"));
            }
            if (intent.hasExtra("password")) {
                passEditText.setText(intent.getStringExtra("password"));
            }

        String userEmail = emailEditText.getText().toString().trim();
        String userPassword =passEditText.getText().toString().trim();

        if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)){
            attemptLogin();

        }
    }
    private void config(){

        String userEmail = emailEditText.getText().toString().trim();
        String userPassword =passEditText.getText().toString().trim();


        SharedPreferences sp=getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("Unm",userEmail);
        Ed.putString("Psw",userPassword);
        Ed.commit();


    }
    public void attemptLogin() {
        config();
        String userEmail = emailEditText.getText().toString().trim();
        String userPassword =passEditText.getText().toString().trim();
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(ManLoginActivity.this, "Please wait...", "Proccessing...", true);

        (firebaseAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passEditText.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(ManLoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ManLoginActivity.this, DashActivity.class);
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);
                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(ManLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }






    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}

