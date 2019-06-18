package com.example.eatify1506;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeRelated {
    public TimeRelated() {
    }

    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {

        long diffInMillies = date2.getTime() - date1.getTime();

        //create the list
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        //create the result map of TimeUnit and difference
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;

        for ( TimeUnit unit : units ) {

            //calculate difference in millisecond
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;

            //put the result in the map
            result.put(unit,diff);
        }

        return result;
    }


    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static String getAge(Date createdAt) {

        Date now = Calendar.getInstance().getTime();

        long days = TimeRelated.getDateDiff(createdAt, now, TimeUnit.DAYS);
        long hours = TimeRelated.getDateDiff(createdAt, now, TimeUnit.HOURS);
        long minutes = TimeRelated.getDateDiff(createdAt, now, TimeUnit.MINUTES);
        long seconds = TimeRelated.getDateDiff(createdAt, now, TimeUnit.SECONDS);

        String time_diff_str;
        if (days > 0) {
            time_diff_str = days + "g";
        } else if (hours > 0) {
            time_diff_str = hours + "sa";
        } else if (minutes > 0) {
            time_diff_str = minutes + "dk";
        } else if (seconds > 0) {
            time_diff_str = seconds + "sn";
        } else {
            time_diff_str = "unknown time val";
        }

        return time_diff_str;
    }

}
