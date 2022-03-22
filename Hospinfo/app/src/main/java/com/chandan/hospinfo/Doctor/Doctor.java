package com.chandan.hospinfo.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Personal_Info;
import com.chandan.hospinfo.R;

public class Doctor extends AppCompatActivity {

    String username,password,user_type;
    DatabaseHelper dbh;
    TextView dname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor);
        getSupportActionBar().setTitle("Doctor");


        dbh = new DatabaseHelper(this);
        dname = (TextView) findViewById(R.id.tv_d_name);


        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");

        Cursor y = dbh.checkduplicates_in_user_credentials(username, password,getResources().getString(R.string.user_credentials));

        if (y.moveToFirst()) {
            String name = y.getString(1);
            dname.setText("Welcome Dr. "+name);
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
            case R.id.b_d_info:
                i = new Intent(Doctor.this, Personal_Info.class);
                break;

            case R.id.b_d_leaves:
                i = new Intent(Doctor.this, Leaves.class);
                break;
            default:
                i = new Intent(Doctor.this, Report_Upload.class);
                break;
        }
        i.putExtras(b);
        startActivity(i);
    }
}