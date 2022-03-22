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

public class choose_spl extends AppCompatActivity {

    String username,password,obd,slot,spl,slt,wk,user_type;
    DatabaseHelper dbh;
    Spinner spe;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_spl);
        getSupportActionBar().setTitle("Appointment");

        Bundle b = getIntent().getExtras();
        username = b.getString("username");
        password = b.getString("password");
        user_type = b.getString("user_type");
        obd =b.getString("obd");
        slot = b.getString("slot");
        slt = b.getString("slt");
        wk=b.getString("wk");
        spe=(Spinner)findViewById(R.id.spinner_specialization) ;
        search=(Button)findViewById(R.id.newbid);


        dbh = new DatabaseHelper(this);
        Cursor y = dbh.checkduplicates_in_user_credentials(slot, obd, "display_specialization");


        List<String> spl_lst = new ArrayList<>();
        if (!y.moveToFirst()) {
            Message.message(choose_spl.this, "Sorry You have No Doctors in Current Slot");
            finish();
        } else {

            while (true) {
                spl_lst.add(y.getString(0));

                if (y.isLast()) {
                    break;
                }
                y.moveToNext();
            }
        }
        Message.message(choose_spl.this, String.valueOf(spl_lst.size()) + " specializations found");
        ArrayAdapter<String> adapter_spl = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spl_lst);
        adapter_spl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spe.setAdapter(adapter_spl);
        dbh.close();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spl = spe.getSelectedItem().toString();
                Bundle b = new Bundle();
                b.putString("username", username);
                b.putString("password", password);
                b.getString("user_type",user_type);
                b.putString("slot",slot);
                b.putString("slt",slt);
                b.putString("wk",wk);
                b.putString("obd",obd);
                b.putString("specialization", spl);

             //   Message.message(choose_spl.this,"wk="+wk+" slt="+slt);

                Intent i = new Intent(choose_spl.this, Select_Doctor.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

    }
}