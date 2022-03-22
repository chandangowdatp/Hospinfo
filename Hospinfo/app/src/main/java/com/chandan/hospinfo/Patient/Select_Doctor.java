package com.chandan.hospinfo.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;

import java.util.ArrayList;
import java.util.List;

public class Select_Doctor extends AppCompatActivity {

    String username, password, user_type, specialization, slot, ss, name, slt,wk,obd;
    ListView lv_doctors;
    List<String> s_doctors;
    List<String> u_d;
    List<String> p_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_doctor);
        getSupportActionBar().setTitle("Appointment");

        Bundle b = getIntent().getExtras();
        username = b.getString("username");
        password = b.getString("password");
        specialization = b.getString("specialization");
        obd =b.getString("obd");
        slot = b.getString("slot");
        slt = b.getString("slt");
        wk=b.getString("wk");


        lv_doctors = (ListView) findViewById(R.id.lv_doctors_available);
        s_doctors = new ArrayList<>();
        u_d = new ArrayList<>();
        p_d = new ArrayList<>();



        DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor z;
        Cursor y = dbh.checkduplicates_in_user_credentials(slot, obd,"display_doctors_to_apply");
        if (!y.moveToFirst()) {
            Message.message(Select_Doctor.this, "Sorry You have No Doctors in Current Slot");
            finish();
        } else {
            while (true) {
                if(y.getString(2).equals(specialization)) {
                    name = y.getString(0);
                    ss = y.getString(1);
                    u_d.add(name);
                    p_d.add(ss);


                    s_doctors.add("Dr. " + name);

                }
                if (y.isLast()) {
                    break;
                }
                y.moveToNext();

               }
        }
        ArrayAdapter adapter_doc = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, s_doctors);
        lv_doctors.setAdapter(adapter_doc);


        lv_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Select_Doctor.this, Apply.class);
                Bundle b = new Bundle();
                b.putString("d_username", u_d.get(position));
                b.putString("d_password", p_d.get(position));
                b.putString("username", username);
                b.putString("slot",slot);
                b.putString("slt",slt);
                b.putString("wk",wk);
                b.putString("password", password);
                i.putExtras(b);

                startActivity(i);
            }
        });
    }
}
