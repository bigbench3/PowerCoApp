package com.example.elon.powerco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class GameActivity extends Activity {

    private Context context;
    private final String filename = "myoutput.txt";
    ArrayList<String> data = new ArrayList<String>();
    private  TextView wattView;
    private TextView moneyView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        context = getBaseContext();
        Intent intent = getIntent();
        Boolean loadData = intent.getExtras().getBoolean("loadData");
        wattView = (TextView) findViewById(R.id.wattView);
        moneyView = (TextView) findViewById(R.id.moneyView);
        GameLoopView gameLoopView = (GameLoopView) findViewById(R.id.GameLoopView);
        gameLoopView.setWattView(wattView);
        gameLoopView.setMoneyView(moneyView);

        if(!loadData){
            gameLoopView.setReset(true);
        }
    }

    public void onShop(View view){
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

}
