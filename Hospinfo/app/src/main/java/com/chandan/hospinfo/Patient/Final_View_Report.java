package com.chandan.hospinfo.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandan.hospinfo.DatabaseHelper;
import com.chandan.hospinfo.R;

public class Final_View_Report extends AppCompatActivity {
    String username,password;
    byte[] im;
    String report;
    ImageView repoimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_view_report);
        getSupportActionBar().setTitle("Report");

        DatabaseHelper dbh = new DatabaseHelper(this);

        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        report = bb.getString("report");
        im=bb.getByteArray("im");
        TextView final_report = (TextView) findViewById(R.id.tv_report);
        repoimg=(ImageView)findViewById(R.id.repo_img);
        final_report.setText(report);

        Bitmap bitmap= BitmapFactory.decodeByteArray(im,0,im.length);
        repoimg.setImageBitmap(bitmap);




    }

}