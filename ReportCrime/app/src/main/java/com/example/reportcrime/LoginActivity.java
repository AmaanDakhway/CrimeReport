package com.example.reportcrime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText EmailAddress,Password;
    Button Login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailAddress = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        Login = findViewById(R.id.Login);
        firebaseAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = EmailAddress.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    EmailAddress.setError("Email is Required.");
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Password.setError("Password is Required.");
                    return;
                }

                else if(password.length() < 6){
                    Password.setError("Password Must be >= 6 Characters");
                    return;
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });

    }
}