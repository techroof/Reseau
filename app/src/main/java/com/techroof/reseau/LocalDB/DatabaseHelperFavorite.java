package com.techroof.reseau.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperFavorite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Favorite.db";
    public static final String TABLE_NAME = "PRODUCTS";
    public static final String COL_2 = "PID";
    public static final String COL_3 = "NAME";
    public static final String COL_4 = "PRICE";
    public static final String COL_5 = "SALEPRICE";
    public static final String COL_6 = "DESCRIPTION";
    public static final String COL_7 = "BRAND";
    public static final String COL_8 = "IMAGE";

    public DatabaseHelperFavorite(Context context) {
        super(context, DATABASE_NAME, null, 30);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String pID) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, pID);


        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getProductId() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + COL_2 + " from " + TABLE_NAME + ";", null);
        return res;
    }

    public boolean updateData(String pID, String name, String price, String description,
                              String qty, String brand, String image, String category, String color) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, pID);

        db.update(TABLE_NAME, contentValues, "PID = ?", new String[]{pID});
        return true;
    }

    public Integer deleteProduct(String pId) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"PID = ?",new String[]{pId});
    }

    public Cursor getFavoriteProducts() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + ";", null);
        return res;
    }

    public void emptyCart() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);
    }
}