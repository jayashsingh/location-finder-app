package com.example.locationfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    //creates SQLIte database
    public Database(Context context) {
        super(context, "AddressData.db", null, 1);
    }

    //creates table with specified columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table AddressDetails(id INTEGER primary key autoincrement, address TEXT, longitude TEXT, latitude TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists AddressDetails");
    }

    //updates the database entry by switching the old address to the new address
    public Boolean updateDb(String oldAddress, String newAddress,String longitude, String latitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //adds the variables to the content values
        values.put("address", newAddress);
        values.put("longitude", longitude);
        values.put("latitude", latitude);

        //raw query of the database for old address
        Cursor c = db.rawQuery("Select * From AddressDetails Where address=?", new String[]{oldAddress});

        //checks if address with that name was found
        if (c.getCount()>0){
            //updates row with new data
            long result = db.update("AddressDetails", values, "address=?", new String[]{oldAddress});
            //returns false if it could not update
            if (result == -1) {
                return false;
            }
            //otherwise returns true
            else {
                return true;
            }
        }
        //returns false if no address was found
        else {
            return false;
        }
    }

    //deletes the database entry
    public Boolean deleteDb(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("Select * From AddressDetails Where address=?", new String[]{address});
        if (c.getCount()>0){
            //delete row with that address
            long result = db.delete("AddressDetails","address=?", new String[]{address});
            //returns false if it could not delete
            if (result == -1) {
                return false;
            }
            //otherwise returns true
            else {
                return true;
            }
        }
        //returns false if no address was found
        else {
            return false;
        }
    }

    //inserts new database entry
    public Boolean insertDb(String address, String longitude, String latitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //adds the variables to the content values
        values.put("address", address);
        values.put("longitude", longitude);
        values.put("latitude", latitude);
        //insert row with provided data
        long result = db.insert("AddressDetails",null, values);
        //returns false if it could not insert
        if (result == -1) {
            return false;
        }
        //otherwise returns true
        else {
            return true;
        }
    }

    //query's db for specific address
    public Cursor getQuery(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select distinct * from AddressDetails where address LIKE \"%"+ address + "%\"", null);
        return c;
    }
}
