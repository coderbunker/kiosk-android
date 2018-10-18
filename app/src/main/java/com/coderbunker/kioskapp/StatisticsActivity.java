package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.coderbunker.kioskapp.config.Configuration;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

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
    private final Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        DatabaseConnection db = new DatabaseConnection();
        db.readViewer(new DatabaseConnection.OnViewersReceived() {
            @Override
            public void onViewersReceived(List<Viewer> viewers) {
                String url = Configuration.loadFromPreferences(context).getUrl();
                createViewerPerDayStatistic(viewers, url);
                createStartTimeStatistics(viewers, url);
                createEndTimeStatistics(viewers, url);
            }


            private void createEndTimeStatistics(List<Viewer> viewers, String url) {

                BarChart barChart = findViewById(R.id.columnChartEndTime);
                ArrayList<BarEntry> entries = new ArrayList<>();
                final ArrayList<String> labels = new ArrayList<>();
                int highestValue = 0;
                int lowestValue = 0;
                for (Viewer viewer : viewers) {
                    int startTime = (int) Math.round(viewer.getEndTime());
                    if (startTime > highestValue) {
                        highestValue = startTime;
                    }
                    if (startTime < lowestValue) {
                        lowestValue = startTime;
                    }
                }
                int index = 0;
                for (int i = lowestValue; i < highestValue; i++) {
                    int counter = 0;
                    for (Viewer viewer : viewers) {
                        int startTime = (int) Math.round(viewer.getEndTime());
                        if (startTime == i && url.equals(viewer.getVideoUrl())) {
                            counter++;
                        }
                    }
                    if (counter > 0) {
                        labels.add(String.valueOf(i));
                        entries.add(new BarEntry(index, counter));
                        index++;
                    }
                }

                BarDataSet bardataset = new BarDataSet(entries, "End time of viewer");
                configureBarChart(barChart, labels, bardataset);

            }

            private void configureBarChart(BarChart barChart, final ArrayList<String> labels, BarDataSet bardataset) {
                BarData data = new BarData(bardataset);
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return labels.get((int) value);
                    }

                });
                barChart.setData(data);
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                barChart.animateY(4000);
                Description description = new Description();
                description.setText("");
                barChart.setDescription(description);
            }

            private void createStartTimeStatistics(List<Viewer> viewers, String url) {
                BarChart barChart = findViewById(R.id.columnChartStartTime);
                ArrayList<BarEntry> entries = new ArrayList<>();
                final ArrayList<String> labels = new ArrayList<>();
                int highestValue = 0;
                int lowestValue = 0;
                for (Viewer viewer : viewers) {
                    int startTime = (int) Math.round(viewer.getStartTime());
                    if (startTime > highestValue) {
                        highestValue = startTime;
                    }
                    if (startTime < lowestValue) {
                        lowestValue = startTime;
                    }
                }
                int index = 0;
                for (int i = lowestValue; i < highestValue; i++) {
                    int counter = 0;
                    for (Viewer viewer : viewers) {
                        int startTime = (int) Math.round(viewer.getStartTime());
                        if (startTime == i && url.equals(viewer.getVideoUrl())) {
                            counter++;
                        }
                    }
                    if (counter > 0) {
                        labels.add(String.valueOf(i));
                        entries.add(new BarEntry(index, counter));
                        index++;
                    }
                }

                BarDataSet bardataset = new BarDataSet(entries, "Start time of viewer");
                configureBarChart(barChart, labels, bardataset);
            }

            private void createViewerPerDayStatistic(List<Viewer> viewers, String url) {
                int numberOfDaysFromCurrentMonth = new GregorianCalendar().getActualMaximum(Calendar.DATE);
                LineChartView lineChartView = findViewById(R.id.chart);
                List yAxisValues = new ArrayList();
                List axisValues = new ArrayList();
                Line line = new Line(yAxisValues);
                for (int dayOfMonthCounter = 1; dayOfMonthCounter <= numberOfDaysFromCurrentMonth; dayOfMonthCounter++) {
                    for (Viewer viewer : viewers) {
                        compareDayOfMonthCounterAndViewerDate(viewer, dayOfMonthCounter, url);
                    }
                    yAxisValues.add(new PointValue(dayOfMonthCounter - 1, countViewers));
                    axisValues.add(dayOfMonthCounter - 1, new AxisValue(dayOfMonthCounter - 1).setLabel(dayOfMonthCounter + ""));
                    countViewers = 0;
                }
                List lines = new ArrayList();
                lines.add(line);
                LineChartData data = new LineChartData();
                data.setLines(lines);

                configuratorXAxisLineChart(axisValues, data);
                configuratorYAxisLineChart(data);
                configuratorLineChartView(lineChartView, data);
            }

            private void configuratorLineChartView(LineChartView lineChartView, LineChartData data) {
                lineChartView.setLineChartData(data);
                Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                lineChartView.setMaximumViewport(viewport);
                lineChartView.setCurrentViewport(viewport);
            }

            private void configuratorYAxisLineChart(LineChartData data) {
                Axis yAxis = new Axis();
                yAxis.setName("Viewers");
                yAxis.setTextColor(Color.parseColor("#03A9F4"));
                yAxis.setTextSize(15);
                data.setAxisYLeft(yAxis);
            }

            private void configuratorXAxisLineChart(List axisValues, LineChartData data) {
                Axis axis = new Axis();
                axis.setName("Day of Month");
                axis.setValues(axisValues);
                axis.setTextSize(15);
                axis.setTextColor(Color.parseColor("#03A9F4"));
                data.setAxisXBottom(axis);
            }


            private void compareDayOfMonthCounterAndViewerDate(Viewer viewer, int dayOfMonthCounter, String url) {
                try {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dateOfViewer = format.parse(viewer.getDateTime());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateOfViewer);
                    int monthOfViewer = cal.get(Calendar.MONTH) + 1;
                    int dayOfViewer = cal.get(Calendar.DAY_OF_MONTH);
                    int monthToday = new Date().getMonth() + 1;

                    if (monthOfViewer == monthToday && dayOfViewer == dayOfMonthCounter && url.equals(viewer.getVideoUrl())) {
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
