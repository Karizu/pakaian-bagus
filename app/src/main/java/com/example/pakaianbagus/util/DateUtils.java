package com.example.pakaianbagus.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alfianhpratama on 18/09/2019.
 * Organization: UTeam
 */

@SuppressLint("SimpleDateFormat")
public class DateUtils {
    public String formatDateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public String getTodayWithFormat(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }

    public Date formatStringToDate(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String formatDateStringToString(String date, String oldFormat, String newFormat) {
        Date newDate = formatStringToDate(date, oldFormat);
        SimpleDateFormat dateFormat = new SimpleDateFormat(newFormat);
        return dateFormat.format(newDate);
    }

}
