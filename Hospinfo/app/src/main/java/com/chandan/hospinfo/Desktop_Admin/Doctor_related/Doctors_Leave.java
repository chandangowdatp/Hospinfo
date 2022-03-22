package com.chandan.hospinfo.Desktop_Admin.Doctor_related;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chandan.hospinfo.CustomListViewAdapter;
import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Desktop_Admin.Delete_Users;
import com.chandan.hospinfo.Desktop_Admin.Grant_appointment;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;
import com.chandan.hospinfo.RowItem;

import java.util.ArrayList;
import java.util.List;

public class Doctors_Leave extends AppCompatActivity {

    ListView lv_all;
    List<String> dname;
    List<String> dn;
    ArrayList<String> uname = new ArrayList<>();
    ArrayList<String> pass = new ArrayList<>();
    List<String> df;
    ArrayAdapter adapter;
    String sms_for_d,dnn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_leave);
        getSupportActionBar().setTitle("Leaves");


        lv_all = (ListView) findViewById(R.id.lv_doctors_leave);

        dname = new ArrayList<>();
        dn = new ArrayList<>();
        df = new ArrayList<>();

        final DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor y = dbh.checkduplicates_in_user_credentials("", "", "all_pending_leaves");

        if (y.moveToFirst()) {
            while (true) {

                if (y.getString(5).equals("N")) {
                    DatabaseHelper dbh1 = new DatabaseHelper(this);
                    Cursor z1 = dbh1.checkduplicates_in_user_credentials(y.getString(0), y.getString(1), getResources().getString(R.string.user_credentials));
                    if(z1.moveToFirst()) {
                        dname.add("Dr. "+z1.getString(1)+ " " + z1.getString(2)+" ["+y.getString(3)+" to "+y.getString(4)+"]");
                        uname.add(y.getString(0));
                        pass.add(y.getString(1));
                        df.add(y.getString(3));
                        dn.add(z1.getString(10));

                    }
                    dbh1.close();
                }
                if (y.isLast())
                    break;

                y.moveToNext();
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dname);
            lv_all.setAdapter(adapter);
            }else{
            Message.message(Doctors_Leave.this, "Sorry You have zero application");
            finish();
        }

        lv_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            boolean y = false;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(Doctors_Leave.this)
                        .setIcon(R.drawable.personlogo)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to grant leave?")
                        .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor x = dbh.checkduplicates_in_user_credentials(uname.get(position), df.get(position),"approve_leaves");

                                if (x.moveToFirst()) {
                                    sms_for_d="Hello Dr."+x.getString(0)+", Your Leave Application from "+x.getString(3)+" to "+x.getString(4)+"  is approved.";

                                   y= dbh.update_leaves(x.getString(0), x.getString(1), x.getString(2), x.getString(3), x.getString(4), "L");
                                }

                                if(y){
                                    dnn=dn.get(position);
                                    Message.message(Doctors_Leave.this, "Application Approved");
                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                        if(getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                                            Message.message(Doctors_Leave.this, "Permission granted");

                                            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                                // sendSMS();
                                                try {
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(dnn, null, sms_for_d, null, null);
                                                    Message.message(Doctors_Leave.this, "Sms is sent");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Message.message(Doctors_Leave.this, "Sms is not sent");
                                                }
                                            } else {
                                                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                                            }
                                        }
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                                    }
                                    finish();
                                } else {
                                    Message.message(Doctors_Leave.this, "Not Approved");
                                }
                            }
                        })
                        .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor x = dbh.checkduplicates_in_user_credentials(uname.get(position), df.get(position),"approve_leaves");
                                if (x.moveToFirst()) {
                                    sms_for_d="Hello Dr."+x.getString(0)+", Your Leave Application from "+x.getString(3)+" to "+x.getString(4)+"  is Rejected.";
                                    y= dbh.update_leaves(x.getString(0), x.getString(1), x.getString(2), x.getString(3), x.getString(4), "F");
                                }

                                if(y){
                                    dnn=dn.get(position);
                                    Message.message(Doctors_Leave.this, "Application Rejected");
                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                        if(getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                                         //   Message.message(Doctors_Leave.this, "Permission granted");

                                            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                                // sendSMS();
                                                try {
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(dnn, null, sms_for_d, null, null);
                                                    Message.message(Doctors_Leave.this, "Sms is sent");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Message.message(Doctors_Leave.this, "Sms is not sent");
                                                }
                                            } else {
                                                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                                            }
                                        }
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                                    }
                                    finish();
                                } else {
                                    Message.message(Doctors_Leave.this, "Not Approved");
                                }
                            }
                        })
                        .show();
        }



            });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //    Message.message(Doctors_Leave.this, "Permission granted");
            }else{
              //  Message.message(Doctors_Leave.this, "Permission denied");

            }
        }
    }


}