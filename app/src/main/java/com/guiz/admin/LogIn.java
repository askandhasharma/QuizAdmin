package com.guiz.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LogIn extends AppCompatActivity {

    TextInputEditText phoneno;
    MaterialButton sendOTP;
    public static String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        phoneno = (TextInputEditText) findViewById(R.id.phoneno);
        sendOTP = (MaterialButton) findViewById(R.id.sendotp);

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneno.getText().toString();
                if(phone.length() != 10)
                    phoneno.setError("PLease enter a valid no.");
                else{
                    Intent intent = new Intent(LogIn.this, Verification.class);
                    startActivity(intent);
                }
            }
        });

    }
}