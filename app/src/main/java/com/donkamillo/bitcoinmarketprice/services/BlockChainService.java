package com.donkamillo.bitcoinmarketprice.services;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by DonKamillo on 09.05.2016.
 */
public class BlockChainService {

    private static final String API_URL = "https://blockchain.info";

    public static BlockChain getService() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(BlockChain.class);
    }

}
