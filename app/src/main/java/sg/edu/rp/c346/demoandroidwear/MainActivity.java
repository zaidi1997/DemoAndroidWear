package sg.edu.rp.c346.demoandroidwear;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    int notificationId = 001; // A unique ID for our notification
    Button btnNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotif = this.findViewById(R.id.btnNotif);

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager nm = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new
                            NotificationChannel("default", "Default Channel",
                            NotificationManager.IMPORTANCE_DEFAULT);

                    channel.setDescription("This is for default notification");
                    nm.createNotificationChannel(channel);
                }

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(MainActivity.this, 0,
                                intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Action action = new
                        NotificationCompat.Action.Builder(
                        R.mipmap.ic_launcher,
                        "This is an Action",
                        pendingIntent).build();


                Intent intentreply = new Intent(MainActivity.this,
                        ReplyActivity.class);
                PendingIntent pendingIntentReply = PendingIntent.getActivity
                        (MainActivity.this, 0, intentreply,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                RemoteInput ri = new RemoteInput.Builder("status")
                        .setLabel("Status report")
                        .setChoices(new String [] {"Done", "Not yet"})
                        .build();

                NotificationCompat.Action action2 = new
                        NotificationCompat.Action.Builder(
                        R.mipmap.ic_launcher,
                        "Reply",
                        pendingIntentReply)
                        .addRemoteInput(ri)
                        .build();


                NotificationCompat.WearableExtender extender = new
                        NotificationCompat.WearableExtender();
                extender.addAction(action);
                extender.addAction(action2);

                String text = getString(R.string.basic_notify_msg);
                String title = getString(R.string.notification_title);

                NotificationCompat.Builder builder = new
                        NotificationCompat.Builder(MainActivity.this, "default");
                builder.setContentText(text);
                builder.setContentTitle(title);
                builder.setSmallIcon(android.R.drawable.btn_star_big_off);

                // Attach the action for Wear notification created above
                builder.extend(extender);

                Notification notification = builder.build();

                nm.notify(notificationId, notification);
            }
        });
    }

}
