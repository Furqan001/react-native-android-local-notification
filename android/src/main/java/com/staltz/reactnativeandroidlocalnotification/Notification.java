package com.staltz.reactnativeandroidlocalnotification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.net.Uri;
import android.app.NotificationChannel;

import java.lang.System;
import java.net.URL;

import com.google.gson.Gson;

import android.util.Base64;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.graphics.Color;

/**
 * An object-oriented Wrapper class around the system notification class.
 *
 * Each instance is an representation of a single, or a set of scheduled
 * notifications. It handles operations like showing, canceling and clearing.
 */
public class Notification {
    private Context context;
    private int id;
    private NotificationAttributes attributes;

    /**
     * Constructor.
     */
    public Notification(Context context, int id, @Nullable NotificationAttributes attributes) {
        this.context = context;
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * Public context getter.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Public attributes getter.
     */
    public NotificationAttributes getAttributes() {
        return attributes;
    }

    /**
     * Create the notification, show it now or set the schedule.
     */
    public Notification create() {
        setAlarmAndSaveOrShow();

        Log.i("ReactSystemNotification", "Notification Created: " + id);

        return this;
    }

    /**
     * Update the notification, resets its schedule.
     */
    public Notification update(NotificationAttributes notificationAttributes) {
        delete();
        attributes = notificationAttributes;
        setAlarmAndSaveOrShow();

        return this;
    }

    /**
     * Clear the notification from the status bar.
     */
    public Notification clear() {
        getSysNotificationManager().cancel(id);

        Log.i("ReactSystemNotification", "Notification Cleared: " + id);

        return this;
    }

    /**
     * Cancel the notification.
     */
    public Notification delete() {
        getSysNotificationManager().cancel(id);

        if (attributes.delayed || attributes.scheduled) {
            cancelAlarm();
        }

        deleteFromPreferences();

        Log.i("ReactSystemNotification", "Notification Deleted: " + id);

        return this;
    }

    /**
     * Build the notification.
     */
    public android.app.Notification build() {
        NotificationCompat.Builder notificationBuilder;
        String iconName = attributes.smallIcon != null ? attributes.smallIcon : "ic_launcher";
        int iconResource = context.getResources().getIdentifier(attributes.smallIcon, "mipmap", context.getPackageName());
        String channelID = attributes.channelID != null ? attributes.channelID : "channel_0";
        if (iconResource == 0) {
            Log.w("Notification", "icon resource not found with name " + iconName);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = attributes.channelName != null ? attributes.channelName : "Default";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationManager notificationManager = getSysNotificationManager();
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }
        notificationBuilder = new NotificationCompat.Builder(context, channelID);
        notificationBuilder.setContentTitle(attributes.subject).setContentText(attributes.message)
                .setSmallIcon(iconResource)
                .setAutoCancel(attributes.autoClear).setContentIntent(getContentIntent());

        if (attributes.priority != null) {
            notificationBuilder.setPriority(attributes.priority);
        }

        if (attributes.largeIcon != null) {
            int largeIconId = context.getResources().getIdentifier(attributes.largeIcon, "drawable",
                    context.getPackageName());
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), largeIconId);
            notificationBuilder.setLargeIcon(largeIcon);
        }

        if (attributes.group != null) {
            notificationBuilder.setGroup(attributes.group);
            notificationBuilder.setGroupSummary(true);
        }

        if (attributes.inboxStyle) {

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            if (attributes.inboxStyleBigContentTitle != null) {
                inboxStyle.setBigContentTitle(attributes.inboxStyleBigContentTitle);
            }
            if (attributes.inboxStyleSummaryText != null) {
                inboxStyle.setSummaryText(attributes.inboxStyleSummaryText);
            }
            if (attributes.inboxStyleLines != null) {
                for (int i = 0; i < attributes.inboxStyleLines.size(); i++) {
                    inboxStyle.addLine(Html.fromHtml(attributes.inboxStyleLines.get(i)));
                }
            }
            notificationBuilder.setStyle(inboxStyle);

            Log.i("ReactSystemNotification", "set inbox style!!");

        } else {

            int defaults = 0;
            if ("default".equals(attributes.sound)) {
                defaults = defaults | android.app.Notification.DEFAULT_SOUND;
            }
            if ("default".equals(attributes.vibrate)) {
                defaults = defaults | android.app.Notification.DEFAULT_VIBRATE;
            }
            if ("default".equals(attributes.lights)) {
                defaults = defaults | android.app.Notification.DEFAULT_LIGHTS;
            }
            notificationBuilder.setDefaults(defaults);

        }

        if (attributes.onlyAlertOnce != null) {
            notificationBuilder.setOnlyAlertOnce(attributes.onlyAlertOnce);
        }

        if (attributes.tickerText != null) {
            notificationBuilder.setTicker(attributes.tickerText);
        }

        if (attributes.when != null) {
            notificationBuilder.setWhen(attributes.when);
            notificationBuilder.setShowWhen(true);
        }

        // if bigText is not null, it have priority over bigStyleImageBase64
        if (attributes.bigText != null) {
            notificationBuilder
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(attributes.bigText));
        } else if (attributes.bigStyleUrlImage != null && !attributes.bigStyleUrlImage.equals("")) {

            Bitmap bigPicture = null;

            try {

                Log.i("ReactSystemNotification", "start to get image from URL : " + attributes.bigStyleUrlImage);
                URL url = new URL(attributes.bigStyleUrlImage);
                bigPicture = BitmapFactory.decodeStream(url.openStream());
                Log.i("ReactSystemNotification", "finishing to get image from URL");

            } catch (Exception e) {
                Log.e("ReactSystemNotification", "Error when getting image from URL" + e.getStackTrace());
            }

            if (bigPicture != null) {
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture));
            }
        } else if (attributes.bigStyleImageBase64 != null) {

            Bitmap bigPicture = null;

            try {

                Log.i("ReactSystemNotification", "start to convert bigStyleImageBase64 to bitmap");
                // Convert base64 image to Bitmap
                byte[] bitmapAsBytes = Base64.decode(attributes.bigStyleImageBase64.getBytes(), Base64.DEFAULT);
                bigPicture = BitmapFactory.decodeByteArray(bitmapAsBytes, 0, bitmapAsBytes.length);
                Log.i("ReactSystemNotification", "finished to convert bigStyleImageBase64 to bitmap");

            } catch (Exception e) {
                Log.e("ReactSystemNotification", "Error when converting base 64 to Bitmap" + e.getStackTrace());
            }

            if (bigPicture != null) {
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture));
            }
        }

        if (attributes.color != null) {
            notificationBuilder.setColor(Color.parseColor(attributes.color));
        }

        if (attributes.subText != null) {
            notificationBuilder.setSubText(attributes.subText);
        }

        if (attributes.progress != null) {
            if (attributes.progress < 0 || attributes.progress > 1000) {
                notificationBuilder.setProgress(1000, 100, true);
            } else {
                notificationBuilder.setProgress(1000, attributes.progress, false);
            }
        }

        if (attributes.number != null) {
            notificationBuilder.setNumber(attributes.number);
        }

        if (attributes.localOnly != null) {
            notificationBuilder.setLocalOnly(attributes.localOnly);
        }

        if (attributes.sound != null) {
            notificationBuilder.setSound(Uri.parse(attributes.sound));
        }

        return notificationBuilder.build();
    }

    /**
     * Show the notification now.
     */
    public void show() {
        getSysNotificationManager().notify(id, build());

        Log.i("ReactSystemNotification", "Notification Show: " + id);
    }

    /**
     * Setup alarm or show the notification.
     */
    public void setAlarmAndSaveOrShow() {
        if (attributes.delayed) {
            setDelay();
            saveAttributesToPreferences();

        } else if (attributes.scheduled) {
            setSchedule();
            saveAttributesToPreferences();

        } else {
            show();
        }
    }

    /**
     * Schedule the delayed notification.
     */
    public void setDelay() {
        PendingIntent pendingIntent = getScheduleNotificationIntent();

        long futureInMillis = SystemClock.elapsedRealtime() + attributes.delay;
        getAlarmManager().set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        Log.i("ReactSystemNotification",
                "Notification Delay Alarm Set: " + id + ", Repeat Type: " + attributes.repeatType + ", Current Time: "
                        + System.currentTimeMillis() + ", Delay: " + attributes.delay);
    }

    /**
     * Schedule the notification.
     */
    public void setSchedule() {
        PendingIntent pendingIntent = getScheduleNotificationIntent();

        if (attributes.repeatType == null) {
            getAlarmManager().set(AlarmManager.RTC_WAKEUP, attributes.sendAt, pendingIntent);
            Log.i("ReactSystemNotification", "Set One-Time Alarm: " + id);

        } else {
            switch (attributes.repeatType) {
            case "time":
                getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, attributes.sendAt, attributes.repeatTime,
                        pendingIntent);
                Log.i("ReactSystemNotification", "Set " + attributes.repeatTime + "ms Alarm: " + id);
                break;

            case "minute":
                getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, attributes.sendAt, 60000, pendingIntent);
                Log.i("ReactSystemNotification", "Set Minute Alarm: " + id);
                break;

            case "hour":
                getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, attributes.sendAt, AlarmManager.INTERVAL_HOUR,
                        pendingIntent);
                Log.i("ReactSystemNotification", "Set Hour Alarm: " + id);
                break;

            case "halfDay":
                getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, attributes.sendAt,
                        AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
                Log.i("ReactSystemNotification", "Set Half-Day Alarm: " + id);
                break;

            case "day":
            case "week":
            case "month":
            case "year":
                getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, attributes.sendAt, AlarmManager.INTERVAL_DAY,
                        pendingIntent);
                Log.i("ReactSystemNotification", "Set Day Alarm: " + id + ", Type: " + attributes.repeatType);
                break;

            default:
                getAlarmManager().set(AlarmManager.RTC_WAKEUP, attributes.sendAt, pendingIntent);
                Log.i("ReactSystemNotification", "Set One-Time Alarm: " + id);
                break;
            }
        }

        Log.i("ReactSystemNotification",
                "Notification Schedule Alarm Set: " + id + ", Repeat Type: " + attributes.repeatType
                        + ", Current Time: " + System.currentTimeMillis() + ", First Send At: " + attributes.sendAt);
    }

    /**
     * Cancel the delayed notification.
     */
    public void cancelAlarm() {
        PendingIntent pendingIntent = getScheduleNotificationIntent();
        getAlarmManager().cancel(pendingIntent);

        Log.i("ReactSystemNotification", "Notification Alarm Canceled: " + id);
    }

    public void saveAttributesToPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        String attributesJSONString = new Gson().toJson(attributes);

        editor.putString(Integer.toString(id), attributesJSONString);

        if (Build.VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

        Log.i("ReactSystemNotification", "Notification Saved To Pref: " + id + ": " + attributesJSONString);
    }

    public void loadAttributesFromPreferences() {
        String attributesJSONString = getSharedPreferences().getString(Integer.toString(id), null);
        this.attributes = (NotificationAttributes) new Gson().fromJson(attributesJSONString,
                NotificationAttributes.class);

        Log.i("ReactSystemNotification", "Notification Loaded From Pref: " + id + ": " + attributesJSONString);
    }

    public void deleteFromPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        editor.remove(Integer.toString(id));

        if (Build.VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

        Log.i("ReactSystemNotification", "Notification Deleted From Pref: " + id);
    }

    private NotificationManager getSysNotificationManager() {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private SharedPreferences getSharedPreferences() {
        return RCTNotificationManager.getSharedPreferences(context);
    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(context, NotificationEventReceiver.class);

        intent.putExtra(NotificationEventReceiver.NOTIFICATION_ID, id);
        intent.putExtra(NotificationEventReceiver.ACTION, attributes.action);
        intent.putExtra(NotificationEventReceiver.PAYLOAD, attributes.payload);

        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getScheduleNotificationIntent() {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);

        return PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
