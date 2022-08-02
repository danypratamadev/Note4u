package com.example.note4u;

public class NotesModel {

    private int note_id;
    private String note_title;
    private String note_body;
    private String note_date;
    private String note_reminder;
    private int note_bg;
    private boolean note_checked;

    public NotesModel(){}

    public NotesModel(int note_id, String note_tite, String note_body, String note_date, String note_reminder, int note_theme, boolean note_checked) {
        this.note_id = note_id;
        this.note_title = note_tite;
        this.note_body = note_body;
        this.note_date = note_date;
        this.note_bg = note_theme;
        this.note_checked = note_checked;
        this.note_reminder = note_reminder;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_body() {
        return note_body;
    }

    public void setNote_body(String note_body) {
        this.note_body = note_body;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }

    public int getNote_bg() {
        return note_bg;
    }

    public void setNote_bg(int note_bg) {
        this.note_bg = note_bg;
    }

    public boolean isNote_checked() {
        return note_checked;
    }

    public void setNote_checked(boolean note_checked) {
        this.note_checked = note_checked;
    }

    public String getNote_reminder() {
        return note_reminder;
    }

    public void setNote_reminder(String note_reminder) {
        this.note_reminder = note_reminder;
    }
}
