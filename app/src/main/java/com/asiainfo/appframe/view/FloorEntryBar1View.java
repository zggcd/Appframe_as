package com.asiainfo.appframe.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.asiainfo.appframe.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

/**
 * 图表层
 */
public class FloorEntryBar1View extends LinearLayout {

    private Context context;
    private LineChart chart_line;
    private LineChart chart_bezier;
    private BarChart chart_bar;

    public FloorEntryBar1View(Context context) {
        super(context);
        this.context = context;
        initView();
        initData();
    }

    public FloorEntryBar1View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloorEntryBar1View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.appframe_entry_bar1, this, true);
        chart_line =findViewById(R.id.chart_line);
        chart_bezier =findViewById(R.id.chart_bezier);
        chart_bar =findViewById(R.id.chart_bar);
    }

    private void initData(){

        chart_line = initChart(chart_line);
        setData1(12, 10);

        chart_bezier = initChart(chart_bezier);
        setData2(12, 10);

        chart_bar = initBarChart(chart_bar);
        setDataBarChart(10, 10);

    }

    private void setData1(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();
        ArrayList<Entry> values2 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            float val2 = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.appframe_bg_num)));
            values2.add(new Entry(i, val2, getResources().getDrawable(R.drawable.appframe_bg_num)));
        }

        LineDataSet set1;

        if (chart_line.getData() != null && chart_line.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)chart_line.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart_line.getData().notifyDataChanged();
            chart_line.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
//            set1 = new LineDataSet(values, "DataSet 1");
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(11f, 0f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.parseColor("#4382C8"));
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setDrawValues(false);
            set1.setCircleRadius(3f);
            set1.setDrawValues(false);
            set1.setDrawCircles(false);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{1f, 1f}, 0f));
            set1.setFormSize(15.f);
            set1.setMode(LineDataSet.Mode.LINEAR);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.appframe_shape_fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            LineDataSet set2;
            // create a dataset and give it a type
            set2 = new LineDataSet(values2, "");

            set2.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(11f, 0f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set2.setColor(Color.parseColor("#E0EBF7"));
            set2.setCircleColor(Color.BLACK);
            set2.setLineWidth(1f);
            set2.setCircleRadius(0f);
            set2.setDrawCircles(false);
            set2.setDrawCircleHole(false);
            set2.setDrawValues(false);
            set2.setValueTextSize(9f);
            set2.setDrawFilled(true);
            set2.setFormLineWidth(1f);
            set2.setFormLineDashEffect(new DashPathEffect(new float[]{1f, 1f}, 0f));
            set2.setFormSize(15.f);
            set2.setMode(LineDataSet.Mode.LINEAR);

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.appframe_shape_fade_gray);
            set2.setFillDrawable(drawable);


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            chart_line.setData(data);
        }
    }

    private void setData2(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.appframe_bg_num)));
        }

        LineDataSet set1;

        if (chart_bezier.getData() != null && chart_bezier.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)chart_bezier.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart_bezier.getData().notifyDataChanged();
            chart_bezier.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
//            set1 = new LineDataSet(values, "DataSet 1");
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(11f, 0f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.parseColor("#AE8AD8"));
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{1f, 1f}, 0f));
            set1.setFormSize(15.f);
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.appframe_shape_fade_purple);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            chart_bezier.setData(data);
        }
    }

    private LineChart initChart(LineChart chart){
        chart.getDescription().setEnabled(false);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        chart.getAxisRight().setEnabled(false);

        chart.animateX(2500);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);

        return chart;
    }

    private BarChart initBarChart(BarChart barChart){
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        barChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(5);
//        xAxis.setValueFormatter(xAxisFormatter);

//        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(5, false);
//        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setDrawLabels(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        return barChart;
    }

    private void setDataBarChart(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);

            if (Math.random() * 100 < 25) {
                yVals1.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.appframe_bg_num)));
            } else {
                yVals1.add(new BarEntry(i, val));
            }
        }

        BarDataSet set1;

        if (chart_bar.getData() != null &&
                chart_bar.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart_bar.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            chart_bar.getData().notifyDataChanged();
            chart_bar.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setFormLineWidth(20);

            int startColor1 = ContextCompat.getColor(context, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(context, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(context, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(context, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(context, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(context, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(context, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(context, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(context, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(context, android.R.color.holo_orange_dark);
//            set1.setBarBorderColor(endColor3);
            int color = Color.parseColor("#58AFFF");
            set1.setColor(color);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            chart_bar.setData(data);
        }
    }
}
