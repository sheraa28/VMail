package com.example.parmindersingh.vmail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText userEmailId;
    private EditText mobileNumber;
    private ProgressBar progressBar;
    private EditText password;
    private EditText confirmPassword;
    private Button Register;
    private TextView alreadyUser;


    private void findViews() {
        fullName = (EditText)findViewById( R.id.fullName );
        userEmailId = (EditText)findViewById( R.id.email );
        mobileNumber = (EditText)findViewById( R.id.contact );
        progressBar = (ProgressBar)findViewById( R.id.progressBar );
        password = (EditText)findViewById( R.id.password );
        confirmPassword = (EditText)findViewById( R.id.cnfPassword );
        Register = (Button)findViewById( R.id.Register );
        alreadyUser = (TextView)findViewById( R.id.already_user );


    }


    public void onClick(View v) {
        if ( v == Register ) {

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // BC Method banaya he toh call bhi karle.
        findViews();

        Intent intent = getIntent();
        if (intent.hasExtra("fullname")) {
            fullName.setText(intent.getStringExtra("fullname"));
        }
        if (intent.hasExtra("email")) {
            userEmailId.setText(intent.getStringExtra("email"));
        }
        if (intent.hasExtra("contact")) {
            mobileNumber.setText(intent.getStringExtra("contact"));
        }
        if (intent.hasExtra("password")) {
            password.setText(intent.getStringExtra("password"));
        }
        if (intent.hasExtra("cnfpassword")) {
            confirmPassword.setText(intent.getStringExtra("cnfpassword"));
        }

    }

    public void logIn(View view) {

            Intent intent = new Intent( this,ManLoginActivity.class);
            startActivity(intent);

    }


}
