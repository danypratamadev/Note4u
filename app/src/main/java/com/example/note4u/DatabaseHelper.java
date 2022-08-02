package com.example.note4u;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "note4u.db";
    private static final String MY_NOTE = "notes";
    private static final String NOTE_ID = "note_id";
    private static final String NOTE_TITLE = "note_title";
    private static final String NOTE_BODY = "note_body";
    private static final String NOTE_DATE = "note_date";
    private static final String NOTE_REMINDER = "note_reminder";
    private static final String NOTE_BG = "note_bg";

    private static final String CREATE_TABLE_NOTE = "CREATE TABLE " + MY_NOTE + " (" + NOTE_ID + " INTEGER PRIMARY KEY, " +
            NOTE_TITLE + " TEXT NOT NULL, " +
            NOTE_BODY + " TEXT NOT NULL, " +
            NOTE_DATE + " TEXT NOT NULL, " +
            NOTE_REMINDER + " TEXT NOT NULL, " +
            NOTE_BG + " INTEGER NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MY_NOTE);
        onCreate(db);
    }

    public Boolean saveNewNote(int id, String title, String body, String date, String reminder, int theme){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_ID, id);
        contentValues.put(NOTE_TITLE, title);
        contentValues.put(NOTE_BODY, body);
        contentValues.put(NOTE_DATE, date);
        contentValues.put(NOTE_REMINDER, reminder);
        contentValues.put(NOTE_BG, theme);

        long result = db.insert(MY_NOTE, null, contentValues);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateNote(int id, String title, String body, String date, String reminder, int theme){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE, title);
        contentValues.put(NOTE_BODY, body);
        contentValues.put(NOTE_DATE, date);
        contentValues.put(NOTE_REMINDER, reminder);
        contentValues.put(NOTE_BG, theme);

        long result = db.update(MY_NOTE, contentValues,NOTE_ID + " = " + id, null);

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public List<NotesModel> getAllNotes(){
        List<NotesModel> noteArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + MY_NOTE + " ORDER BY " + NOTE_DATE + " DESC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                NotesModel notes = new NotesModel();
                notes.setNote_id(cursor.getInt(cursor.getColumnIndex(NOTE_ID)));
                notes.setNote_title(cursor.getString(cursor.getColumnIndex(NOTE_TITLE)));
                notes.setNote_body(cursor.getString(cursor.getColumnIndex(NOTE_BODY)));
                notes.setNote_date(cursor.getString(cursor.getColumnIndex(NOTE_DATE)));
                notes.setNote_reminder(cursor.getString(cursor.getColumnIndex(NOTE_REMINDER)));
                notes.setNote_bg(cursor.getInt(cursor.getColumnIndex(NOTE_BG)));
                notes.setNote_checked(false);
                noteArrayList.add(notes);
            } while (cursor.moveToNext());
        }
        db.close();
        return noteArrayList;
    }

    public Boolean deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(MY_NOTE, NOTE_ID + " = " + id,null);

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

}
