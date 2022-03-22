package com.chandan.hospinfo.Patient;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;

import java.util.ArrayList;
import java.util.List;

public class New_Appointment extends AppCompatActivity {


    String username, password, user_type, spl, slt, ob, wk;
    String tm;
    int k = 0;
    Spinner  slot, week;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_appointment);
        getSupportActionBar().setTitle("Appointment");
        Bundle bb = getIntent().getExtras();

        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");


        slot = (Spinner) findViewById(R.id.spinner_slot);
        week = (Spinner) findViewById(R.id.spinner_week);
        b1 = (Button) findViewById(R.id.refresh);

        List<String> s_slot = new ArrayList<>();
        s_slot.add("Morning (9-12)");
        s_slot.add("Afternoon (1-4)");
        s_slot.add("Evening (5-9)");

        ArrayAdapter<String> adapter_slot = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, s_slot);
        adapter_slot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slot.setAdapter(adapter_slot);

        List<String> s_week = new ArrayList<>();
        s_week.add("Monday");
        s_week.add("Tuesday");
        s_week.add("Wednesday");
        s_week.add("Thursday");
        s_week.add("Friday");
        s_week.add("Saturday");
        ArrayAdapter<String> adapter_week = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, s_week);
        adapter_week.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        week.setAdapter(adapter_week);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slt = slot.getSelectedItem().toString();
                wk = week.getSelectedItem().toString();
                if (wk.equals("Monday") || wk.equals("Wednesday") || wk.equals("Friday")) {
                    ob = "OBD1";
                } else {
                    ob = "OBD2";
                }

                if (slt.equals("Morning (9-12)") || slt.equals("Afternoon (1-4)")) {
                    tm = "Day shift";
                } else {
                    tm = "Night shift";
                }
                Bundle b = new Bundle();
                b.putString("username", username);
                b.putString("password", password);
                b.putString("user_type",user_type);
                b.putString("slot",tm);
                b.putString("slt",slt);
                b.putString("wk",wk);
                b.putString("obd",ob);

                Intent i = new Intent(New_Appointment.this, choose_spl.class);
                i.putExtras(b);
                startActivity(i);

            }
        });
    }
}