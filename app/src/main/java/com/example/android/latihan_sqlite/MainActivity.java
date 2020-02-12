package com.example.android.latihan_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView _txResult;
    CrudHelper _crudHelper;
    DatabaseHelper _dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _txResult = findViewById(R.id.txResult);
        _dbHelper = new DatabaseHelper(this,"dbBimbingan",null,2);
        _crudHelper = new CrudHelper(_dbHelper);
        _crudHelper.clearTable(DatabaseContract.TbJurusan.TABLE_NAME);
        _crudHelper.clearTable(DatabaseContract.TbDosen.TABLE_NAME);
        _crudHelper.clearTable(DatabaseContract.TbMahasiswa.TABLE_NAME);

        ContentValues jurusan1 = new ContentValues();
        ContentValues jurusan2 = new ContentValues();
        ContentValues Dosen1= new ContentValues();
        ContentValues Dosen2= new ContentValues();
        ContentValues mhs1= new ContentValues();
        ContentValues mhs2= new ContentValues();
        ContentValues mhs3= new ContentValues();
        ContentValues mhs4= new ContentValues();
        ContentValues mhsArr[] = {mhs3,mhs4};

        jurusan1.put(DatabaseContract.TbJurusan.COLUMN_NAME_JURUSAN,"TIF");
        jurusan2.put(DatabaseContract.TbJurusan.COLUMN_NAME_JURUSAN,"SI");

        Dosen1.put("nama","akbar");
        Dosen1.put("email","muhammad.aminul@ub.ac.id");
        Dosen1.put("id_jurusan", "1");

        Dosen2.put("nama","afi");
        Dosen2.put("email","tri.afi@ub.ac.id");
        Dosen2.put("id_jurusan", "2");

        mhs1.put(DatabaseContract.TbMahasiswa.COLUMN_NAME_NAMA,"mhs1");
        mhs1.put("email","mhs1@ub.ac.id");
        mhs1.put("id_dosenpa","1");
        mhs1.put("id_jurusan", "1");

        mhs2.put(DatabaseContract.TbMahasiswa.COLUMN_NAME_NAMA,"mhs2");
        mhs2.put("email","mhs2@ub.ac.id");
        mhs2.put("id_dosenpa","2");
        mhs2.put("id_jurusan", "2");

        mhs3.put(DatabaseContract.TbMahasiswa.COLUMN_NAME_NAMA,"mhs3");
        mhs3.put("email","mhs3@ub.ac.id");
        mhs3.put("id_dosenpa","1");
        mhs3.put("id_jurusan", "1");

        mhs4.put(DatabaseContract.TbMahasiswa.COLUMN_NAME_NAMA,"mhs3");
        mhs4.put("email","mhs3@ub.ac.id");
        mhs4.put("id_dosenpa","2");
        mhs4.put("id_jurusan", "2");

        _crudHelper.insertJurusan(jurusan1);
        _crudHelper.insertJurusan(jurusan2);
        _crudHelper.insertDosen(Dosen1);
        _crudHelper.insertDosen(Dosen2);
        _crudHelper.insertMahasiswa(mhs1);
        _crudHelper.insertMahasiswa(mhs2);

        _txResult.setText("select"+ System.getProperty ("line.separator"));

        _txResult.append(_crudHelper.getDosen(null).get(0).get("nama")+
                        " "+_crudHelper.getDosen(null).get(0).get("id")+
                        " "+_crudHelper.getDosen(null).get(0).get("email")+
                        " "+_crudHelper.getDosen(null).get(0).get("jurusan")+
                        System.getProperty ("line.separator"));

        _txResult.append(_crudHelper.getMahasiswa(null).get(0).get("nama")+
                        " "+_crudHelper.getMahasiswa(null).get(0).get("dosenPa")+
                        " "+_crudHelper.getMahasiswa(null).get(0).get("jurusan")+
                        System.getProperty ("line.separator"));

        mhs1.put(DatabaseContract.TbMahasiswa.COLUMN_NAME_NAMA,"mhsSatu");
        mhs1.put("id_dosenpa","2");
        mhs1.put("id_jurusan", "2");
        _crudHelper.updateMahasiswa("1",mhs1);

        Dosen2.put("nama","TriAfi");
        Dosen2.put("id_jurusan", "1");
        _crudHelper.updateDosen("2",Dosen2);

        _txResult.append("select after Update"+System.getProperty ("line.separator"));

        _txResult.append(_crudHelper.getDosen(null).get(1).get("nama")+
                        " "+_crudHelper.getDosen(null).get(1).get("id")+
                        " "+_crudHelper.getDosen(null).get(1).get("email")+
                        " "+_crudHelper.getDosen(null).get(1).get("jurusan")+
                        System.getProperty ("line.separator"));

        _txResult.append(_crudHelper.getMahasiswa(null).get(0).get("nama")+
                        " "+_crudHelper.getMahasiswa(null).get(0).get("dosenPa")+
                        " "+_crudHelper.getMahasiswa(null).get(0).get("jurusan"));

        _txResult.append(System.getProperty("line.separator"));
    }

    @Override
    protected void onDestroy() {
        _dbHelper.close();
        super.onDestroy();
    }
}
