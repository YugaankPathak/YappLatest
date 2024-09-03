package com.example.userloginsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper {
    private static final String dbname="Yappin.db";

    public database(@Nullable Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS apparel");
        String q= "CREATE TABLE apparel (id INTEGER PRIMARY KEY AUTOINCREMENT, ownership INTEGER, color TEXT, material TEXT, upper_lower TEXT, type TEXT, image BLOB)";
        db.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS apparel");
        onCreate(db);
    }
}