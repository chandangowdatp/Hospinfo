package com.chandan.hospinfo.Desktop_Admin.Doctor_related;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Desktop_Admin.Add_Users;
import com.chandan.hospinfo.Desktop_Admin.Desktop_Admin;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;

import java.util.ArrayList;

public class D_Slot extends AppCompatActivity {

    Spinner  se, ob;
    String s,o, username, password, user_type,spl;
    String user_type_a,password_a,username_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dslot);
        getSupportActionBar().setTitle("Add Slot");


        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");
        username_a = bb.getString("username_a");
        password_a= bb.getString("password_a");
        user_type_a = bb.getString("user_type_a");
        spl=bb.getString("spl");

        se = (Spinner) findViewById(R.id.sl);
        ob=(Spinner)findViewById(R.id.obd);

        ArrayList<String> hour = new ArrayList<>();
        ArrayList<String> obds= new ArrayList<>();

        hour.add("Day shift");
        hour.add("Night shift");

        obds.add("OBD1");
        obds.add("OBD2");

        ArrayAdapter<String> adapterh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hour);

         adapterh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<String> adaptero = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,obds);
        adaptero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        se.setAdapter(adapterh);

        ob.setAdapter(adaptero);

    }

    public void onClick(View view) {

        s = se.getSelectedItem().toString();

        o=ob.getSelectedItem().toString();

        DatabaseHelper db = new DatabaseHelper(this);
        db.insert_slot(username, password,spl ,s, "Y",o);
        Message.message(D_Slot.this, "Slot Has been Inserted");
        Intent i;
        Bundle bb = new Bundle();
        bb.putString("username",username_a);
        bb.putString("password",password_a);
        bb.putString("user_type",user_type_a);

        i = new Intent(D_Slot.this, Desktop_Admin.class);
        i.putExtras(bb);
        db.close();
        startActivity(i);

    }
}