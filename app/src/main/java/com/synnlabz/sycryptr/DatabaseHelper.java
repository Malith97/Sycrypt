package com.synnlabz.sycryptr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.widget.Toast;

import java.util.ArrayList;

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
        String table2 = "CREATE TABLE "+TABLE2+"('id' INTEGER PRIMARY KEY AUTOINCREMENT,'ac_name' TEXT,'ac_username' TEXT,'ac_password' TEXT,'ac_type' INTEGER,'ac_weblink' TEXT,'ac_timestamp' LONG)";
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

    public long insertInfo(String accountname , String accountusername , String accountpassword , int accounttype , String accountweb, String timestamp){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ac_name", accountname);
        contentValues.put("ac_username",accountusername);
        contentValues.put("ac_password",accountpassword);
        contentValues.put("ac_type",accounttype);
        contentValues.put("ac_weblink",accountweb);
        contentValues.put("ac_timestamp",timestamp);

        long id = sqLiteDatabase.insert(TABLE2,null,contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public ArrayList<Model> getAllData(){  //String orderBy within brackets
        ArrayList<Model> arrayList = new ArrayList<>();
        //String selectQuery = "SELECT * FROM ITEMS";
        String selectQuery = "SELECT  * FROM " + TABLE2;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        Model model;

        if (cursor.moveToFirst()){
            do {
                model = new Model();

                model.setId(cursor.getLong(cursor.getColumnIndex("id")));
                model.setAccountname(cursor.getString(cursor.getColumnIndex("ac_name")));
                model.setAccountusername(cursor.getString(cursor.getColumnIndex("ac_username")));
                model.setAccountpassword(cursor.getString(cursor.getColumnIndex("ac_password")));
                arrayList.add(model);

            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return arrayList;
    }

    public ArrayList<Model> getFilterData(int type){  //String orderBy within brackets
        ArrayList<Model> arrayList = new ArrayList<>();
        //String selectQuery = "SELECT * FROM ITEMS";
        String selectQuery = "SELECT  * FROM " + TABLE2 + " WHERE ac_type="+type;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        Model model;

        if (cursor.moveToFirst()){
            do {
                model = new Model();

                model.setId(cursor.getLong(cursor.getColumnIndex("id")));
                model.setAccountname(cursor.getString(cursor.getColumnIndex("ac_name")));
                model.setAccountusername(cursor.getString(cursor.getColumnIndex("ac_username")));
                model.setAccountpassword(cursor.getString(cursor.getColumnIndex("ac_password")));
                arrayList.add(model);

            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return arrayList;
    }

    public boolean updatePassword(String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE ACCOUNT SET password ="+password+" WHERE id=1");
        return true;
    }

    public Model getModel(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE2 + " WHERE id="+ id;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        Model receivedAccount = new Model();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedAccount.setAccountname(cursor.getString(cursor.getColumnIndex("ac_name")));
            receivedAccount.setAccountusername(cursor.getString(cursor.getColumnIndex("ac_username")));
            receivedAccount.setAccountpassword(cursor.getString(cursor.getColumnIndex("ac_password")));
            receivedAccount.setAccountlink(cursor.getString(cursor.getColumnIndex("ac_weblink")));
            receivedAccount.setUpdatetimestamp(cursor.getLong(cursor.getColumnIndex("ac_timestamp")));
            receivedAccount.setAccounttype(cursor.getInt(cursor.getColumnIndex("ac_type")));
        }
        return receivedAccount;
    }

    public String checkpassword(String password){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select password from ACCOUNT where password=?",new String[]{password});
        String temp = null;
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                temp = cursor.getString(0);
            }
        }
        return temp;
    }

    public void updateAccountRecord(long accountId, Context context, Model updateAccount) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE  "+TABLE2+" SET ac_name ='"+ updateAccount.getAccountname() + "', ac_username ='" + updateAccount.getAccountusername()+ "', ac_password ='"+ updateAccount.getAccountpassword() + "', ac_weblink ='"+ updateAccount.getAccountlink() + "', ac_type ='"+ updateAccount.getAccounttype() + "', ac_timestamp ='"+ updateAccount.getUpdatetimestamp() +"'  WHERE id='" + accountId + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
    }

    public void deleteAccountRecord(long accountId, Context context) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+TABLE2+" WHERE id='"+accountId+"'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }
}
