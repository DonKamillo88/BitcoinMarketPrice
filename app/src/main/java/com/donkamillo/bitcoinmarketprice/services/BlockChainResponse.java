package com.donkamillo.bitcoinmarketprice.services;

import java.util.List;

/**
 * Created by DonKamillo on 09.05.2016.
 */
public class BlockChainResponse {

    private List<Value> values;

    public class Value {
        private long x; //Unix timestamp
        private float y; // price value

        public long getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    public List<Value> getValues() {
        return values;
    }
}
