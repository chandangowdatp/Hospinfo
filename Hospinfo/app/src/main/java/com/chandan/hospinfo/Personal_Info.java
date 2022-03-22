package com.chandan.hospinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Personal_Info extends AppCompatActivity {

    String username,password,user_type;
    DatabaseHelper db;
    CircleImageView img;
    byte[] im;
    TextView name,age,gender,dob,bgroup,utype,city,pincode,mobno,uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        db = new DatabaseHelper(this);
        getSupportActionBar().setTitle("Personal Information");


        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");

        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        gender = (TextView) findViewById(R.id.gender);
        dob = (TextView) findViewById(R.id.dob);
        bgroup = (TextView) findViewById(R.id.bgroup);
        utype = (TextView) findViewById(R.id.utype);
        city = (TextView) findViewById(R.id.city);
        pincode = (TextView) findViewById(R.id.pincode);
        mobno = (TextView) findViewById(R.id.tv_mno);
        uname = (TextView) findViewById(R.id.username);
        img=(CircleImageView)findViewById(R.id.r_image) ;

        Cursor y = db.checkduplicates_in_user_credentials(username, password, getResources().getString(R.string.user_credentials));

        if (y.moveToFirst()) {
            String name1 = y.getString(1);
            String name2 = y.getString(2);

            name.setText(name1+" "+name2);
            age.setText(y.getString(3));
            gender.setText(y.getString(4));
            dob.setText(y.getString(5));
            bgroup.setText(y.getString(6));
            utype.setText(y.getString(7));
            city.setText(y.getString(8));
            pincode.setText(y.getString(9));
            mobno.setText(y.getString(10));
            uname.setText(y.getString(12));
            im=y.getBlob(13);

            Bitmap bitmap= BitmapFactory.decodeByteArray(im,0,im.length);
            img.setImageBitmap(bitmap);
        }
    }

    public void update(View view){

        Intent i;
        Bundle b = new Bundle();
        b.putString("username",username);
        b.putString("password",password);
        b.putString("user_type",user_type);

        i = new Intent(Personal_Info.this, Update.class);
        i.putExtras(b);
        startActivity(i);
        finish();
    }
}