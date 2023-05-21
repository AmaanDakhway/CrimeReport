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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    EditText EmailAddress, Password, Phone;
    Button Registration;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        EmailAddress     = findViewById(R.id.emailAddress);
        Password         = findViewById(R.id.password);
        Phone            = findViewById(R.id.phone);
        Registration     = findViewById(R.id.Registration);

        firebaseAuth = FirebaseAuth.getInstance();

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(EmailAddress.getText().toString().trim())){
                    EmailAddress.setError("Email is Required.");
                    return;
                }

                else if(TextUtils.isEmpty(Phone.getText().toString().trim())){
                    Phone.setError("Password is Required.");
                    return;
                }

                else if(Password.getText().toString().length() < 6){
                    Password.setError("Password Must be >= 6 Characters");
                    return;
                }

                else{
                    firebaseAuth.createUserWithEmailAndPassword(
                            EmailAddress.getText().toString().trim(),Password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        String userID = firebaseAuth.getCurrentUser().getUid();
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS").child(userID);
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("EMAIL",EmailAddress.getText().toString());
                                        hashMap.put("PHONE",Phone.getText().toString());
                                        hashMap.put("PASSWORD",Password.getText().toString());

                                        databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(),"You are Registered Succesfully",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    }else{
                                        Toast.makeText(getApplicationContext(),"Error ! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }



            }
        });


    }
}