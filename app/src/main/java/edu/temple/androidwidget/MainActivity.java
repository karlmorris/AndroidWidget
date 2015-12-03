package edu.temple.androidwidget;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button turnOffButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turnOffButton = (Button) findViewById(R.id.turnOffButton);

        // Set a repeating alarm for evey 10 seconds
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(this, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                10 * 1000, pendingIntent);


        turnOffButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alarmManager.cancel(pendingIntent);
            }
        });
    }
}