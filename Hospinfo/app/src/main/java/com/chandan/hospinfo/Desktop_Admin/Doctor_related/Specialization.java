package com.chandan.hospinfo.Desktop_Admin.Doctor_related;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Desktop_Admin.Add_Users;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;

import java.util.ArrayList;

public class Specialization extends AppCompatActivity {

    String s, username, password, user_type;
    String user_type_a,password_a,username_a;

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialization);
        getSupportActionBar().setTitle("Doctor Specialization");


        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");
        username_a = bb.getString("username_a");
        password_a= bb.getString("password_a");
        user_type_a = bb.getString("user_type_a");
        et = (EditText) findViewById(R.id.et_spl);


    }

    public void onClick(View view) {
        s = et.getText().toString();

        if (s.length() > 0) {
            Intent i;

            Bundle bb = new Bundle();
            bb.putString("username", username);
            bb.putString("password", password);
            bb.putString("user_type", user_type);
            bb.putString("username_a",username_a);
            bb.putString("password_a",password_a);
            bb.putString("user_type_a",user_type_a);

            bb.putString("spl",s);
            i = new Intent(Specialization.this, D_Slot.class);
            i.putExtras(bb);
            startActivity(i);
        } else {
            Message.message(Specialization.this, "Please Write Your Specialization");
        }
    }
}