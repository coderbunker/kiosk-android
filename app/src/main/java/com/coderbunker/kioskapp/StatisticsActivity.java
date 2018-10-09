package com.coderbunker.kioskapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import java.util.List;

public class StatisticsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        DatabaseConnection db = new DatabaseConnection();
        db.readViewer(new DatabaseConnection.OnViewersReceived() {
            @Override
            public void onViewersReceived(List<Viewer> viewers) {
                for (Viewer v : viewers) {
                    System.out.println(v.getStartTime() + " " + v.getEndTime());
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
