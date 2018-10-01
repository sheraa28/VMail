package com.example.parmindersingh.vmail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText userEmailId;
    private ProgressBar progressBar;
    private EditText userPassword;
    private EditText confirmPassword;
    private Button Register;
    private TextView alreadyUser;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = (EditText) findViewById(R.id.fullName);
        userEmailId = (EditText) findViewById(R.id.email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        userPassword = (EditText) findViewById(R.id.Password);
        confirmPassword = (EditText) findViewById(R.id.cnfPassword);
        Register = (Button) findViewById(R.id.Register);
        alreadyUser = (TextView) findViewById(R.id.already_user);


        Intent intent = getIntent();
        if (intent.hasExtra("fullname")) {
            fullName.setText(intent.getStringExtra("fullname"));
        }
        if (intent.hasExtra("email")) {
            userEmailId.setText(intent.getStringExtra("email"));
        }
        if (intent.hasExtra("password")) {
            userPassword.setText(intent.getStringExtra("password"));
        }
        if (intent.hasExtra("cnfpassword")) {
            confirmPassword.setText(intent.getStringExtra("cnfpassword"));
        }
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing views
        userEmailId = (EditText) findViewById(R.id.email);
        userPassword = (EditText) findViewById(R.id.Password);
        Register = (Button) findViewById(R.id.Register);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //calling register method on click
                registerUser();
            }

        });
        String email = userEmailId.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            registerUser();

        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 8;
    }

    private void config() {

        String email = userEmailId.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String userName = fullName.getText().toString().trim();


        SharedPreferences sp = getSharedPreferences("Register", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("UnmReg", email);
        Ed.putString("PswReg", password);
        Ed.putString("username", userName);
        Ed.commit();


    }

    private void registerUser() {
        config();

        //getting email and password from edit texts
        String email = userEmailId.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }


        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            //display some message here
                            Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegisterActivity.this, DashActivity.class);
                            startActivity(i);
                        } else {
                            //display some message here
                            Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public void logIn(View view) {

        Intent intent = new Intent(this, ManLoginActivity.class);
        startActivity(intent);

    }


}
