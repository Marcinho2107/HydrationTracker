/**
 * Abstrakter Dienst zur Überwachung der Temperatur der Gerätebatterie und zur Anzeige einer Benachrichtigung, wenn sie einen Schwellenwert überschreitet.
 * Erweiterungsklassen müssen eine spezifische Logik implementieren, um festzustellen, wann das Gerät überhitzt ist.
 */
package com.example.hydrationtracker_git.TemperatureMonitoring;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.hydrationtracker_git.MainMenu.MainScreen;
import com.example.hydrationtracker_git.R;
import java.util.Objects;

public abstract class TemperatureMonitoringService extends Service {

    /**
     * BroadcastReceiver zur Überwachung von Batterietemperaturänderungen und zum Auslösen entsprechender Aktionen.
     */
    private final BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), Intent.ACTION_BATTERY_CHANGED)) {

                if (phoneHeating()) {
                    showNotification();
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryInfoReceiver, filter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(batteryInfoReceiver);
        super.onDestroy();
    }

    /**
     * Überprüft, ob die Temperatur des Telefons über einem Schwellenwert liegt.
     *
     * @return true, wenn die Temperatur des Telefons über dem Schwellenwert liegt, sonst false.
     */
    private boolean phoneHeating() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, filter);
        if (batteryStatus != null) {
            int temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            int temperatureThreshold = 220; // 35 degrees Celsius

            return temperature >= temperatureThreshold;
        } else {
            // No Battery
            return false;
        }
    }

    /**
     * Zeigt eine Benachrichtigung an, wenn die Temperatur des Telefons über dem Schwellenwert liegt.
     */
    @SuppressLint("ObsoleteSdkInt")
    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("hot_phone_channel", "Hot Phone Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "hot_phone_channel")
                .setContentTitle("Phone Heating")
                .setContentText("Looks like it is hot outside! Do not forget to drink water!")
                .setSmallIcon(R.drawable.baseline_local_fire_department_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());
    }

    /**
     * onBind-Methode überschrieben, um null zurückzugeben, da es sich nicht um einen gebundenen Dienst handelt.
     *
     * @param intent Der Intent, der zur Bindung an diesen Dienst verwendet wurde, wie in Context.bindService angegeben.
     * @return Gibt immer null zurück.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
