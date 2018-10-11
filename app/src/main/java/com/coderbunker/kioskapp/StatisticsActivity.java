package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticsActivity extends Activity {
    private int countViewers = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        DatabaseConnection db = new DatabaseConnection();
        db.readViewer(new DatabaseConnection.OnViewersReceived() {
            @Override
            public void onViewersReceived(List<Viewer> viewers) {
                createViewerPerDayStatistic(viewers);
            }

            private void createViewerPerDayStatistic(List<Viewer> viewers) {
                int numberOfDaysFromCurrentMonth = new GregorianCalendar().getActualMaximum(Calendar.DATE);
                LineChartView lineChartView = findViewById(R.id.chart);
                List yAxisValues = new ArrayList();
                List axisValues = new ArrayList();
                Line line = new Line(yAxisValues);
                for (int dayOfMonthCounter = 1; dayOfMonthCounter <= numberOfDaysFromCurrentMonth; dayOfMonthCounter++) {
                    for (Viewer viewer : viewers) {
                        compareDayOfMonthCounterAndViewerDate(viewer, dayOfMonthCounter);
                    }
                    yAxisValues.add(new PointValue(dayOfMonthCounter - 1, countViewers));
                    axisValues.add(dayOfMonthCounter - 1, new AxisValue(dayOfMonthCounter - 1).setLabel(dayOfMonthCounter + ""));
                    countViewers = 0;
                }
                List lines = new ArrayList();
                lines.add(line);
                LineChartData data = new LineChartData();
                data.setLines(lines);

                configuratorXAxis(axisValues, data);
                configuratorYAxis(data);
                configuratorLineChartView(lineChartView, data);
            }

            private void configuratorLineChartView(LineChartView lineChartView, LineChartData data) {
                lineChartView.setLineChartData(data);
                Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                lineChartView.setMaximumViewport(viewport);
                lineChartView.setCurrentViewport(viewport);
            }

            private void configuratorYAxis(LineChartData data) {
                Axis yAxis = new Axis();
                yAxis.setName("Viewers");
                yAxis.setTextColor(Color.parseColor("#03A9F4"));
                yAxis.setTextSize(20);
                data.setAxisYLeft(yAxis);
            }

            private void configuratorXAxis(List axisValues, LineChartData data) {
                Axis axis = new Axis();
                axis.setName("Day of Month");
                axis.setValues(axisValues);
                axis.setTextSize(20);
                axis.setTextColor(Color.parseColor("#03A9F4"));
                data.setAxisXBottom(axis);
            }


            private void compareDayOfMonthCounterAndViewerDate(Viewer viewer, int dayOfMonthCounter) {
                try {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dateOfViewer = format.parse(viewer.getDateTime());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateOfViewer);
                    int monthOfViewer = cal.get(Calendar.MONTH) + 1;
                    int dayOfViewer = cal.get(Calendar.DAY_OF_MONTH);
                    int monthToday = new Date().getMonth() + 1;

                    if (monthOfViewer == monthToday && dayOfViewer == dayOfMonthCounter) {
                        countViewers++;
                    }
                } catch (ParseException e) {
                    System.out.println(e.getStackTrace());
                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(StatisticsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
