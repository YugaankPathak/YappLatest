package com.example.userloginsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class dbConnect extends SQLiteOpenHelper {
    private static final String dbname = "findusermanager";
    private static final int dbVersion = 1;
    private static final String dbtable = "users";
    private static final String ID = "id";
    private static final String name = "name";
    private static final String emailaddress = "emailaddress";
    private static final String password = "password";
    private static final String gender = "gender";
    private static final String age = "age";
    private static final String bio = "bio";

    public dbConnect(@Nullable Context context) {
        super(context, dbname, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createUserTable = "CREATE TABLE " + dbtable + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                name + " TEXT, " +
                emailaddress + " TEXT UNIQUE, " +
                password + " TEXT, " +
                gender + " TEXT, " +
                age + " INTEGER, " +
                bio + " TEXT)";
        sqLiteDatabase.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbtable);
        onCreate(sqLiteDatabase);
    }

    public void addUser(users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(name, user.getName());
        values.put(emailaddress, user.getEmail());
        values.put(password, user.getPassword());
        values.put(gender, user.getGender());
        values.put(age, user.getAge());
        values.put(bio, user.getBio());
        db.insert(dbtable, null, values);
        db.close();
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + dbtable + " WHERE " + emailaddress + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkNameExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + dbtable + " WHERE " + this.name + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + password + " FROM " + dbtable + " WHERE " + emailaddress + " = ?";
        Cursor cursor = null;
        String passwordResult = null;

        try {
            cursor = db.rawQuery(query, new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                int passwordIndex = cursor.getColumnIndex(password);
                if (passwordIndex >= 0) {
                    passwordResult = cursor.getString(passwordIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return passwordResult;
    }

    public String getPasswordByNameAndEmail(String name, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + password + " FROM " + dbtable + " WHERE " + this.name + " = ? AND " + emailaddress + " = ?";
        Cursor cursor = null;
        String passwordResult = null;

        try {
            cursor = db.rawQuery(query, new String[]{name, email});
            if (cursor != null && cursor.moveToFirst()) {
                int passwordIndex = cursor.getColumnIndex(password);
                if (passwordIndex >= 0) {
                    passwordResult = cursor.getString(passwordIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return passwordResult;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + ID + " FROM " + dbtable + " WHERE " + emailaddress + " = ?";
        Cursor cursor = null;
        int userIdResult = -1;

        try {
            cursor = db.rawQuery(query, new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(ID);
                if (idIndex >= 0) {
                    userIdResult = cursor.getInt(idIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return userIdResult;
    }

    public void updateUser(users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(name, user.getName());
        values.put(emailaddress, user.getEmail());
        values.put(gender, user.getGender());
        values.put(age, user.getAge());
        values.put(bio, user.getBio());

        db.update(dbtable, values, ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public users getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        users user = null;

        String query = "SELECT * FROM " + dbtable + " WHERE " + ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(this.name));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(emailaddress));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(this.password));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(this.gender));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow(this.age));
                String bio = cursor.getString(cursor.getColumnIndexOrThrow(this.bio));

                user = new users(id, name, email, password, gender, age, bio);
            }
            cursor.close();
        }

        db.close();
        return user;
    }

    public users getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        users user = null;

        String query = "SELECT * FROM " + dbtable + " WHERE " + emailaddress + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(this.name));
                String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(emailaddress));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(this.gender));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow(this.age));
                String bio = cursor.getString(cursor.getColumnIndexOrThrow(this.bio));

                user = new users(name, userEmail, gender, age, bio);
            }
            cursor.close();
        }

        db.close();
        return user;
    }


    public String getGenderByEmail(String email) {
        if (email == null) {
            return null;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + gender + " FROM " + dbtable + " WHERE " + emailaddress + " = ?";
        Cursor cursor = null;
        String genderResult = null;
        try {
            cursor = db.rawQuery(query, new String[]{email});
            if (cursor.moveToFirst()) {
                genderResult = cursor.getString(cursor.getColumnIndexOrThrow(gender));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return genderResult;
    }

    public boolean updateUserProfile(users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(name, user.getName());
        values.put(emailaddress, user.getEmail());
        values.put(gender, user.getGender());
        values.put(age, user.getAge());
        values.put(bio, user.getBio());

        // Use email as the identifier for update
        int rowsAffected = db.update(dbtable, values, emailaddress + " = ?", new String[]{user.getEmail()});
        db.close();

        // Return true if at least one row was updated, false otherwise
        return rowsAffected > 0;
    }












}
