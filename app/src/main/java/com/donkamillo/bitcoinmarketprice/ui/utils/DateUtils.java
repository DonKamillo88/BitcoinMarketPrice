package com.donkamillo.bitcoinmarketprice.ui.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DonKamillo on 10.05.2016.
 */
public class DateUtils {

    public static String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        return format.format(date);
    }
}
