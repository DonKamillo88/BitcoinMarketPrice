package com.donkamillo.bitcoinmarketprice.ui.activity;

import com.donkamillo.bitcoinmarketprice.ui.model.BitcoinPriceModel;

import java.util.List;

/**
 * Created by DonKamillo on 09.05.2016.
 */
public interface MainView {

    void showError(String errorMessage);

    void setBitcoinPriceData(List<BitcoinPriceModel> bitcoinPriceModels);
}
