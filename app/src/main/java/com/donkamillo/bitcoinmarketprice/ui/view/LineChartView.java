package com.donkamillo.bitcoinmarketprice.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.donkamillo.bitcoinmarketprice.R;

/**
 * Created by DonKamillo on 09.05.2016.
 */
public class LineChartView extends View {

    public interface LineChartEvent {
        void setSelectedPoint(int selectedPoint);
    }

    private static final String TAG = "LineChartView";
    private static final int MIN_LINES = 4;
    private static final int MAX_LINES = 7;
    private static final int[] DISTANCES = {1, 2, 5};

    private float[] dataPoints = new float[]{};
    private Paint backgroundPaint, pointPaint, linePaint;

    private LineChartEvent lineChartEvent;
    private Float selectedXPoint;
    private int selectedPoint;

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLineChartEvent(LineChartEvent lineChartEvent) {
        this.lineChartEvent = lineChartEvent;
    }

    public void setChartData(float[] dataPoints) {
        this.dataPoints = dataPoints.clone();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (dataPoints.length == 0) {
            return;
        }
        drawBackground(canvas);
        drawLineChart(canvas);
        drawSelectedPoint(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectedXPoint = x;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                selectedXPoint = x;
                invalidate();
                break;
        }
        return true;
    }

    public void showSelectedPoint(int selectedPoint) {
        this.selectedPoint = selectedPoint;
        invalidate();
    }

    private void drawBackground(Canvas canvas) {
        float maxValue = getMax(dataPoints);
        int range = getLineDistance(maxValue);

        for (int y = 0; y < maxValue; y += range) {
            final float yPos = getYPos(y);
            canvas.drawLine(0, yPos, getWidth(), yPos, getBackgroundPaint());
            canvas.drawText(String.valueOf(y), getPaddingLeft(), yPos - 2, getBackgroundPaint());
        }
    }

    private void drawSelectedPoint(Canvas canvas) {
        if (selectedPoint == 0) {
            return;
        }
        float xPos = getXPos(selectedPoint);
        float yPos = getYPos(dataPoints[selectedPoint]);

        canvas.drawPoint(xPos, yPos, getPointPaint());
        lineChartEvent.setSelectedPoint(selectedPoint);
    }

    private void drawLineChart(Canvas canvas) {
        float xPos, yPos;
        float lastXPos = getXPos(0), lastYPos = getYPos(dataPoints[0]);

        Path path = new Path();
        path.moveTo(lastXPos, lastYPos);

        for (int i = 1; i < dataPoints.length; i++) {

            xPos = getXPos(i);
            yPos = getYPos(dataPoints[i]);

            if (selectedXPoint != null && lastXPos <= selectedXPoint && xPos > selectedXPoint) {
                if (selectedXPoint - lastXPos > xPos - selectedXPoint) {
                    selectedPoint = i;
                } else {
                    selectedPoint = i - 1;
                }
            }

            path.lineTo(xPos, yPos);
            lastXPos = xPos;
        }
        canvas.drawPath(path, getLinePaint());
    }

    private float getMax(float[] array) {
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private float getYPos(float value) {
        float height = getHeight() - getPaddingTop() - getPaddingBottom();
        float maxValue = getMax(dataPoints);

        // scale it to the view size
        value = (value / maxValue) * height;

        // invert it so that higher values have lower y
        value = height - value;

        // offset it to adjust for padding
        value += getPaddingTop();

        return value;
    }

    private float getXPos(float value) {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float maxValue = dataPoints.length - 1;

        // scale it to the view size
        value = (value / maxValue) * width;

        // offset it to adjust for padding
        value += getPaddingLeft();

        return value;
    }

    /**
     * calculates the distance between the lines on the chart
     * @param maxValue - the greatest value from data
     *
     */
    private int getLineDistance(float maxValue) {
        int distance;
        int distanceIndex = 0;
        int distanceMultiplier = 1;
        int numberOfLines;

        do {
            distance = DISTANCES[distanceIndex] * distanceMultiplier;
            numberOfLines = (int) Math.ceil(maxValue / distance);

            distanceIndex++;
            if (distanceIndex == DISTANCES.length) {
                distanceIndex = 0;
                distanceMultiplier *= 10;
            }
        } while (numberOfLines < MIN_LINES || numberOfLines > MAX_LINES);

        return distance;
    }

    private Paint getPointPaint() {
        if (pointPaint == null) {
            pointPaint = new Paint();
            pointPaint.setStyle(Style.STROKE);
            pointPaint.setColor(getResources().getColor(R.color.colorAccent));
            pointPaint.setStrokeWidth(15);
        }
        return pointPaint;
    }


    private Paint getLinePaint() {
        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setStyle(Style.STROKE);
            linePaint.setStrokeWidth(4);
            linePaint.setColor(getResources().getColor(R.color.chart_line));
            linePaint.setAntiAlias(true);
            linePaint.setShadowLayer(4, 2, 2, 0x80000000);
            linePaint.setShadowLayer(0, 0, 0, 0);
        }
        return linePaint;
    }

    private Paint getBackgroundPaint() {
        if (backgroundPaint == null) {
            backgroundPaint = new Paint();
            backgroundPaint.setStyle(Style.STROKE);
            backgroundPaint.setColor(getResources().getColor(R.color.chart_background_line));
            backgroundPaint.setTextAlign(Paint.Align.LEFT);
            backgroundPaint.setTextSize(16);
            backgroundPaint.setStrokeWidth(1);
        }
        return backgroundPaint;
    }

}