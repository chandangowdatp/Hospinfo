package com.chandan.hospinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chandan.hospinfo.Patient.Apply;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Hospinfo_DATABASE.db";
    private static final String TABLE_NAME_USER = "USER_CREDENTIALS";
    private static final String TABLE_NAME_D_LEAVES = "DOCTOR_LEAVES";
    private static final String TABLE_NAME_D_SLOT = "DOCTOR_SLOT";
    private static final String TABLE_NAME_DOCTOR_PATIENT = "DOCTOR_PATIENT";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TABLE FOR USER CREDENTIAL
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME_USER + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name VARCHAR," +
                    "last_name VARCHAR," +
                    "age VARCHAR," +
                    "gender VARCHAR," +
                    "dob VARCHAR," +
                    "blood_group VARCHAR," +
                    "u_type VARCHAR," +
                    "city VARCHAR," +
                    "pincode VARCHAR," +
                    "mobile_number VARCHAR," +
                    "password VARCHAR," +
                    "username VARCHAR," +
                    "image BLOG);"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //*****************************TABLE FOR DOCTOR LEAVES**************************************
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME_D_LEAVES + " (" +
                    "username VARCHAR," +
                    "password VARCHAR," +
                    "user_type VARCHAR," +
                    "date_from VARCHAR," +
                    "date_to VARCHAR," +
                    "approval VARCHAR);"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //*****************************TABLE FOR DOCTOR SLOTS**************************************
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME_D_SLOT + " (" +
                    "username VARCHAR," +
                    "password VARCHAR," +
                    "specialization VARCHAR," +
                    "slot VARCHAR," +
                    "available VARCHAR," +
                    "obd VARCHAR);"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME_DOCTOR_PATIENT + " (" +
                    "p_username VARCHAR," +
                    "p_password VARCHAR," +
                    "d_username VARCHAR," +
                    "d_password VARCHAR," +
                    "granted VARCHAR," +
                    "problem VARCHAR," +
                    "report VARCHAR,"+
                    "reportimage BLOG,"+
                    "week VARCHAR,"+
                    "pslot VARCHAR);"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_D_LEAVES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_D_SLOT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DOCTOR_PATIENT);
        onCreate(db);
    }

    //*************************************USER CREDENTIALS TABLE* *********************************************************
    //CHECHK THAT THE REGISTERED USER ALREADY EXIST ******** AND RETURNS ALL FAVOURABLE VALUES

    //CURSUR RETRUN FUNCTION
    public Cursor checkduplicates_in_user_credentials(String user_name, String password, String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;

        if (table.equals("approve_leaves")) {
            res = db.rawQuery("select * from " + TABLE_NAME_D_LEAVES + " where username=?  and date_from=?", new String[]{user_name, password});
        } else if (table.equals("all_pending_leaves")) {
            res = db.rawQuery("select * from " + TABLE_NAME_D_LEAVES , new String[]{});
        } else if (table.equals(TABLE_NAME_D_SLOT)) {
            res = db.rawQuery("select * from " + TABLE_NAME_D_SLOT + " where username=? and password=?", new String[]{user_name, password});
        } else if (table.equals(TABLE_NAME_USER)) {
            res = db.rawQuery("select * from " + TABLE_NAME_USER + " where username=? and password=?", new String[]{user_name, password});
        } else if (table.equals(TABLE_NAME_DOCTOR_PATIENT)) {
            res = db.rawQuery("select * from " + TABLE_NAME_DOCTOR_PATIENT + " where d_username=? and d_password=?", new String[]{user_name, password});
        } else if (table.equals("get_all_doctors")) {
            res = db.rawQuery("select * from " + TABLE_NAME_USER, new String[]{});
        } else if (table.equals("all_doctor_slots")) {
            res = db.rawQuery("select * from " + TABLE_NAME_D_SLOT, new String[]{});
        } else if (table.equals("all_pending_appointment")) {
            res = db.rawQuery("select * from " + TABLE_NAME_DOCTOR_PATIENT, new String[]{});
        } else if (table.equals("patient_identify")) {
            res = db.rawQuery("select * from " + TABLE_NAME_DOCTOR_PATIENT + " where p_username=? and p_password=?", new String[]{user_name, password});
        } else if (table.equals("display_specialization")){
            res= db.rawQuery("select distinct specialization from " + TABLE_NAME_D_SLOT + " where slot=? and obd=?",new String[]{user_name, password});
        } else if (table.equals("display_doctors_to_apply")){
            res= db.rawQuery("select * from " + TABLE_NAME_D_SLOT + " where slot=? and obd=?",new String[]{user_name, password});
        }else{
            res = db.rawQuery("select * from " + TABLE_NAME_DOCTOR_PATIENT + " where p_username=? and d_username=? and problem=?", new String[]{user_name, password, table});
        }
        return res;
    }

    //INSERT INTO USER CREDENTIALS check duplication or alredy exists
    public boolean insert_user_credentials(String fnames, String lnames, String ages, String dobs, String citys, String pincodes, String unames, String passwords, String mobnos, String utypes, String genders, String bgroups, byte[] image) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", fnames);
        contentValues.put("last_name", lnames);
        contentValues.put("age", ages);
        contentValues.put("dob", dobs);
        contentValues.put("city", citys);
        contentValues.put("pincode", pincodes);
        contentValues.put("username", unames);
        contentValues.put("password", passwords);
        contentValues.put("mobile_number", mobnos);
        contentValues.put("u_type", utypes);
        contentValues.put("gender", genders);
        contentValues.put("blood_group", bgroups);
        contentValues.put("image", image);

        long l = db.insert(TABLE_NAME_USER, null, contentValues);

        if (l != -1) {
            Message.message(context, "new entry inserted");
            return true;
        } else {
            Message.message(context, "Registration Failed");
            return false;
        }
    }

    public boolean update_user_credentials(String ou, String op, String fnames, String lnames, String ages, String dobs, String citys, String pincodes, String unames, String passwords, String mobnos, String utypes, String genders, String bgroups, byte[] image) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", fnames);
        contentValues.put("last_name", lnames);
        contentValues.put("age", ages);
        contentValues.put("dob", dobs);
        contentValues.put("city", citys);
        contentValues.put("pincode", pincodes);
        contentValues.put("username", unames);
        contentValues.put("password", passwords);
        contentValues.put("mobile_number", mobnos);
        contentValues.put("u_type", utypes);
        contentValues.put("gender", genders);
        contentValues.put("blood_group", bgroups);
        contentValues.put("image", image);

        long l = db1.update(TABLE_NAME_USER, contentValues, "username=? and password=?", new String[]{ou, op});
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean delete_user_credentials(String ou, String op) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        long l = db1.delete(TABLE_NAME_USER, "username=? and password=?", new String[]{ou, op});

        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }

    //*************************************************DOCTOR LEAVES TABLE ********************************************************
    //insert leaves

    public boolean insert_leaves(String username, String password, String user_type, String dfrom, String dto, String approval) {

        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("user_type", user_type);
        contentValues.put("date_from", dfrom);
        contentValues.put("date_to", dto);
        contentValues.put("approval", approval);

        long l = db1.insert(TABLE_NAME_D_LEAVES, null, contentValues);

        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }
    public boolean update_leaves(String username, String password, String user_type, String dfrom, String dto, String approval) {

        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("user_type", user_type);
        contentValues.put("date_from", dfrom);
        contentValues.put("date_to", dto);
        contentValues.put("approval", approval);

        long l = db1.update(TABLE_NAME_D_LEAVES,contentValues, "username=? and password=? and date_from=? and date_to=? " , new String[]{username,  password,dfrom,dto });
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }
    public boolean delete_leaves(String ou, String op) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        long l = db1.delete(TABLE_NAME_D_LEAVES, "username=? and password=?", new String[]{ou, op});

        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }

    //**********************************************DOCTOR SLOT TABLE ***********************************************************
    //insert slots

    public boolean insert_slot(String username, String password, String specialization, String slot, String available, String obd) {

        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("specialization", specialization);
        contentValues.put("slot", slot);
        contentValues.put("available", available);
        contentValues.put("obd", obd);

        long l = db1.insert(TABLE_NAME_D_SLOT, null, contentValues);
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean update_slot(String username, String password, String specialization, String slot, String available, String obd) {

        SQLiteDatabase db1 = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("specialization", specialization);
        contentValues.put("slot", slot);
        contentValues.put("available", available);
        contentValues.put("obd", obd);

        long l = db1.update(TABLE_NAME_D_SLOT, contentValues, "username=? and password=?", new String[]{username, password});
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }
    public boolean delete_slot(String ou, String op) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        long l = db1.delete(TABLE_NAME_D_SLOT, "username=? and password=?", new String[]{ou, op});

        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }
    //**********************************************Doctor patient TABLE ***********************************************************
    //insert appointment

    public boolean insert_doctor_patient(String p_username, String p_password, String d_username, String d_password, String granted, String problem, String report,byte[] rimage,String week,String pslot) {

        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("p_username", p_username);
        contentValues.put("p_password", p_password);
        contentValues.put("d_username", d_username);
        contentValues.put("d_password", d_password);
        contentValues.put("granted", granted);
        contentValues.put("problem", problem);
        contentValues.put("report", report);
        contentValues.put("reportimage",rimage);
        contentValues.put("week", week);
        contentValues.put("pslot", pslot);

        long l = db1.insert(TABLE_NAME_DOCTOR_PATIENT, null, contentValues);
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }

    //update appointment

    public boolean update_doctor_patient(String p_username, String p_password, String d_username, String d_password, String granted, String problem, String report,byte[] rimage,String week,String pslot) {

        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("p_username", p_username);
        contentValues.put("p_password", p_password);
        contentValues.put("d_username", d_username);
        contentValues.put("d_password", d_password);
        contentValues.put("granted", granted);
        contentValues.put("problem", problem);
        contentValues.put("report", report);
        contentValues.put("reportimage",rimage);
        contentValues.put("week", week);
        contentValues.put("pslot", pslot);

        long l = db1.update(TABLE_NAME_DOCTOR_PATIENT, contentValues, "p_username=? and p_password=? and problem=?", new String[]{p_username, p_password, problem});
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean delete_doctor_patient(String pn) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        long l = db1.delete(TABLE_NAME_DOCTOR_PATIENT, "p_username=? ", new String[]{pn});

        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }

}