package com.donkamillo.bitcoinmarketprice.ui.presenter;

import android.content.Context;

import com.donkamillo.bitcoinmarketprice.ui.activity.MainView;

/**
 * Created by DonKamillo on 09.05.2016.
 */
interface Presenter {

    void attachView(MainView mainView, Context context);

    void onStop();
}
