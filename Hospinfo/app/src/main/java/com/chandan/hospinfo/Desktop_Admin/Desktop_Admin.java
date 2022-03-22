package com.chandan.hospinfo.Desktop_Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Desktop_Admin.Doctor_related.Doctors_Leave;
import com.chandan.hospinfo.Personal_Info;
import com.chandan.hospinfo.R;

public class Desktop_Admin extends AppCompatActivity {

    String username,password,user_type;
    DatabaseHelper dbh;
    TextView daname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desktop_admin);
        getSupportActionBar().setTitle("Admin");


        dbh = new DatabaseHelper(this);
        daname = (TextView) findViewById(R.id.tv_da_name);


        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");

        Cursor y = dbh.checkduplicates_in_user_credentials(username, password,getResources().getString(R.string.user_credentials));

        if (y.moveToFirst()) {
            String name = y.getString(1);
            daname.setText(name);
        }
    }

    public void onClick(View view){

        Intent i;
        Bundle b = new Bundle();
        b.putString("username",username);
        b.putString("password",password);
        b.putString("user_type",user_type);

        switch (view.getId())
        {
            case R.id.b_da_info:
                i = new Intent(Desktop_Admin.this, Personal_Info.class);
                break;
            case R.id.b_da_patient_appointment:
                i = new Intent(Desktop_Admin.this, Grant_appointment.class);
                break;
            case R.id.b_da_addusers:
                i = new Intent(Desktop_Admin.this, Add_Users.class);
                break;
            case R.id.b_da_doctor_leave:
                i = new Intent(Desktop_Admin.this, Doctors_Leave.class);
                break;
            default:
                i = new Intent(Desktop_Admin.this, Delete_Users.class);
                break;
        }

        i.putExtras(b);
        startActivity(i);
    }
}