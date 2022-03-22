package com.chandan.hospinfo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Update extends AppCompatActivity {

    EditText fname, lname, age, dd, yy, city, pincode, mobno, uname, password;
    String tmp, fnames, lnames, ages, genders, bgroups, dobs, dds, yys, mms, citys, pincodes, mobnos, unames, passwords, utypes, username, pass, date, month, year;
    Button register;
    Spinner mm, gender, bgroup, usertype;
    CircleImageView im;
    byte[] img1,img2;

    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        getSupportActionBar().setTitle("Update");


        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        pass = bb.getString("password");


        //DEFINING ALL VIEWS
        fname = (EditText) findViewById(R.id.etfname);
        lname = (EditText) findViewById(R.id.etlname);
        age = (EditText) findViewById(R.id.etage);
        dd = (EditText) findViewById(R.id.etdd);
        yy = (EditText) findViewById(R.id.etyy);
        city = (EditText) findViewById(R.id.etcity);
        pincode = (EditText) findViewById(R.id.etpin);
        mobno = (EditText) findViewById(R.id.etmobile);
        uname = (EditText) findViewById(R.id.etusername);
        password = (EditText) findViewById(R.id.etpassword);
        register = (Button) findViewById(R.id.bregister);
        mm = (Spinner) findViewById(R.id.spinnermonth);
        gender = (Spinner) findViewById(R.id.spinnergender);
        usertype = (Spinner) findViewById(R.id.spinnerusertype);
        bgroup = (Spinner) findViewById(R.id.spinnerbgroup);
        im=(CircleImageView)findViewById(R.id.r_image) ;
        dbh = new DatabaseHelper(this);

        List<String> category = new ArrayList<>();
        category.add("Patient");
        category.add("Doctor");
        category.add("Admin");

        List<String> genderc = new ArrayList<>();
        genderc.add("Male");
        genderc.add("Female");

        List<String> bgroupc = new ArrayList<>();
        bgroupc.add("A+");
        bgroupc.add("A-");
        bgroupc.add("B+");
        bgroupc.add("B-");
        bgroupc.add("O+");
        bgroupc.add("O-");
        bgroupc.add("AB+");
        bgroupc.add("AB-");

        List<String> months = new ArrayList<>();
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

        ArrayAdapter<String> acat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        ArrayAdapter<String> amonth = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        ArrayAdapter<String> abgroup = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bgroupc);
        ArrayAdapter<String> agender = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderc);

        acat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        abgroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        usertype.setAdapter(acat);
        mm.setAdapter(amonth);
        gender.setAdapter(agender);
        bgroup.setAdapter(abgroup);

        DatabaseHelper dh = new DatabaseHelper(this);
        Cursor z = dh.checkduplicates_in_user_credentials(username, pass, getResources().getString(R.string.user_credentials));


        z.moveToFirst();
        fname.setText(z.getString(1));
        lname.setText(z.getString(2));
        age.setText(z.getString(3));
        city.setText(z.getString(8));
        pincode.setText(z.getString(9));
        mobno.setText(z.getString(10));
        uname.setText(z.getString(12));
        password.setText(z.getString(11));
        img1=z.getBlob(13);

        Bitmap bitmap= BitmapFactory.decodeByteArray(img1,0,img1.length);
        im.setImageBitmap(bitmap);


        //SET DOB
        tmp = z.getString(5);
        date = tmp.substring(0, 2);
        month = tmp.substring(3, 6);
        year = tmp.substring(7);

        dd.setText(date);
        yy.setText(year);

        //SET SPINNERS
        tmp = z.getString(4);
        if (tmp.equals("Male")) {
            gender.setSelection(0);
        } else {
            gender.setSelection(1);
        }

        if (month.equals("Jan")) {
            mm.setSelection(0);
        } else if (month.equals("Feb")) {
            mm.setSelection(1);
        } else if (month.equals("Mar")) {
            mm.setSelection(2);
        } else if (month.equals("Apr")) {
            mm.setSelection(3);
        } else if (month.equals("May")) {
            mm.setSelection(4);
        } else if (month.equals("June")) {
            mm.setSelection(5);
        } else if (month.equals("July")) {
            mm.setSelection(6);
        } else if (month.equals("Aug")) {
            mm.setSelection(7);
        } else if (month.equals("Sep")) {
            mm.setSelection(8);
        } else if (month.equals("Oct")) {
            mm.setSelection(9);
        } else if (month.equals("Nov")) {
            mm.setSelection(10);
        } else {
            mm.setSelection(11);
        }

        tmp = z.getString(6);

        if (tmp.equals("A+")) {
            bgroup.setSelection(0);
        } else if (tmp.equals("A-")) {
            bgroup.setSelection(1);
        } else if (tmp.equals("B+")) {
            bgroup.setSelection(2);
        } else if (tmp.equals("B-")) {
            bgroup.setSelection(3);
        } else if (tmp.equals("O+")) {
            bgroup.setSelection(4);
        } else if (tmp.equals("O-")) {
            bgroup.setSelection(5);
        } else if (tmp.equals("AB+")) {
            bgroup.setSelection(6);
        } else if (tmp.equals("AB-")) {
            bgroup.setSelection(7);
        }

        tmp = z.getString(7);

        if (tmp.equals("Patient")) {
            usertype.setSelection(0);
        } else if (tmp.equals("Doctor")) {
            usertype.setSelection(1);
        } else {
            usertype.setSelection(2);
        }

        im.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick=true;
                if(pick==true){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else PickImage();


                }else{
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else PickImage();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnames = lname.getText().toString();
                fnames = fname.getText().toString();
                ages = age.getText().toString();
                dds = dd.getText().toString();
                yys = yy.getText().toString();
                citys = city.getText().toString();
                pincodes = pincode.getText().toString();
                unames = uname.getText().toString();
                passwords = password.getText().toString();
                mobnos = mobno.getText().toString();
                mms = mm.getSelectedItem().toString();
                genders = gender.getSelectedItem().toString();
                bgroups = bgroup.getSelectedItem().toString();
                utypes = usertype.getSelectedItem().toString();
                img2= ImageToByte(im);

                if (fnames.equals("") || lnames.equals("") || ages.equals("") || dds.equals("") ||
                        yys.equals("") || citys.equals("") || pincodes.equals("") || unames.equals("") ||
                        passwords.equals("") || mobnos.equals("")) {
                    Message.message(Update.this, "Please Fill in all your Details");
                }else if(!mobnos.matches("[0-9]{10}")){
                    Message.message(Update.this, "Please Fill Valid Mobile Number");
                }else if(passwords.length()<5){
                    Message.message(Update.this, "Minimum 5 digit password is required ");
                } else {
                    //SETUP DATABASE QUERY
                    if (dds.length() == 1)
                        dds = "0" + dds;
                    dobs = dds + " " + mms + " " + yys;

                    boolean b = dbh.update_user_credentials(username, pass, fnames, lnames, ages, dobs, citys, pincodes, unames,passwords,mobnos,utypes,genders,bgroups,  img2);

                    if (b) {
                        Message.message(Update.this, "User Info Updated Sucessfully");
                    } else {
                        Message.message(Update.this, "Error Occured While Updation");
                    }
                    finish();
                }
            }

        });
    }
    private byte[] ImageToByte(ImageView im) {
        Bitmap bitmap=((BitmapDrawable) im.getDrawable()).getBitmap() ;
        ByteArrayOutputStream streem=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,streem);
        byte[] bytes=streem.toByteArray();
        return bytes;
    }

    private void PickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream stream=getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap= BitmapFactory.decodeStream(stream);
                    im.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        },100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
        },100);
    }

    private boolean checkStoragePermission() {
        boolean res1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res1;
    }

    private boolean checkCameraPermission() {
        boolean res1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        boolean res2= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

}