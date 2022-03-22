package com.chandan.hospinfo.Desktop_Admin;

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
import android.widget.ListView;

import com.chandan.hospinfo.CustomListViewAdapter;
import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;
import com.chandan.hospinfo.RowItem;

import java.util.ArrayList;
import java.util.List;

public class Grant_appointment extends AppCompatActivity {


    ListView lv_appointment;
    List<String> pn;
    List<String> dn;
    List<String> u_p;
    List<String> u_d;
    List<RowItem> rowItems;
    ArrayList<String> doc = new ArrayList<>();
    ArrayList<String> pat = new ArrayList<>();
    ArrayList<String> pro = new ArrayList<>();
    CustomListViewAdapter adapter;
    String sms_for_p;
    String sms_for_d;
    String pnn;
    String dnn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grant_appointment);
        getSupportActionBar().setTitle("Grant Appointment");


        lv_appointment = (ListView) findViewById(R.id.lv_pending_appontments);
        u_p = new ArrayList<>();
        u_d = new ArrayList<>();
        pn = new ArrayList<>();
        dn = new ArrayList<>();

        final DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor y = dbh.checkduplicates_in_user_credentials("", "", "all_pending_appointment");

        if (y.moveToFirst()) {
            while (true) {

                //pateinet approvl has three mode W - wait, A - approved, F - finished

                if (y.getString(4).equals("W")) {

                    DatabaseHelper dbh1 = new DatabaseHelper(this);
                    Cursor z1 = dbh1.checkduplicates_in_user_credentials(y.getString(0), y.getString(1), getResources().getString(R.string.user_credentials));
                    DatabaseHelper dbh2 = new DatabaseHelper(this);
                    Cursor z2 = dbh2.checkduplicates_in_user_credentials(y.getString(2), y.getString(3), getResources().getString(R.string.user_credentials));
                    u_p.add(y.getString(0));
                    u_d.add(y.getString(2));




                  //  Message.message(Grant_appointment.this, "Hello vishwa");

                    if (z1.moveToNext()) {
                        pat.add(z1.getString(1) + " " + z1.getString(2));
                        pn.add(z1.getString(10));

                    }

                    if (z2.moveToNext()) {
                        doc.add(z2.getString(1) + " " + z2.getString(2));
                        dn.add(z2.getString(10));
                    }
                    pro.add(y.getString(5));

                    dbh1.close();
                    dbh2.close();
                }

                if (y.isLast())
                    break;

                y.moveToNext();
            }

            rowItems = new ArrayList<>();

            for (int i = 0; i < doc.size(); i++) {
                RowItem item = new RowItem(doc.get(i), pat.get(i), pro.get(i));
                rowItems.add(item);
            }

            adapter = new CustomListViewAdapter(this, R.layout.custom_adapter, rowItems);
            lv_appointment.setAdapter(adapter);
        } else {
            Message.message(Grant_appointment.this, "No Pending Apppointments");
            finish();
        }

        lv_appointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            boolean y = false;
            @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                new AlertDialog.Builder(Grant_appointment.this)
                        .setIcon(R.drawable.personlogo)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to grant appointment this user?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor x = dbh.checkduplicates_in_user_credentials(u_p.get(position), u_d.get(position), pro.get(position));
                                if (x.moveToFirst()) {
                                    sms_for_p="Hello "+x.getString(0)+", Your Appointment at "+x.getString(9)+" on "+x.getString(8)+" with Doctor:"+ x.getString(2)+ " is Fixed.";
                                    sms_for_d="Hello Dr."+x.getString(2)+", Your Appointment at "+x.getString(9)+" on "+x.getString(8)+" with Patient:"+ x.getString(0)+ " is Fixed.";
                                    y = dbh.update_doctor_patient(x.getString(0), x.getString(1), x.getString(2), x.getString(3), "A", x.getString(5), x.getString(6), x.getBlob(7), x.getString(8), x.getString(9));

                                    rowItems.remove(position);
                                    adapter.notifyDataSetChanged();
                                }


                                if (y) {

                                    pnn=pn.get(position);
                                    dnn=dn.get(position);
                                    Message.message(Grant_appointment.this, "Application Approved");
                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                        if(getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
                                            Message.message(Grant_appointment.this, "Permission granted");

                                            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                                // sendSMS();
                                                try {
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(pnn, null, sms_for_p, null, null);
                                                    smsManager.sendTextMessage(dnn, null, sms_for_d, null, null);
                                                    Message.message(Grant_appointment.this, "Sms is sent");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Message.message(Grant_appointment.this, "Sms is not sent");
                                                }
                                            } else {
                                                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                                            }
                                        }
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                                    }
                                    finish();
                                } else {
                                    Message.message(Grant_appointment.this, "Not Approved");
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Message.message(Grant_appointment.this, "Permission granted");
            }else{
                Message.message(Grant_appointment.this, "Permission denied");

            }
        }
    }
}