package com.example.note4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper;
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private NotesAdapter notesAdapter;
    private EditText search_note;
    private RecyclerView rcy_notes;
    private ExtendedFloatingActionButton addNote;
    private HorizontalScrollView theme_app_color;
    private ImageButton theme_app;
    private ImageButton purple_theme;
    private ImageButton pink_theme;
    private ImageButton blue_theme;
    private ImageButton green_theme;
    private ImageButton orange_theme;
    private ImageButton brown_theme;

    private List<NotesModel> notesArrayList = new ArrayList<>();

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
        setContentView(R.layout.activity_main);

        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllNotes();
    }

    private void init() {

        databaseHelper = new DatabaseHelper(this);
        search_note = findViewById(R.id.search_note);
        rcy_notes = findViewById(R.id.rcy_notes);
        addNote = findViewById(R.id.add_note);
        theme_app_color = findViewById(R.id.theme_app_color);
        theme_app = findViewById(R.id.theme_app);
        purple_theme = findViewById(R.id.purple_theme);
        pink_theme = findViewById(R.id.pink_theme);
        blue_theme = findViewById(R.id.blue_theme);
        green_theme = findViewById(R.id.green_theme);
        orange_theme = findViewById(R.id.orange_theme);
        brown_theme = findViewById(R.id.brown_theme);

        rcy_notes.setHasFixedSize(true);
        rcy_notes.setLayoutManager(new LinearLayoutManager(this));

        search_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchNote(s.toString());
            }
        });

        getAllNotes();

        addNote.setOnClickListener(this);
        theme_app.setOnClickListener(this);
        purple_theme.setOnClickListener(this);
        pink_theme.setOnClickListener(this);
        blue_theme.setOnClickListener(this);
        green_theme.setOnClickListener(this);
        orange_theme.setOnClickListener(this);
        brown_theme.setOnClickListener(this);

    }

    private void searchNote(String text) {
        List<NotesModel> notesFound = new ArrayList<>();

        for (NotesModel item : notesArrayList) {
            if (item.getNote_title().toLowerCase().contains(text.toLowerCase())) {
                notesFound.add(item);
            }
        }

        notesAdapter.setSearchNotes(notesFound);
    }

    private void getAllNotes() {

        notesArrayList.clear();
        notesArrayList.addAll(databaseHelper.getAllNotes());

        notesAdapter = new NotesAdapter(this, notesArrayList);
        rcy_notes.setAdapter(notesAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_note:
                Intent intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
            break;
            case R.id.theme_app:
                if(theme_app_color.getVisibility() == View.INVISIBLE){
                    theme_app_color.setVisibility(View.VISIBLE);
                    theme_app.setImageDrawable(getResources().getDrawable(R.drawable.round_close_black_24dp));
                } else {
                    theme_app_color.setVisibility(View.INVISIBLE);
                    theme_app.setImageDrawable(getResources().getDrawable(R.drawable.outline_color_lens_black_24dp));
                }
                break;
            case R.id.purple_theme:
                theme_app_color.setVisibility(View.INVISIBLE);
                theme_app.setImageDrawable(getResources().getDrawable(R.drawable.outline_color_lens_black_24dp));
                editor.putInt("theme", 10);
                editor.apply();
                recreate();
                break;
            case R.id.pink_theme:
                theme_app_color.setVisibility(View.INVISIBLE);
                theme_app.setImageDrawable(getResources().getDrawable(R.drawable.outline_color_lens_black_24dp));
                editor.putInt("theme", 20);
                editor.apply();
                recreate();
                break;
            case R.id.blue_theme:
                theme_app_color.setVisibility(View.INVISIBLE);
                theme_app.setImageDrawable(getResources().getDrawable(R.drawable.outline_color_lens_black_24dp));
                editor.putInt("theme", 30);
                editor.apply();
                recreate();
                break;
            case R.id.green_theme:
                theme_app_color.setVisibility(View.INVISIBLE);
                theme_app.setImageDrawable(getResources().getDrawable(R.drawable.outline_color_lens_black_24dp));
                editor.putInt("theme", 40);
                editor.apply();
                recreate();
                break;
            case R.id.orange_theme:
                theme_app_color.setVisibility(View.INVISIBLE);
                theme_app.setImageDrawable(getResources().getDrawable(R.drawable.outline_color_lens_black_24dp));
                editor.putInt("theme", 50);
                editor.apply();
                recreate();
                break;
            case R.id.brown_theme:
                theme_app_color.setVisibility(View.INVISIBLE);
                theme_app.setImageDrawable(getResources().getDrawable(R.drawable.outline_color_lens_black_24dp));
                editor.putInt("theme", 60);
                editor.apply();
                recreate();
                break;
        }
    }

}