package com.synnlabz.sycryptr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Sycrypter.db";

    private static final String TABLE1 = "ACCOUNT";
    private static final String TABLE2 = "ITEMS";

    DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = "CREATE TABLE "+TABLE1+"('id' INTEGER PRIMARY KEY,'password' TEXT NOT NULL)";
        String table2 = "CREATE TABLE "+TABLE2+"('id' INTEGER PRIMARY KEY,'ac_name' TEXT,'ac_username' TEXT,'ac_password' TEXT,'ac_weblink' TEXT)";
        db.execSQL(table1);
        db.execSQL(table2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE2);
        onCreate(db);
    }

    public boolean insert(String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("password",password);

        sqLiteDatabase.insert(TABLE1,null,contentValues);
        return true;
    }

    public boolean insert(String accountname , String accountusername , String accountpassword , String accountweblink){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("accountname",accountname);
        contentValues.put("accountusername",accountusername);
        contentValues.put("accountpassword",accountpassword);
        contentValues.put("accountweblink",accountweblink);

        sqLiteDatabase.insert(TABLE2,null,contentValues);
        return true;
    }

    public String checkpassword(String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select password from ACCOUNT where password=?",new String[]{password});
        String temp = null;
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                temp = cursor.getString(0);
            }
        }
        return temp;
    }
}
