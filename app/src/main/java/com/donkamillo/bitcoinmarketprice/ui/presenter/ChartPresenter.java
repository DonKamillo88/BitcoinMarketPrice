package com.donkamillo.bitcoinmarketprice.ui.presenter;

import android.content.Context;

import com.donkamillo.bitcoinmarketprice.services.BlockChain;
import com.donkamillo.bitcoinmarketprice.services.BlockChainResponse;
import com.donkamillo.bitcoinmarketprice.services.BlockChainService;
import com.donkamillo.bitcoinmarketprice.storage.SharedPreferencesManager;
import com.donkamillo.bitcoinmarketprice.ui.utils.DateUtils;
import com.donkamillo.bitcoinmarketprice.ui.activity.MainView;
import com.donkamillo.bitcoinmarketprice.ui.model.BitcoinPriceModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by DonKamillo on 09.05.2016.
 */
public class ChartPresenter implements Presenter, Callback<BlockChainResponse> {
    private Call<BlockChainResponse> call;
    private BlockChain blockChain;
    private MainView mainView;
    private Context context;

    @Override
    public void attachView(MainView view, Context context) {
        this.mainView = view;
        this.context = context;
    }

    @Override
    public void onFailure(Throwable t) {
        mainView.showError(t.getLocalizedMessage());
    }

    @Override
    public void onResponse(Response<BlockChainResponse> response, Retrofit retrofit) {
        List<BitcoinPriceModel> bitcoinPriceModels = new ArrayList<>();

        for (BlockChainResponse.Value venue : response.body().getValues()) {
            bitcoinPriceModels.add(new BitcoinPriceModel(venue.getX(), venue.getY()));
        }

        SharedPreferencesManager.saveHistoryData(bitcoinPriceModels, context);
        SharedPreferencesManager.saveLastDate(DateUtils.convertTime(bitcoinPriceModels.get(bitcoinPriceModels.size() - 1).getTimestamp() * 1000), context);

        mainView.setBitcoinPriceData(bitcoinPriceModels);
    }

    @Override
    public void onStop() {
        if (call != null)
            call.cancel();
    }

    /**
     * if there are data of this day, it reads data from the sharedPreferences, if not, it retrieves from the network
     */
    public void getBitcoinDate() {
        String lastSavedDate = SharedPreferencesManager.loadLastDate(context);
        Date today = new Date();

        if (lastSavedDate.equals(DateUtils.convertTime(today.getTime()))) {
            mainView.setBitcoinPriceData(SharedPreferencesManager.loadHistoryData(context));
        } else {
            call = getBlockChain().getData();
            call.enqueue(this);
        }
    }

    private BlockChain getBlockChain() {
        if (blockChain == null)
            blockChain = BlockChainService.getService();
        return blockChain;
    }

}
