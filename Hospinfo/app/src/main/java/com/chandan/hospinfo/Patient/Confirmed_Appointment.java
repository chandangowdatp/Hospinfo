package com.chandan.hospinfo.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.chandan.hospinfo.CustomListViewAdapter;
import com.chandan.hospinfo.CustomListViewAdapter2;
import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;
import com.chandan.hospinfo.RowItem;

import java.util.ArrayList;
import java.util.List;


public class Confirmed_Appointment extends AppCompatActivity {
    ListView ca_list;
    List<RowItem> rowItems;
    ArrayList<String> doc = new ArrayList<>();
    ArrayList<String> pro = new ArrayList<>();
    ArrayList<String> slo = new ArrayList<>();


    String username,password,user_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmed_appointment);
        getSupportActionBar().setTitle("Appointment");



        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");

        ca_list = (ListView) findViewById(R.id.lv_confirmed_appontments);

        final DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor y = dbh.checkduplicates_in_user_credentials("", "", "all_pending_appointment");

        if (y.moveToFirst()) {
            while (true) {

                //pateinet approvl has three mode W - wait, A - approved, F - finished

                if (y.getString(4).equals("A") && y.getString(0).equals(username)) {
                    DatabaseHelper dbh1 = new DatabaseHelper(this);
                    Cursor z1 = dbh1.checkduplicates_in_user_credentials(y.getString(0), y.getString(1), getResources().getString(R.string.user_credentials));
                    DatabaseHelper dbh2 = new DatabaseHelper(this);
                    Cursor z2 = dbh2.checkduplicates_in_user_credentials(y.getString(2), y.getString(3), getResources().getString(R.string.user_credentials));


                    if (z2.moveToNext()) {
                        doc.add(z2.getString(1) + " " + z2.getString(2));
                    }
                    pro.add(y.getString(5));
                    slo.add(y.getString(8)+" "+y.getString(9));




                    dbh1.close();
                    dbh2.close();
                }

                if (y.isLast())
                    break;

                y.moveToNext();
            }

            rowItems = new ArrayList<>();

            for (int i = 0; i < doc.size(); i++) {
                RowItem item = new RowItem(slo.get(i),doc.get(i),pro.get(i));

                rowItems.add(item);
            }

            CustomListViewAdapter2 adapter = new CustomListViewAdapter2(this, R.layout.custom_adapter2, rowItems);
            ca_list.setAdapter(adapter);

        } else {
            Message.message(Confirmed_Appointment.this, "No Confirmed Apppointments");
            finish();

        }


    }
}