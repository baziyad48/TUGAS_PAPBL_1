package com.example.android.latihan_sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;

public class CrudHelper {

    DatabaseHelper dbHelper;

    public CrudHelper(DatabaseHelper _dbHelper) {
        this.dbHelper = _dbHelper;
    }

    public long insertDosen(ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id = db.insert(DatabaseContract.TbDosen.TABLE_NAME, null, values);
        return _id;
    }

    public long insertMahasiswa(ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id = db.insert(DatabaseContract.TbMahasiswa.TABLE_NAME, null, values);
        return _id;
    }

    public long insertJurusan(ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id = db.insert(DatabaseContract.TbJurusan.TABLE_NAME, null, values);
        return  _id;
    }

    public long insertMahasiswaTransaction(ContentValues values[]) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long _id = 0;
        try {
            for (int i = 0; i < values.length; i++) {
                db.insert(DatabaseContract.TbMahasiswa.TABLE_NAME, null, values[i]);
            }
            db.setTransactionSuccessful();
            _id = 1;
        } finally {
            _id = 0;
            db.endTransaction();
        }
        return _id;
    }

    public ArrayList<HashMap<String, String>> getMahasiswa(@Nullable String _id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.TbMahasiswa.TABLE_NAME + "." + BaseColumns._ID,
                DatabaseContract.TbMahasiswa.TABLE_NAME + "." + DatabaseContract.TbMahasiswa.COLUMN_NAME_NAMA + " as NamaMhs",
                DatabaseContract.TbMahasiswa.TABLE_NAME+"."+DatabaseContract.TbMahasiswa.COLUMN_NAME_EMAIL,
                DatabaseContract.TbDosen.TABLE_NAME+"."+DatabaseContract.TbDosen.COLUMN_NAME_NAMA + " as NamaDosen",
                DatabaseContract.TbJurusan.TABLE_NAME+"."+DatabaseContract.TbJurusan.COLUMN_NAME_JURUSAN
        };

        String selection = null;
        String[] selectionArgs =null;

        if(_id!=null){
            selectionArgs = new String[]{_id};
            selection = DatabaseContract.TbMahasiswa.TABLE_NAME+"."+DatabaseContract.TbMahasiswa._ID + " = ?" ;
        }

        Cursor cursor = db.query(DatabaseContract.TbMahasiswa.TABLE_NAME + " INNER JOIN " + DatabaseContract.TbDosen.TABLE_NAME + " ON " + DatabaseContract.TbMahasiswa.TABLE_NAME + "." +DatabaseContract.TbMahasiswa.COLUMN_NAME_ID_DOSENPA + " = " +
                DatabaseContract.TbDosen.TABLE_NAME+"."+DatabaseContract.TbDosen._ID + " INNER JOIN " + DatabaseContract.TbJurusan.TABLE_NAME + " ON " + DatabaseContract.TbMahasiswa.TABLE_NAME+"."+DatabaseContract.TbMahasiswa.COLUMN_NAME_ID_JURUSAN + " = " + DatabaseContract.TbJurusan.TABLE_NAME+"."+DatabaseContract.TbJurusan._ID,
                projection, selection, selectionArgs, null, null, null);

        ArrayList <HashMap<String,String>> Mahasiswas = new ArrayList<HashMap<String,String>>();

        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbMahasiswa._ID));
            String nama = cursor.getString(cursor.getColumnIndexOrThrow("NamaMhs"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbMahasiswa.COLUMN_NAME_EMAIL));
            String dosenPa = cursor.getString(cursor.getColumnIndexOrThrow("NamaDosen"));
            String jurusan = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbJurusan.COLUMN_NAME_JURUSAN));

            HashMap<String,String> mahasiswa = new HashMap<String,String>();

            mahasiswa.put("id",id );
            mahasiswa.put("nama",nama );
            mahasiswa.put("email",email);
            mahasiswa.put("dosenPa",dosenPa);
            mahasiswa.put("jurusan", jurusan);
            Mahasiswas.add(mahasiswa);
        }
        cursor.close();
        return Mahasiswas;
    }

    public ArrayList<HashMap<String,String>> getDosen(@Nullable String _id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.TbDosen.TABLE_NAME + "." +BaseColumns._ID,
                DatabaseContract.TbDosen.TABLE_NAME + "." +DatabaseContract.TbDosen.COLUMN_NAME_NAMA,
                DatabaseContract.TbDosen.TABLE_NAME + "." +DatabaseContract.TbDosen.COLUMN_NAME_EMAIL,
                DatabaseContract.TbJurusan.TABLE_NAME + "."+DatabaseContract.TbJurusan.COLUMN_NAME_JURUSAN + " as NamaJurusan"
        };

        String selection = DatabaseContract.TbDosen.TABLE_NAME + "." +DatabaseContract.TbDosen.COLUMN_NAME_ID_JURUSAN+" = "+DatabaseContract.TbJurusan.TABLE_NAME+"."+DatabaseContract.TbJurusan._ID;;
        String[] selectionArgs =null;

        if(_id!=null) {
            selectionArgs = new String[]{_id};
            selection = DatabaseContract.TbDosen.TABLE_NAME+"."+DatabaseContract.TbDosen.COLUMN_NAME_ID_JURUSAN+" = "+
            DatabaseContract.TbJurusan.TABLE_NAME+"."+DatabaseContract.TbJurusan._ID + " AND " + DatabaseContract.TbDosen.TABLE_NAME+"."+BaseColumns._ID + " = ?";
        }

        String sortOrder = DatabaseContract.TbDosen.TABLE_NAME+"."+DatabaseContract.TbDosen.COLUMN_NAME_NAMA + " DESC";
        Cursor cursor = db.query(DatabaseContract.TbDosen.TABLE_NAME+" , "+ DatabaseContract.TbJurusan.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        ArrayList <HashMap<String,String>> Dosens = new ArrayList<HashMap<String,String>>();

        while(cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbDosen._ID));
            String nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbDosen.COLUMN_NAME_NAMA));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TbDosen.COLUMN_NAME_EMAIL));
            String jurusan = cursor.getString(cursor.getColumnIndexOrThrow("NamaJurusan"));

            HashMap<String,String> dosen = new HashMap<String,String>();
            dosen.put("id",id );
            dosen.put("nama",nama);
            dosen.put("email",email);
            dosen.put("jurusan", jurusan);
            Dosens.add(dosen);
        }

        cursor.close();
        return Dosens;
    }

    public int updateMahasiswa(String id,ContentValues cv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseContract.TbMahasiswa._ID + " = ?";
        String[] selectionArgs = { id };
        int updateRows = db.update(DatabaseContract.TbMahasiswa.TABLE_NAME,cv,selection, selectionArgs);
        return updateRows;
    }

    public int updateDosen(String id,ContentValues cv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseContract.TbDosen._ID + " = ?";
        String[] selectionArgs = { id };
        int updateRows = db.update(DatabaseContract.TbDosen.TABLE_NAME,cv,selection, selectionArgs);
        return updateRows;
    }

    public int updateJurusan(String id, ContentValues cv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseContract.TbJurusan._ID + " = ?";
        String[] selectionArgs = { id };
        int updateRows = db.update(DatabaseContract.TbJurusan.TABLE_NAME,cv,selection,selectionArgs);
        return updateRows;
    }

    public int deleteMahasiswa(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseContract.TbMahasiswa._ID+ " = ?";
        String[] selectionArgs = { id };
        int deletedRows = db.delete(DatabaseContract.TbMahasiswa.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }

    public int deleteDosen(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseContract.TbDosen._ID + " = ?";
        String[] selectionArgs = { id };
        int deletedRows = db.delete(DatabaseContract.TbDosen.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }

    public int deleteJurusan(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseContract.TbJurusan._ID + " = ?";
        String[] selectionArgs = { id };
        int deletedRows = db.delete(DatabaseContract.TbJurusan.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }

    public void clearTable(String TABLE_NAME) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
    }
}
