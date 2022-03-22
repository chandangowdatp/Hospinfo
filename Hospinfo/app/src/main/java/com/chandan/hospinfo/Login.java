package com.chandan.hospinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chandan.hospinfo.Desktop_Admin.Desktop_Admin;
import com.chandan.hospinfo.Doctor.Doctor;
import com.chandan.hospinfo.Patient.New_Appointment;
import com.chandan.hospinfo.Patient.Patient;

public class Login extends AppCompatActivity {


    EditText username, password;
    String usernames, passwords;
    Button Bregister, Blogin;
    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getSupportActionBar().setTitle("Login");

        username = (EditText) findViewById(R.id.etusername);
        password = (EditText) findViewById(R.id.etpassword);
        Bregister = (Button) findViewById(R.id.bregister);
        Blogin = (Button) findViewById(R.id.blogin);
        dbh = new DatabaseHelper(this);

        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        Blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernames = username.getText().toString();
                passwords = password.getText().toString();
                username.setText("");
                password.setText("");

                Cursor y = dbh.checkduplicates_in_user_credentials(usernames, passwords, getResources().getString(R.string.user_credentials));

                if (y.moveToFirst()) {
                    String ut = y.getString(7);


                    Bundle b = new Bundle();
                    b.putString("username", usernames);
                    b.putString("password", passwords);
                    b.putString("user_type", ut);

                    Intent i;
                    if (ut.equals("Doctor")) {
                        Message.message(Login.this, "Welcome Dr." +usernames);
                        i = new Intent(Login.this, Doctor.class);
                    } else if (ut.equals("Patient")) {
                        Message.message(Login.this, "Welcome " +usernames);
                        i = new Intent(Login.this, Patient.class);
                    } else {
                        Message.message(Login.this, "Welcome " +usernames);
                        i = new Intent(Login.this, Desktop_Admin.class);
                    }

                    i.putExtras(b);
                    startActivity(i);
                } else {
                    Message.message(Login.this, "No Such User Exists");
                    Message.message(Login.this, "Please Register Yourself");
                }

                y.close();
            }
        });
        dbh.close();
    }
}