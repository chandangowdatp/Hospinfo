package com.chandan.hospinfo.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;

public class Apply extends AppCompatActivity {


    String username,user_type, password, d_username, d_password, problem, slot,slt,wk,dn,pn,sms_for_p,sms_for_d;
    TextView name;
    byte[] rep;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply);
        getSupportActionBar().setTitle("Appointment");

        Bundle b = getIntent().getExtras();
        username = b.getString("username");
        password = b.getString("password");
        user_type=b.getString("user_type");

        d_username = b.getString("d_username");
        d_password = b.getString("d_password");
        slot = b.getString("slot");
        slt = b.getString("slt");
        wk=b.getString("wk");


        name = (TextView) findViewById(R.id.tv_d_name);
        et = (EditText) findViewById(R.id.et_problem);


        DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor y = dbh.checkduplicates_in_user_credentials(d_username, d_password, getResources().getString(R.string.user_credentials));

        if (y.moveToFirst()) {
            name.setText("Dr.  " + y.getString(1) + " " + y.getString(2));
            rep=y.getBlob(13);


        }
    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.b_apply:

                problem = et.getText().toString();
                DatabaseHelper dbh = new DatabaseHelper(this);


                if (problem.length() == 0) {
                    Message.message(Apply.this, "Please Enter You Problem");
                } else {
                    DatabaseHelper dbh2 = new DatabaseHelper(this);

                    boolean b = dbh2.insert_doctor_patient(username, password, d_username, d_password, "W", problem, " ",rep,wk,slt);
                    if (b) {
                        Message.message(Apply.this, "Your Application has been Sent");
                        Intent i;
                        Bundle bb = new Bundle();
                        bb.putString("username",username);
                        bb.putString("password",password);
                        bb.putString("user_type",user_type);

                        i=new Intent(Apply.this,Patient.class);
                        i.putExtras(bb);
                        startActivity(i);
                    } else {
                        Message.message(Apply.this, "Error...Please Try Again");
                    }
                }
                break;
        }
    }
}