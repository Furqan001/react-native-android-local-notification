package com.exease.react.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;


import android.util.Log;

/**
 * Set alarms for scheduled notification after system reboot.
 */
public class SystemBootEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ReactSystemNotification", "SystemBootEventReceiver: Setting system alarms");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            RCTNotificationManager notificationManager = new RCTNotificationManager(context);

            ArrayList<Integer> ids = notificationManager.getIDs();

            for (Integer id: ids) {
                try {
                    Notification notification = notificationManager.find(id);

                    if (notification.getAttributes() != null) {
                        notification.cancelAlarm();
                        notification.setAlarmAndSaveOrShow();
                        Log.i("ReactSystemNotification", "SystemBootEventReceiver: Alarm set for: " + notification.getAttributes().id);
                    }
                } catch (Exception e) {
                    Log.e("ReactSystemNotification", "SystemBootEventReceiver: onReceive Error: " + e.getMessage());
                }
            }
        }
    }
}
