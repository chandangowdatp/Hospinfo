package com.chandan.hospinfo.Doctor;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.Message;
import com.chandan.hospinfo.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Write_Report extends AppCompatActivity {

    String username, password, user_type, p_username, p_password, problem,problem2,wk, rp,  pslot;

    EditText etr;
    TextView pro;
    Button b_submit;
    ImageView im;
    byte[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_report);
        getSupportActionBar().setTitle("Report");


        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");
        p_username = bb.getString("p_username");
        p_password = bb.getString("p_password");
        problem = bb.getString("problem");

        pro = (TextView) findViewById(R.id.tv_problem);
        etr = (EditText) findViewById(R.id.et_report);
        im=(ImageView)findViewById(R.id.d_reportimage);
        b_submit = (Button) findViewById(R.id.b_submit_report);
        problem2=problem;

        pro.setText(problem);



        final DatabaseHelper db = new DatabaseHelper(this);
       Cursor x = db.checkduplicates_in_user_credentials(p_username, username,problem2);

        if(x.moveToFirst()) {
             wk = x.getString(8);
             pslot=x.getString(9);
        }else{
            Message.message(Write_Report.this,"problem=");
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


        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rp = etr.getText().toString();
                img= ImageToByte(im);

                boolean b = db.update_doctor_patient(p_username, p_password, username, password, "F", problem, rp,img,wk,pslot);

                if (b) {
                    Message.message(Write_Report.this, "Report uploaded successfully");
                    Intent i;
                    Bundle bb = new Bundle();
                    bb.putString("username",username);
                    bb.putString("password",password);
                    bb.putString("user_type",user_type);
                    i = new Intent(Write_Report.this,Doctor.class);
                    i.putExtras(bb);
                    startActivity(i);
                } else {
                    Message.message(Write_Report.this, "Report Not Uploaded, Try again");
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