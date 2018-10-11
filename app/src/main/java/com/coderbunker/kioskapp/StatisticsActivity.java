package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class StatisticsActivity extends Activity {
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                GraphView graphView = configurateGraphView(numberOfDaysFromCurrentMonth);
                List<DataPoint> dataPoints = addDataToGraph(viewers, numberOfDaysFromCurrentMonth);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new
                        DataPoint[dataPoints.size()]));
                graphView.addSeries(series);
            }

            private List<DataPoint> addDataToGraph(List<Viewer> viewers, int numberOfDaysFromCurrentMonth) {
                List<DataPoint> dataPoints = new ArrayList<>();
                for (int dayOfMonthCounter = 1; dayOfMonthCounter <= numberOfDaysFromCurrentMonth; dayOfMonthCounter++) {
                    for (Viewer viewer : viewers) {
                        compareDayOfMonthCounterAndViewerDate(viewer, dayOfMonthCounter);
                    }
                    dataPoints.add(new DataPoint(dayOfMonthCounter, countViewers));
                    countViewers = 0;
                }
                return dataPoints;
            }

            private void compareDayOfMonthCounterAndViewerDate(Viewer viewer, int dayOfMonthCounter) {
                try {
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

            private GraphView configurateGraphView(int numOfDaysOfCurrentMonth) {
                GraphView graph = findViewById(R.id.graph);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(1);
                graph.getViewport().setMaxX(numOfDaysOfCurrentMonth);
                graph.setTitle("Viewers per Day");
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Days of Month");
                graph.getGridLabelRenderer().setVerticalAxisTitle("Viewers");
                graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(30);
                graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(30);
                graph.setTitleTextSize(50);
                return graph;
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
