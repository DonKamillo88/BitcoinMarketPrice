package com.donkamillo.bitcoinmarketprice.ui.model;

/**
 * Created by DonKamillo on 09.05.2016.
 */
public class BitcoinPriceModel {

    private final long timestamp;
    private final float price;

    public BitcoinPriceModel(long timestamp, float price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getPrice() {
        return price;
    }


}
