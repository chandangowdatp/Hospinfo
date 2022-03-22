package com.chandan.hospinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

       //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg)));
        getWindow().setStatusBarColor(ContextCompat.getColor(   Splashscreen.this, R.color.bg));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Splashscreen.this,MainActivity.class);
                startActivity(i);

                finish();
            }
        }, 3000);
    }
}