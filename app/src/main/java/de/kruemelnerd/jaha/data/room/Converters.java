package de.kruemelnerd.jaha.data.room;

import android.arch.persistence.room.TypeConverter;
import android.location.Location;

import java.util.Calendar;
import java.util.Locale;

public class Converters {

    @TypeConverter
    public static Calendar fromTimeStamp(Long value) {
        if (value == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            return calendar;
        }
    }

    @TypeConverter
    public static Long dateToTimeStamp(Calendar date) {
        return date == null ? null : date.getTime().getTime();
    }


    @TypeConverter
    public static String fromLocation(Location location) {
        if (location == null) {
            return (null);
        }

        return(String.format(Locale.US, "%f,%f", location.getLatitude(),
                location.getLongitude()));

    }

    @TypeConverter
    public static Location toLocation(String latlon) {
        if (latlon == null) {
            return (null);
        }

        String[] pieces = latlon.split(",");
        Location result = new Location("");

        result.setLatitude(Double.parseDouble(pieces[0]));
        result.setLongitude(Double.parseDouble(pieces[1]));

        return (result);
    }
}
