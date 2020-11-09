package com.guiz.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {

    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationid;
    TextInputEditText otp;
    MaterialButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                System.out.println("\n inside mcallback code sent");
                verificationid = s;
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null){
                    System.out.println("\n inside mcallback onverification completed");
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Verification.this, e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("\n inside mcallback verification failed");
            }
        };

        sendVerificationCode("91" +LogIn.phone);

        otp = (TextInputEditText) findViewById(R.id.otp);
        login = (MaterialButton) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = otp.getText().toString();
                if(OTP.length() != 6)
                    otp.setError("Enter a valid OTP");
                else{
                    verifyCode(OTP);
                }
            }
        });

    }


    private void sendVerificationCode(String number){
        System.out.println("\n send verification code");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
    }

    private void verifyCode(String code){
        System.out.println("\n verify code");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        System.out.println("\n signin with credentials");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = Verification.this.getSharedPreferences("user", MODE_PRIVATE);
                            sharedPreferences.edit().putString("cno", LogIn.phone ).apply();
                            sharedPreferences.edit().putBoolean("logged", true).apply();

                            Toast.makeText(Verification.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Verification.this, MainActivity.class));
                        } else{
                            Toast.makeText(Verification.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

}