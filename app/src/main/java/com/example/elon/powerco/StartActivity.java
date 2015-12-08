package com.example.elon.powerco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class StartActivity extends Activity {

    private boolean loadData, fromStart;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        intent = getIntent();
    }

    public void onNewClick(View view){
        loadData = false;
        fromStart = true;
        intent = new Intent(this, GameActivity.class);
        intent.putExtra("loadData", loadData);
        intent.putExtra("fromStart", fromStart);
        startActivity(intent);
        finish();
    }

    public void onContinueClick(View view){
        loadData = true;
        fromStart = true;
        intent = new Intent(this, GameActivity.class);
        intent.putExtra("loadData", loadData);
        intent.putExtra("fromStart", fromStart);
        startActivity(intent);
        finish();
    }
}
