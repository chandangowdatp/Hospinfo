package com.chandan.hospinfo.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;

import java.util.ArrayList;

public class View_Leaves extends AppCompatActivity {


        String username, password, user_type;
        ListView lvy, lvn;


       @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view_leaves);
           getSupportActionBar().setTitle("Leaves");

            Bundle bb = getIntent().getExtras();
            username = bb.getString("username");
            password = bb.getString("password");
            user_type = bb.getString("user_type");

            lvy = (ListView) findViewById(R.id.lvy);
            lvn = (ListView) findViewById(R.id.lvn);

            DatabaseHelper dbh = new DatabaseHelper(this);
            Cursor y = dbh.checkduplicates_in_user_credentials("", "", "all_pending_leaves");


            if (y.moveToFirst()) {
                ArrayList<String> ay = new ArrayList<>();
                ArrayList<String> an = new ArrayList<>();

                if (y.getString(5).equals("F")&&y.getString(0).equals(username))
                    an.add("FROM: " + y.getString(3) + " TO: " + y.getString(4));
                else if (y.getString(5).equals("L")&&y.getString(0).equals(username))
                    ay.add("FROM: " + y.getString(3) + " TO: " + y.getString(4));

                if (!y.isLast()) {
                    y.moveToNext();
                    while (true) {
                        if (y.getString(5).equals("F")&&y.getString(0).equals(username))
                            an.add("FROM: " + y.getString(3) + " TO: " + y.getString(4));
                        else if (y.getString(5).equals("L")&&y.getString(0).equals(username))
                            ay.add("FROM: " + y.getString(3) + " TO: " + y.getString(4));

                        if (y.isLast())
                            break;

                        y.moveToNext();
                    }
                }

                ArrayAdapter adaptery = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ay);
                ArrayAdapter adaptern = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, an);
                lvy.setAdapter(adaptery);
                lvn.setAdapter(adaptern);
            } else {
                Message.message(View_Leaves.this, "Sorry You have No Applications");
                finish();
            }
        }
    }