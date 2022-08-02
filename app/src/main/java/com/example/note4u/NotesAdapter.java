package com.example.note4u;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private MainActivity mainActivity;
    private List<NotesModel> notesArrayList;
    private DatabaseHelper databaseHelper;

    public NotesAdapter(MainActivity mainActivity, List<NotesModel> notesArrayList) {
        this.mainActivity = mainActivity;
        this.notesArrayList = notesArrayList;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_card, parent, false);
        NotesViewHolder nvh = new NotesViewHolder(view);
        return nvh;

    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder holder, int position) {

        databaseHelper = new DatabaseHelper(mainActivity);
        final NotesModel currentNote = notesArrayList.get(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if(position == 0) {
            params.setMargins(40, 45, 40, 45);
            holder.note_card.setLayoutParams(params);
        } else if(position == notesArrayList.size() - 1){
            params.setMargins(40, 0, 40, 350);
            holder.note_card.setLayoutParams(params);
        } else {
            params.setMargins(40, 0, 40, 45);
            holder.note_card.setLayoutParams(params);
        }

        if(currentNote.getNote_title().isEmpty()){
            holder.note_title_card.setText("Untitled");
        } else {
            holder.note_title_card.setText(currentNote.getNote_title());
        }
        holder.note_date_card.setText(currentNote.getNote_date());

        holder.note_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, NotesActivity.class);
                intent.putExtra("id", currentNote.getNote_id());
                intent.putExtra("title", currentNote.getNote_title());
                intent.putExtra("body", currentNote.getNote_body());
                intent.putExtra("bg", currentNote.getNote_bg());
                intent.putExtra("reminder", currentNote.getNote_reminder());
                mainActivity.startActivity(intent);
            }
        });

        holder.note_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String message;

                if(currentNote.getNote_title() != null){
                    message = currentNote.getNote_title();
                } else {
                    message = "Untitled";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle("Hapus Catatan");
                builder.setMessage("Apakah kamu yakin ingin menghapus catatan " + message + "?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                boolean result = databaseHelper.deleteNote(currentNote.getNote_id());
                                if(result){
                                    notesArrayList.remove(currentNote);
                                    notifyDataSetChanged();
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
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder{

        LinearLayout note_card;
        TextView note_title_card;
        TextView note_date_card;

        public NotesViewHolder(View itemView) {
            super(itemView);

            note_card = itemView.findViewById(R.id.note_card);
            note_title_card = itemView.findViewById(R.id.note_title_card);
            note_date_card = itemView.findViewById(R.id.note_date_card);

        }
    }

    public void setSearchNotes(List<NotesModel> notesFound) {
        notesArrayList = notesFound;
        notifyDataSetChanged();
    }

}
