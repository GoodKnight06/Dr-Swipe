package com.example.drswipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button loginbutton = findViewById(R.id.loginbutton);
        final Button signupbutton = findViewById(R.id.signupbutton);
        final Intent loginna = new Intent(this, LogInActivity.class);
        final Intent signupna = new Intent(this, SignUpActivity.class);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginna);
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(signupna);
            }
        });
    }

}
