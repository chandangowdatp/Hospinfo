package com.chandan.hospinfo.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.Personal_Info;
import com.chandan.hospinfo.R;

public class Patient extends AppCompatActivity {

    String username, password, user_type;
    DatabaseHelper dbh;
    TextView pname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient);

        getSupportActionBar().setTitle("Patient");

        dbh = new DatabaseHelper(this);
        pname = (TextView) findViewById(R.id.tv_p_name);


        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");

        Cursor y = dbh.checkduplicates_in_user_credentials(username, password, getResources().getString(R.string.user_credentials));

        if (y.moveToFirst()) {
            String name = y.getString(1);
            pname.setText(name);
        }
    }

    public void onClick(View view) {

        Intent i;
        Bundle b = new Bundle();
        b.putString("username", username);
        b.putString("password", password);
        b.putString("user_type", user_type);

        switch (view.getId()) {
            case R.id.b_p_info:
                i = new Intent(Patient.this, Personal_Info.class);
                break;
            case R.id.b_p_appointment:
                i = new Intent(Patient.this, Appointment.class);
                break;
            default:
                i = new Intent(Patient.this, View_Report.class);
                break;

        }

        i.putExtras(b);
        startActivity(i);
    }


    public void clear(View view) {
        boolean b = false;
        final DatabaseHelper db = new DatabaseHelper(this);
        Cursor y = db.checkduplicates_in_user_credentials("", "", "all_pending_appointment");
        if (y.moveToFirst()) {
            while (true) {

                if (y.getString(4).equals("F") && y.getString(0).equals(username)) {
                    b = db.delete_doctor_patient(username);

                }
                if (y.isLast()) {
                    break;
                }
                y.moveToNext();
            }
            if(b) {
                Message.message(Patient.this, "Cleared successfully");
            }
        } else {
            Message.message(Patient.this, "No History");
        }
    }
}