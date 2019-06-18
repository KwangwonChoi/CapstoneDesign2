package healthcare.ckwpjt.com.healthcare.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import healthcare.ckwpjt.com.healthcare.Activities.NotificatedActivity;
import healthcare.ckwpjt.com.healthcare.Objects.ClientLog;
import healthcare.ckwpjt.com.healthcare.R;

// 서비스 클래스를 구현하려면, Service 를 상속받는다
public class AlertService extends Service {

    private final String mCliendGrp = "Client/";
    private final String mClientId = "KwangwonChoi/";
    private final String mLogPath = "alert_log/";

    private FirebaseDatabase f = FirebaseDatabase.getInstance();
    private DatabaseReference ref = f.getReference(mCliendGrp + mClientId + mLogPath);
    private boolean isFirstServiceAcc = true;
    private LocalDateTime encounterTime;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

            Log.d("TAG", "onChildAdded:" + dataSnapshot.getKey());

            if(previousChildName != null) {
                if(LocalDateTime.parse(previousChildName,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        .isAfter(encounterTime)){

                    ClientLog messages = new ClientLog();
                    messages.setDate(dataSnapshot.getKey().toString());

                    // A new comment has been added, add it to the displayed list
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (ds.getKey().equals("details"))
                            messages.setDetails(ds.getValue().toString());

                        if (ds.getKey().equals("state"))
                            messages.setState(ds.getValue().toString());
                    }

                    postNotif(messages);
                }
            }
        }


        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d("StartService","onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("StartService","onCreate()");
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("StartService","onStartCommand()");

        encounterTime = LocalDateTime.now();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        ref.addChildEventListener(childEventListener);


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.d("StartService","onDestroy()");
        super.onDestroy();
    }


    private void postNotif(ClientLog notifString) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.doctor;
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, NotificatedActivity.class);
        notificationIntent.putExtra("receivedValue", notifString);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context, "channel_id")
                .setContentTitle(notifString.getDate())
                .setContentText(notifString.getState() + " : "+ notifString.getDetails())
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .build();

        mNotificationManager.notify(1, notification);
    }
}
