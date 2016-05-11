package com.donkamillo.bitcoinmarketprice.services;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by DonKamillo on 09.05.2016.
 */
public interface BlockChain {

    @GET("/charts/market-price?format=json")
    Call<BlockChainResponse> getData();

}
