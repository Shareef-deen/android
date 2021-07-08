package com.example.smartfarmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register;
    private EditText logemail, logpassword;
    private Button logbutton;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        logbutton = (Button) findViewById(R.id.logbutton);
        logbutton.setOnClickListener(this);

        logemail = (EditText) findViewById(R.id.logemail);
        logpassword = (EditText) findViewById(R.id.logpassword);


        register = (TextView) findViewById(R.id.reg);
        register.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reg:
                startActivity(new Intent(this, Register.class));
                progressBar.setVisibility(View.GONE);
                break;

            case R.id.logbutton:
                progressBar.setVisibility(View.VISIBLE);
                userlogin();
                break;
        }

    }

    private void userlogin() {
        String email = logemail.getText().toString().trim();
        String password = logpassword.getText().toString().trim();

        if(email.isEmpty()){
            progressBar.setVisibility(View.GONE);
            logemail.setError("Email is required");
            logemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            progressBar.setVisibility(View.GONE);
            logemail.setError("Please enter valid email");
            logemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            progressBar.setVisibility(View.GONE);
            logpassword.setError("Password is empty");
            logpassword.requestFocus();
            return;
        }

        if(password.length()<6){
            progressBar.setVisibility(View.GONE);
            logpassword.setError("Password is lessthan 6 characters");
            logpassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, Profile.class));
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Login Failed, invalid username or password ",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}