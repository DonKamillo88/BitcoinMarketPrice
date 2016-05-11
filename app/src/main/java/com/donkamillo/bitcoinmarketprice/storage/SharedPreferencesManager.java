package com.donkamillo.bitcoinmarketprice.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.donkamillo.bitcoinmarketprice.ui.model.BitcoinPriceModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DonKamillo on 10.05.2016.
 */
public class SharedPreferencesManager {
    private static final String HISTORY_DATA = "history_data";
    private static final String LAST_DATE = "last_date";

    static public void saveHistoryData(List<BitcoinPriceModel> list, Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(list);

        editor.putString(HISTORY_DATA, json);
        editor.apply();
    }

    static public List<BitcoinPriceModel> loadHistoryData(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(HISTORY_DATA, null);
        Type type = new TypeToken<ArrayList<BitcoinPriceModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    static public void saveLastDate(String date, Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString(LAST_DATE, date);
        editor.apply();
    }

    static public String loadLastDate(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(LAST_DATE, "");
    }


}
