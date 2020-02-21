package com.staltz.reactnativeandroidlocalnotification;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;

public class NotificationAttributes {
    public Integer id;
    public String subject;
    public String message;
    public String action;
    public String payload;

    public String channelID;
    public String channelName;

    public Boolean delayed;
    public Integer delay;

    public Boolean scheduled;
    public Long sendAt;
    public Integer sendAtYear;
    public Integer sendAtMonth;
    public Integer sendAtDay;
    public Integer sendAtWeekDay;
    public Integer sendAtHour;
    public Integer sendAtMinute;

    public String repeatEvery;
    public String repeatType;
    public Integer repeatTime;
    public Integer repeatCount;
    public Long endAt;

    public Integer priority;
    public String smallIcon;
    public String largeIcon;
    public String sound;
    public String vibrate;
    public String lights;
    public Boolean autoClear;
    public Boolean onlyAlertOnce;
    public String tickerText;
    public Long when;
    public String bigText;
    public String bigStyleUrlImage;
    public String bigStyleImageBase64;
    public String subText;
    public Integer progress;
    public Integer lifetime;
    public Integer progressEnd;
    public String color;
    public Integer number;
    public String category;
    public Boolean localOnly;

    public Boolean inboxStyle;
    public String inboxStyleBigContentTitle;
    public String inboxStyleSummaryText;
    public ArrayList<String> inboxStyleLines;

    public String group;

    public void loadFromReadableMap(ReadableMap readableMap) {
        if (readableMap.hasKey("id"))
            id = readableMap.getInt("id");
        if (readableMap.hasKey("subject"))
            subject = readableMap.getString("subject");
        if (readableMap.hasKey("message"))
            message = readableMap.getString("message");
        if (readableMap.hasKey("action"))
            action = readableMap.getString("action");
        if (readableMap.hasKey("payload"))
            payload = readableMap.getString("payload");

        if (readableMap.hasKey("channelID"))
            channelID = readableMap.getString("channelID");
        if (readableMap.hasKey("channelName"))
            channelName = readableMap.getString("channelName");

        if (readableMap.hasKey("delayed"))
            delayed = readableMap.getBoolean("delayed");
        if (readableMap.hasKey("delay"))
            delay = readableMap.getInt("delay");

        if (readableMap.hasKey("scheduled"))
            scheduled = readableMap.getBoolean("scheduled");
        if (readableMap.hasKey("sendAt"))
            sendAt = Long.parseLong(readableMap.getString("sendAt"));
        if (readableMap.hasKey("sendAtYear"))
            sendAtYear = readableMap.getInt("sendAtYear");
        if (readableMap.hasKey("sendAtMonth"))
            sendAtMonth = readableMap.getInt("sendAtMonth");
        if (readableMap.hasKey("sendAtDay"))
            sendAtDay = readableMap.getInt("sendAtDay");
        if (readableMap.hasKey("sendAtWeekDay"))
            sendAtWeekDay = readableMap.getInt("sendAtWeekDay");
        if (readableMap.hasKey("sendAtHour"))
            sendAtHour = readableMap.getInt("sendAtHour");
        if (readableMap.hasKey("sendAtMinute"))
            sendAtMinute = readableMap.getInt("sendAtMinute");

        if (readableMap.hasKey("repeatEvery"))
            repeatEvery = readableMap.getString("repeatEvery");
        if (readableMap.hasKey("repeatType"))
            repeatType = readableMap.getString("repeatType");
        if (readableMap.hasKey("repeatTime"))
            repeatTime = readableMap.getInt("repeatTime");
        if (readableMap.hasKey("repeatCount"))
            repeatCount = readableMap.getInt("repeatCount");
        if (readableMap.hasKey("endAt"))
            endAt = Long.parseLong(readableMap.getString("endAt"));

        if (readableMap.hasKey("priority"))
            priority = readableMap.getInt("priority");
        if (readableMap.hasKey("smallIcon"))
            smallIcon = readableMap.getString("smallIcon");
        if (readableMap.hasKey("largeIcon"))
            largeIcon = readableMap.getString("largeIcon");
        if (readableMap.hasKey("sound"))
            sound = readableMap.getString("sound");
        if (readableMap.hasKey("vibrate"))
            vibrate = readableMap.getString("vibrate");
        if (readableMap.hasKey("lights"))
            lights = readableMap.getString("lights");
        if (readableMap.hasKey("autoClear"))
            autoClear = readableMap.getBoolean("autoClear");
        else
            autoClear = true;
        if (readableMap.hasKey("onlyAlertOnce"))
            onlyAlertOnce = readableMap.getBoolean("onlyAlertOnce");
        if (readableMap.hasKey("tickerText"))
            tickerText = readableMap.getString("tickerText");
        if (readableMap.hasKey("when"))
            when = Long.parseLong(readableMap.getString("when"));
        if (readableMap.hasKey("bigText"))
            bigText = readableMap.getString("bigText");
        if (readableMap.hasKey("bigStyleUrlImage"))
            bigStyleUrlImage = readableMap.getString("bigStyleUrlImage");
        if (readableMap.hasKey("bigStyleImageBase64"))
            bigStyleImageBase64 = readableMap.getString("bigStyleImageBase64");
        if (readableMap.hasKey("subText"))
            subText = readableMap.getString("subText");
        if (readableMap.hasKey("progress"))
            progress = readableMap.getInt("progress");
        if (readableMap.hasKey("progressEnd"))
            progressEnd = readableMap.getInt("progressEnd");
        if (readableMap.hasKey("lifetime"))
            lifetime = readableMap.getInt("lifetime");

        if (readableMap.hasKey("color"))
            color = readableMap.getString("color");
        if (readableMap.hasKey("number"))
            number = readableMap.getInt("number");
        if (readableMap.hasKey("category"))
            category = readableMap.getString("category");
        if (readableMap.hasKey("localOnly"))
            localOnly = readableMap.getBoolean("localOnly");
        if (readableMap.hasKey("group"))
            group = readableMap.getString("group");

        if (readableMap.hasKey("inboxStyle")) {
            inboxStyle = true;
            ReadableMap inboxStyleMap = readableMap.getMap("inboxStyle");

            inboxStyleBigContentTitle = inboxStyleMap.getString("bigContentTitle");
            inboxStyleSummaryText = inboxStyleMap.getString("summaryText");

            ReadableArray inboxLines = inboxStyleMap.getArray("lines");
            if (inboxLines != null) {
                inboxStyleLines = new ArrayList<>();
                for (int i = 0; i < inboxLines.size(); i++) {
                    inboxStyleLines.add(inboxLines.getString(i));
                }
            }
        } else {
            inboxStyle = false;
        }

    }

    public ReadableMap asReadableMap() {
        WritableMap writableMap = new com.facebook.react.bridge.WritableNativeMap();

        if (id != null)
            writableMap.putInt("id", id);
        if (subject != null)
            writableMap.putString("subject", subject);
        if (message != null)
            writableMap.putString("message", message);
        if (action != null)
            writableMap.putString("action", action);
        if (payload != null)
            writableMap.putString("payload", payload);

        if (delayed != null)
            writableMap.putBoolean("delayed", delayed);
        if (delay != null)
            writableMap.putInt("delay", delay);

        if (scheduled != null)
            writableMap.putBoolean("scheduled", scheduled);
        if (sendAt != null)
            writableMap.putString("sendAt", Long.toString(sendAt));
        if (sendAtYear != null)
            writableMap.putInt("sendAtYear", sendAtYear);
        if (sendAtMonth != null)
            writableMap.putInt("sendAtMonth", sendAtMonth);
        if (sendAtDay != null)
            writableMap.putInt("sendAtDay", sendAtDay);
        if (sendAtWeekDay != null)
            writableMap.putInt("sendAtWeekDay", sendAtWeekDay);
        if (sendAtHour != null)
            writableMap.putInt("sendAtHour", sendAtHour);
        if (sendAtMinute != null)
            writableMap.putInt("sendAtMinute", sendAtMinute);

        if (repeatEvery != null)
            writableMap.putString("repeatEvery", repeatEvery);
        if (repeatType != null)
            writableMap.putString("repeatType", repeatType);
        if (repeatTime != null)
            writableMap.putInt("repeatTime", repeatTime);
        if (repeatCount != null)
            writableMap.putInt("repeatCount", repeatCount);
        if (endAt != null)
            writableMap.putString("endAt", Long.toString(endAt));

        if (priority != null)
            writableMap.putInt("priority", priority);
        if (smallIcon != null)
            writableMap.putString("smallIcon", smallIcon);
        if (largeIcon != null)
            writableMap.putString("largeIcon", largeIcon);
        if (sound != null)
            writableMap.putString("sound", sound);
        if (vibrate != null)
            writableMap.putString("vibrate", vibrate);
        if (lights != null)
            writableMap.putString("lights", lights);
        if (autoClear != null)
            writableMap.putBoolean("autoClear", autoClear);
        if (onlyAlertOnce != null)
            writableMap.putBoolean("onlyAlertOnce", onlyAlertOnce);
        if (tickerText != null)
            writableMap.putString("tickerText", tickerText);
        if (when != null)
            writableMap.putString("when", Long.toString(when));
        if (bigText != null)
            writableMap.putString("bigText", bigText);
        if (bigStyleImageBase64 != null)
            writableMap.putString("bigStyleImageBase64", bigStyleImageBase64);
        if (bigStyleUrlImage != null)
            writableMap.putString("bigStyleImageBase64", bigStyleUrlImage);
        if (subText != null)
            writableMap.putString("subText", subText);
        if (progress != null)
            writableMap.putInt("progress", progress);
        if (color != null)
            writableMap.putString("color", color);
        if (number != null)
            writableMap.putInt("number", number);
        if (category != null)
            writableMap.putString("category", category);
        if (localOnly != null)
            writableMap.putBoolean("localOnly", localOnly);
        if (group != null)
            writableMap.putString("group", group);

        if (progressEnd != null)
            writableMap.putInt("progressEnd", progressEnd);
        if (lifetime != null)
            writableMap.putInt("lifetime", lifetime);

        if (inboxStyle) {

            WritableMap inboxStyle = new com.facebook.react.bridge.WritableNativeMap();
            if (inboxStyleBigContentTitle != null)
                inboxStyle.putString("bigContentTitle", inboxStyleBigContentTitle);
            if (inboxStyleSummaryText != null)
                inboxStyle.putString("summaryText", inboxStyleSummaryText);

            if (inboxStyleLines != null) {
                WritableArray inboxLines = new com.facebook.react.bridge.WritableNativeArray();
                for (int i = 0; i < inboxStyleLines.size(); i++) {
                    inboxLines.pushString(inboxStyleLines.get(i));
                }
                inboxStyle.putArray("lines", inboxLines);
            }

            writableMap.putMap("inboxStyle", inboxStyle);
        }

        return writableMap;
    }
}
