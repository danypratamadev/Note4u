package com.example.note4u;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteReminder extends BroadcastReceiver {

    private int idNotification = 0;
    private final static String CHANNEL_NAME = "note_reminder";
    private final static String CHANNEL_ID = "note_reminder_id";
    private List<NotesModel> notesArrayList = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotifyReminder(context);
    }

    private void showNotifyReminder(Context context) {

        String title_reminder = "Reminder untuk aktivitasmu";
        databaseHelper = new DatabaseHelper(context);
        Date dateNow = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("EEE, dd MMMM yyyy HH.mm.ss", Locale.getDefault()).format(dateNow);
        notesArrayList.addAll(databaseHelper.getAllNotes());


        for(NotesModel note : notesArrayList){
            if(note.getNote_reminder().equals(currentDate)){
                title_reminder = note.getNote_title();
                databaseHelper.updateNote(note.getNote_id(), note.getNote_title(), note.getNote_body(), note.getNote_date(), "null", note.getNote_bg());
            }
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("NOTE4U Reminder")
            .setContentText("Kamu memiliki kegiatan, yaitu: " + title_reminder)
            .setSmallIcon(R.drawable.round_circle_notifications_black_24dp)
            .setContentIntent(pendingIntent)
            .setGroupSummary(true)
            .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mBuilder.setChannelId(CHANNEL_ID);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }

        }

        Notification notification = mBuilder.build();

        if (mNotificationManager != null) {
            mNotificationManager.notify(idNotification, notification);
            idNotification++;
        }

    }

}
