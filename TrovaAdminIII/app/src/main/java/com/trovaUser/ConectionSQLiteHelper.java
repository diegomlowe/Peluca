package com.trovaUser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trovaUser.utils.Utils;

public class ConectionSQLiteHelper extends SQLiteOpenHelper {


    public ConectionSQLiteHelper(Context context,  String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Utils.CREAR_TABLA_PELUCAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {

        db.execSQL("DROP TABLE IF EXISTS peluquerias");
        onCreate(db);

    }
}
