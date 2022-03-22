package com.chandan.hospinfo.Desktop_Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class Delete_Users extends AppCompatActivity {

    ListView lv_all;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> uname = new ArrayList<>();
    ArrayList<String> pass = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();
    DatabaseHelper db = new DatabaseHelper(this);
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_users);
        getSupportActionBar().setTitle("Delete User");

        lv_all = (ListView) findViewById(R.id.lv_all_users);


        Cursor y = db.checkduplicates_in_user_credentials("", "", "get_all_doctors");

        if (!y.moveToFirst()) {
            Message.message(Delete_Users.this, "Sorry You have No users");
            finish();
        } else {
            while (true) {
                name.add(y.getString(1) + " " + y.getString(2) + " (" + y.getString(7) + ")");
                uname.add(y.getString(12));
                pass.add(y.getString(11));
                type.add(y.getString(7));

                if (y.isLast())
                    break;
                y.moveToNext();
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name);
            lv_all.setAdapter(adapter);
        }
        

        lv_all.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(Delete_Users.this)
                        .setIcon(R.drawable.personlogo)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this user?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean by = db.delete_user_credentials(uname.get(position), pass.get(position));



                                if (type.get(position).equals("Doctor")) {
                                    db.delete_slot(uname.get(position), pass.get(position));
                                    db.delete_leaves(uname.get(position), pass.get(position));
                                    name.remove(position);
                                    adapter.notifyDataSetChanged();

                                }

                                if (by) {
                                    Message.message(Delete_Users.this, "User Deleted");
                                    finish();
                                } else {
                                    Message.message(Delete_Users.this, "User Cannot Be deleted Try again");
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;

            }
        });


    }
}