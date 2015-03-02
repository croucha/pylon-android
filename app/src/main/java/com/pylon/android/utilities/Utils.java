package com.pylon.android.utilities;

import android.text.format.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by croucha on 2/11/15.
 */
public class Utils {

    /**
     * Coverts ISO 8601 string to relative time (10 seconds ago, 5 minutes ago etc).
     *
     * @param iso8601String
     * @return
     */
    public static String convertIso8601ToRelativeTime(String iso8601String) {
        // Define time zone
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        // Define Atom (ISO 8601) format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        // Set time zone
        dateFormat.setTimeZone(timeZone);
        // Define date
        Date date = new Date();
        // Attempt to parse data as ISO 8601
        try {
            date = dateFormat.parse(iso8601String);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Format time
        CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
            date.getTime(),
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        );
        return relativeTime.toString();
    }
}