package com.techroof.reseau.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Cart.db";
    public static final String TABLE_NAME = "PRODUCTS";
    public static final String COL_2 = "PID";
    public static final String COL_3 = "NAME";
    public static final String COL_4 = "PRICE";
    public static final String COL_5 = "QTY";
    public static final String COL_6 = "IMAGE";
    public static final String COL_7 = "CATEGORY";
    public static final String COL_8 = "CREATOR";
    public static final String COL_9 = "STATUS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 30);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT," + COL_5 + " TEXT," + COL_6 + " TEXT," + COL_7 + " TEXT,"+COL_8+" TEXT,"+COL_9+" TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String pID, String name, String price,
                              String qty, String image, String category,String creator,String status) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, pID);
        contentValues.put(COL_3, name);
        contentValues.put(COL_4, price);
        contentValues.put(COL_5, qty);
        contentValues.put(COL_6, image);
        contentValues.put(COL_7, category);
        contentValues.put(COL_8, creator);
        contentValues.put(COL_9, status);

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

    public Cursor getCartProducts() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + ";", null);
        return res;
    }

    public Integer deleteProduct(String pId) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"PID = ?",new String[]{pId});
    }

    public boolean updateData(String pID, String name, String price,
                              String qty, String image, String category,String creator,String status) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, pID);
        contentValues.put(COL_3, name);
        contentValues.put(COL_4, price);
        contentValues.put(COL_5, qty);
        contentValues.put(COL_6, image);
        contentValues.put(COL_7, category);
        contentValues.put(COL_8, creator);
        contentValues.put(COL_9, status);

        db.update(TABLE_NAME, contentValues, "PID = ?", new String[]{pID});
        return true;
    }

    public void emptyCart() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);
    }
}