package com.example.elon.powerco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class StartActivity extends Activity {

    private boolean loadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    protected void onNewClick(){
        loadData = false;
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("loadData", loadData);
        startActivity(intent);


    }

    protected void onContinueClick(){
        loadData = true;
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("loadData", loadData);
        startActivity(intent);
    }
}
