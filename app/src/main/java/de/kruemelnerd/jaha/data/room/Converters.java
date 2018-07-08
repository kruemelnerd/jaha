package de.kruemelnerd.jaha.data.room;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;

public class Converters {

    @TypeConverter
    public static Calendar fromTimeStamp(Long value){
        if(value == null){
            return null;
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            return calendar;
        }
    }

    @TypeConverter
    public static Long dateToTimeStamp(Calendar date){
        return date == null ? null : date.getTime().getTime();
    }
}
