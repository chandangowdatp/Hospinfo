package com.chandan.hospinfo.Desktop_Admin;

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

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Desktop_Admin.Doctor_related.Specialization;
import com.chandan.hospinfo.Doctor.Doctor;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.Patient.Appointment;
import com.chandan.hospinfo.Patient.Patient;
import com.chandan.hospinfo.R;
import com.chandan.hospinfo.Register;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Users extends AppCompatActivity {

    EditText fname, lname, age, dd, yy, city, pincode, mobno, uname, password;
    String fnames, lnames, ages, genders, bgroups, dobs, dds, yys, mms, citys, pincodes, mobnos, unames, passwords, utypes;
    Button register;
    Spinner usertype, mm, gender, bgroup;
    CircleImageView im;
    String user_type_a,password_a,username_a;
    byte[] img;

    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_users);
        getSupportActionBar().setTitle("Add User");



        Bundle bb = getIntent().getExtras();
        username_a = bb.getString("username");
        password_a= bb.getString("password");
        user_type_a = bb.getString("user_type");

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
        usertype = (Spinner) findViewById(R.id.spinnerusertype);
        mm = (Spinner) findViewById(R.id.spinnermonth);
        gender = (Spinner) findViewById(R.id.spinnergender);
        bgroup = (Spinner) findViewById(R.id.spinnerbgroup);
        im=(CircleImageView) findViewById(R.id.r_image) ;
        dbh = new DatabaseHelper(this);

        //SET UP THE SPINNER DROOPDOWN
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
        gender.setAdapter(abgroup);
        bgroup.setAdapter(agender);

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
                utypes = usertype.getSelectedItem().toString();
                mms = mm.getSelectedItem().toString();
                bgroups = gender.getSelectedItem().toString();
                genders = bgroup.getSelectedItem().toString();

                img= ImageToByte(im);

                if (fnames.equals("") || lnames.equals("") || ages.equals("") || dds.equals("") ||
                        yys.equals("") || citys.equals("") || pincodes.equals("") || unames.equals("") ||
                        passwords.equals("") || mobnos.equals("")) {
                    Message.message(Add_Users.this, "Please Fill in all your Details");
                }else if(!mobnos.matches("[0-9]{10}")){
                    Message.message(Add_Users.this, "Please Fill Valid Mobile Number");
                }else if(passwords.length()<5){
                    Message.message(Add_Users.this, "Minimum 5 digit password is required ");
                } else {

                    //CHECK WHETHER THE ENTRY ALREADY EXISTS
                    Cursor y = dbh.checkduplicates_in_user_credentials(unames, passwords, getResources().getString(R.string.user_credentials));
                    if (y.moveToFirst()) {
                        Message.message(Add_Users.this, "User Already Exists");
                        Message.message(Add_Users.this, "Login With Your Username and Password");
                        finish();
                    } else {
                        //SETUP DATABASE QUERY
                        if (dds.length() == 1)
                            dds = "0" + dds;
                        dobs = dds + " " + mms + " " + yys;

                        boolean b = dbh.insert_user_credentials(fnames, lnames, ages, dobs, citys, pincodes, unames, passwords, mobnos, utypes, genders, bgroups,img);
                        if (b) {
                            Intent i;
                            Bundle bb = new Bundle();
                            bb.putString("username", unames);
                            bb.putString("password", passwords);
                            bb.putString("user_type", utypes);
                            bb.putString("username_a",username_a);
                            bb.putString("password_a",password_a);
                            bb.putString("user_type_a",user_type_a);

                            if (utypes.equals("Patient")) {
                                finish();
                            } else if (utypes.equals("Doctor")) {
                                i = new Intent(Add_Users.this, Specialization.class);
                                i.putExtras(bb);
                                startActivity(i);
                            } else {
                                finish();
                            }


                        }
                    }
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