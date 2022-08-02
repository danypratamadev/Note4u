package com.example.note4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_BackgroundColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontSize;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper;
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private Date dateNow;
    private Date reminderDateTime;

    private EditText note_title;
    private TextView note_date;
    private AREditText note_editor;
    private IARE_Toolbar note_toolbar;
    private LinearLayout toolbar_view;
    private LinearLayout note_reminder_info;
    private TextView note_date_reminder;
    private HorizontalScrollView bg_item;

    private ImageButton back_button;
    private ImageButton reminder_button;
    private ImageButton bg_button;
    private ImageButton delete_button;
    private CircleImageView bg_1;
    private CircleImageView bg_2;
    private CircleImageView bg_3;
    private CircleImageView bg_4;
    private CircleImageView bg_5;
    private ImageView note_bg_display;

    private int note_id;
    private int note_bg = 10;
    private boolean savedToDB = false;
    private boolean note_title_focused = false;
    private boolean note_editor_focused = false;
    private boolean showBgItem = false;

    @Override
    public void onBackPressed() {
        if(note_title_focused || note_editor_focused){
            try {
                InputMethodManager keyInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyInput.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                Log.e("Error", "Something went wrong!");
            }
            note_title.clearFocus();
            note_editor.clearFocus();
            note_title_focused = false;
            note_editor_focused = false;
            toolbar_view.setVisibility(View.GONE);
            if(reminderDateTime != null){
                note_reminder_info.setVisibility(View.VISIBLE);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("com.example.note4u", Context.MODE_PRIVATE);
        editor = preferences.edit();
        int theme = preferences.getInt("theme", 10);
        if(theme == 20){
            setTheme(R.style.PinkTheme);
        } else if(theme == 30){
            setTheme(R.style.BlueTheme);
        } else if(theme == 40){
            setTheme(R.style.GreenTheme);
        } else if(theme == 50){
            setTheme(R.style.OrangeTheme);
        } else if(theme == 60){
            setTheme(R.style.BrownTheme);
        } else {
            setTheme(R.style.PurpleTheme);
        }
        setContentView(R.layout.activity_notes);

        init();

    }

    private void init() {

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        int bg = intent.getIntExtra("bg", 10);
        String reminder = intent.getStringExtra("reminder");

        databaseHelper = new DatabaseHelper(this);
        dateNow = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm", Locale.getDefault()).format(dateNow);

        back_button = findViewById(R.id.back_button);
        reminder_button = findViewById(R.id.reminder_button);
        bg_button = findViewById(R.id.bg_button);
        delete_button = findViewById(R.id.delete_button);
        note_title = findViewById(R.id.note_title);
        note_date = findViewById(R.id.note_date);
        note_editor = findViewById(R.id.note_editor);
        note_toolbar = findViewById(R.id.note_toolbar);
        toolbar_view = findViewById(R.id.toolbar_view);
        note_reminder_info = findViewById(R.id.note_reminder_info);
        note_date_reminder = findViewById(R.id.note_date_reminder);
        bg_item = findViewById(R.id.bg_item);
        bg_1 = findViewById(R.id.bg_1);
        bg_2 = findViewById(R.id.bg_2);
        bg_3 = findViewById(R.id.bg_3);
        bg_4 = findViewById(R.id.bg_4);
        bg_5 = findViewById(R.id.bg_5);
        note_bg_display = findViewById(R.id.note_bg_display);

        setUpToolBar();

        if(id != 0){
            savedToDB = true;
            note_id = id;
            note_title.setText(title);
            note_editor.fromHtml(body);
            toolbar_view.setVisibility(View.GONE);
            delete_button.setVisibility(View.VISIBLE);
            note_bg = bg;
            if(reminder.equals("null")){
                reminder_button.setVisibility(View.VISIBLE);
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm.ss");
                try {
                    reminderDateTime = dateFormat.parse(reminder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String dateReminder = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm", Locale.getDefault()).format(reminderDateTime);
                note_reminder_info.setVisibility(View.VISIBLE);
                note_date_reminder.setText(dateReminder);
            }
        } else {
            note_editor.requestFocus();
        }

        setNoteBg();

        note_date.setText(currentDate);

        note_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!savedToDB){
                    saveNewNote();
                } else {
                    updateNote();
                }
            }
        });

        note_editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!savedToDB){
                    saveNewNote();
                } else {
                    updateNote();
                }
            }
        });

        note_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    note_title_focused = true;
                } else {
                    note_title_focused = false;
                }
            }
        });

        note_editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(toolbar_view.getVisibility() == View.GONE){
                        toolbar_view.setVisibility(View.VISIBLE);
                        if(note_reminder_info.getVisibility() == View.VISIBLE){
                            note_reminder_info.setVisibility(View.GONE);
                        }
                    }
                    note_editor_focused = true;
                } else {
                    note_editor_focused = false;
                }
            }
        });

        back_button.setOnClickListener(this);
        reminder_button.setOnClickListener(this);
        delete_button.setOnClickListener(this);
        note_reminder_info.setOnClickListener(this);
        bg_button.setOnClickListener(this);
        bg_1.setOnClickListener(this);
        bg_2.setOnClickListener(this);
        bg_3.setOnClickListener(this);
        bg_4.setOnClickListener(this);
        bg_5.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_button:
                super.onBackPressed();
                break;
            case R.id.delete_button:
                deleteNote();
                break;
            case R.id.reminder_button:
                setNoteReminder();
                break;
            case R.id.note_reminder_info:
                cancelNoteReminder();
                break;
            case R.id.bg_button:
                if(showBgItem){
                    if(savedToDB){
                        reminder_button.setVisibility(View.VISIBLE);
                        delete_button.setVisibility(View.VISIBLE);
                    }
                    bg_item.setVisibility(View.GONE);
                    bg_button.setImageDrawable(getResources().getDrawable(R.drawable.outline_color_lens_black_24dp));
                    showBgItem = false;
                } else {
                    reminder_button.setVisibility(View.GONE);
                    delete_button.setVisibility(View.GONE);
                    bg_item.setVisibility(View.VISIBLE);
                    bg_button.setImageDrawable(getResources().getDrawable(R.drawable.round_close_black_24dp));
                    showBgItem = true;
                }
                break;
            case R.id.bg_1:
                note_bg = 10;
                setNoteBg();
                if(!savedToDB){
                    saveNewNote();
                } else {
                    updateNote();
                }
                break;
            case R.id.bg_2:
                note_bg = 20;
                setNoteBg();
                if(!savedToDB){
                    saveNewNote();
                } else {
                    updateNote();
                }
                break;
            case R.id.bg_3:
                note_bg = 30;
                setNoteBg();
                if(!savedToDB){
                    saveNewNote();
                } else {
                    updateNote();
                }
                break;
            case R.id.bg_4:
                note_bg = 40;
                setNoteBg();
                if(!savedToDB){
                    saveNewNote();
                } else {
                    updateNote();
                }
                break;
            case R.id.bg_5:
                note_bg = 50;
                setNoteBg();
                if(!savedToDB){
                    saveNewNote();
                } else {
                    updateNote();
                }
                break;
        }
    }

    private void setUpToolBar() {

        IARE_ToolItem fontColor = new ARE_ToolItem_FontColor();
        IARE_ToolItem fontSize = new ARE_ToolItem_FontSize();
        IARE_ToolItem backFont = new ARE_ToolItem_BackgroundColor();
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();

        note_toolbar.addToolbarItem(fontColor);
        note_toolbar.addToolbarItem(fontSize);
        note_toolbar.addToolbarItem(backFont);
        note_toolbar.addToolbarItem(bold);
        note_toolbar.addToolbarItem(italic);
        note_toolbar.addToolbarItem(underline);
        note_toolbar.addToolbarItem(strikethrough);
        note_toolbar.addToolbarItem(listNumber);
        note_toolbar.addToolbarItem(listBullet);
        note_toolbar.addToolbarItem(left);
        note_toolbar.addToolbarItem(center);
        note_toolbar.addToolbarItem(right);

        note_editor.setToolbar(note_toolbar);

    }

    private void saveNewNote() {

        Random random = new Random();
        String randomId = String.format("%6d", random.nextInt(999999));
        note_id = Integer.parseInt(randomId);

        dateNow = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm.ss", Locale.getDefault()).format(dateNow);
        String dateReminder = "null";
        if(reminderDateTime != null){
            dateReminder = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm.ss", Locale.getDefault()).format(reminderDateTime);
        }
        boolean result = databaseHelper.saveNewNote(note_id, note_title.getText().toString(), note_editor.getHtml(), currentDate, dateReminder, note_bg);
        if(result){
            savedToDB = true;
            if(!showBgItem){
                delete_button.setVisibility(View.VISIBLE);
                reminder_button.setVisibility(View.VISIBLE);
            }
        }

    }

    private void updateNote() {

        dateNow = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm.ss", Locale.getDefault()).format(dateNow);
        String dateReminder = "null";
        if(reminderDateTime != null){
            dateReminder = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm.ss", Locale.getDefault()).format(reminderDateTime);
        }
        boolean result = databaseHelper.updateNote(note_id, note_title.getText().toString(), note_editor.getHtml(), currentDate, dateReminder, note_bg);

    }

    private void deleteNote() {

        try {
            InputMethodManager keyInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            keyInput.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("Error", "Something went wrong!");
        }
        note_title.clearFocus();
        note_editor.clearFocus();
        note_title_focused = false;
        note_editor_focused = false;

        String message;

        if(note_title.getText() != null){
            message = note_title.getText().toString();
        } else {
            message = "Untitled";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Catatan");
        builder.setMessage("Apakah kamu yakin ingin menghapus catatan " + message + "?");
        builder.setCancelable(true);

        builder.setPositiveButton(
            "Ya",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    boolean result = databaseHelper.deleteNote(note_id);
                    if(result){
                        onBackPressed();
                    }
                }
            });

        builder.setNegativeButton(
            "Tidak",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        AlertDialog alertDelete = builder.create();
        alertDelete.show();

    }

    private void setNoteReminder() {

        try {
            InputMethodManager keyInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            keyInput.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("Error", "Something went wrong!");
        }
        note_title.clearFocus();
        note_editor.clearFocus();
        note_title_focused = false;
        note_editor_focused = false;

        new SingleDateAndTimePickerDialog.Builder(this).bottomSheet().curved()
            .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                @Override
                public void onDisplayed(SingleDateAndTimePicker picker) {
                    picker.setMinDate(dateNow);
                }
            })
            .title("Ingatkan saya pada tanggal")
            .titleTextSize(18)
            .titleTextColor(getResources().getColor(R.color.black))
            .minutesStep(1)
            .listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {

                    reminderDateTime = date;

                    Intent intent = new Intent(NotesActivity.this, NoteReminder.class);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(NotesActivity.this, note_id, intent, 0);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(reminderDateTime.getTime());

                    if(alarmManager != null){
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        Toast.makeText(NotesActivity.this, "Reminder aktif", Toast.LENGTH_SHORT).show();
                        String dateReminder = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm", Locale.getDefault()).format(reminderDateTime);
                        note_reminder_info.setVisibility(View.VISIBLE);
                        reminder_button.setVisibility(View.GONE);
                        toolbar_view.setVisibility(View.GONE);
                        note_date_reminder.setText(dateReminder);
                        updateNote();
                    } else {
                        Toast.makeText(NotesActivity.this, "Tidak dapat memasang reminder!", Toast.LENGTH_SHORT).show();
                    }

                }
            }).display();

    }

    private void cancelNoteReminder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reminder Catatan");
        builder.setMessage("Apakah kamu yakin ingin menghapus reminder?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(NotesActivity.this, NoteReminder.class);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotesActivity.this, note_id, intent, 0);

                        alarmManager.cancel(pendingIntent);
                        reminderDateTime = null;
                        updateNote();
                        reminder_button.setVisibility(View.VISIBLE);
                        note_reminder_info.setVisibility(View.GONE);
                        Toast.makeText(NotesActivity.this, "Reminder tidak aktif", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.setNegativeButton(
                "Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDelete = builder.create();
        alertDelete.show();

    }

    private void setNoteBg() {

        switch (note_bg){
            case 10:
                note_bg_display.setImageDrawable(getResources().getDrawable(R.drawable.book));
                break;
            case 20:
                note_bg_display.setImageDrawable(getResources().getDrawable(R.drawable.space));
                break;
            case 30:
                note_bg_display.setImageDrawable(getResources().getDrawable(R.drawable.tree));
                break;
            case 40:
                note_bg_display.setImageDrawable(getResources().getDrawable(R.drawable.flower));
                break;
            case 50:
                note_bg_display.setImageDrawable(getResources().getDrawable(R.drawable.building));
                break;
        }

    }

}