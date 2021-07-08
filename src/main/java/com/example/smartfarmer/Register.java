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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText regname,signpass,signmail;
    private ProgressBar progressBar2;
    private Button signup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(this);

        regname = (EditText) findViewById(R.id.regname);
        signpass = (EditText) findViewById(R.id.signpass);
        signmail = (EditText) findViewById(R.id.signmail);


        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup:
                progressBar2.setVisibility(View.VISIBLE);
                registerUser();
                break;
            default:
                break;
        }
    }

    private void registerUser() {
        String email = signmail.getText().toString().trim();
        String name = regname.getText().toString().trim();
        String password = signpass.getText().toString().trim();

        if(name.isEmpty()){
            progressBar2.setVisibility(View.GONE);
            regname.setError("Name is required");
            regname.requestFocus();
            return;
        }
        if(password.isEmpty()){
            progressBar2.setVisibility(View.GONE);
            signpass.setError("Password is required");
            signpass.requestFocus();
            return;
        }
        if(password.length() <6){
            progressBar2.setVisibility(View.GONE);
            signpass.setError("Password is lessthan 6 characters");
            signpass.requestFocus();
            return;
        }
        if(email.isEmpty()){
            progressBar2.setVisibility(View.GONE);
            signmail.setError("Email is reqired");
            signmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            progressBar2.setVisibility(View.GONE);
            signmail.setError("Please provide a valid email");
            signmail.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {


                        if(task.isSuccessful()){
                            User user = new User(name, email,password);

                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull  Task<Void> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(Register.this, "user has been registered successfuly",Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(Register.this, Profile.class));
                                        progressBar2.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(Register.this,"Failed toooo register! Try again", Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }else{
                            Toast.makeText(Register.this,"Failed to register! Try again", Toast.LENGTH_LONG).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });

    }
}