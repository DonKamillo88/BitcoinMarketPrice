package com.donkamillo.bitcoinmarketprice.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.donkamillo.bitcoinmarketprice.R;
import com.donkamillo.bitcoinmarketprice.ui.utils.DateUtils;
import com.donkamillo.bitcoinmarketprice.ui.model.BitcoinPriceModel;
import com.donkamillo.bitcoinmarketprice.ui.presenter.ChartPresenter;
import com.donkamillo.bitcoinmarketprice.ui.view.LineChartView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String SELECTED_POINT = "selected_point";

    private RelativeLayout descriptions;
    private TextView priceTV, dateTV;
    private ProgressBar progressBar;
    private LineChartView lineChart;

    private ChartPresenter chartPresenter;
    private List<BitcoinPriceModel> bitcoinPriceModels;

    private int selectedPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            selectedPoint = savedInstanceState.getInt(SELECTED_POINT);
        }

        descriptions = (RelativeLayout) findViewById(R.id.descriptions);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        priceTV = (TextView) findViewById(R.id.price);
        dateTV = (TextView) findViewById(R.id.date);
        lineChart = (LineChartView) findViewById(R.id.linechart);

        initPresenter();
        initChart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showProgressBar(true);
        chartPresenter.getBitcoinDate();
        lineChart.showSelectedPoint(selectedPoint);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SELECTED_POINT, selectedPoint);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        chartPresenter.onStop();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setBitcoinPriceData(List<BitcoinPriceModel> bitcoinPriceModels) {
        this.bitcoinPriceModels = bitcoinPriceModels;
        lineChart.setChartData(getChartData(bitcoinPriceModels));

        showProgressBar(false);
    }

    private void initPresenter() {
        chartPresenter = new ChartPresenter();
        chartPresenter.attachView(this, this);
    }

    private void initChart() {
        lineChart.setChartData(new float[]{1, 10});
        lineChart.setLineChartEvent(new LineChartView.LineChartEvent() {
            @Override
            public void setSelectedPoint(int selectedPoint) {
                MainActivity.this.selectedPoint = selectedPoint;
                priceTV.setText(bitcoinPriceModels.get(selectedPoint).getPrice() + "");
                dateTV.setText(DateUtils.convertTime(bitcoinPriceModels.get(selectedPoint).getTimestamp() * 1000));
            }
        });
    }

    private void showProgressBar(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
        descriptions.setVisibility(b ? View.GONE : View.VISIBLE);
        lineChart.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    /**
     * convert array of bitcoinPriceModel on an array of prices
     *
     * @param bitcoinPriceModels
     * @return array of prices
     */
    private float[] getChartData(List<BitcoinPriceModel> bitcoinPriceModels) {
        int size = bitcoinPriceModels.size();
        float[] dataPoints = new float[size];
        for (int i = 0; i < size; i++) {
            dataPoints[i] = bitcoinPriceModels.get(i).getPrice();
        }

        return dataPoints;
    }

}
