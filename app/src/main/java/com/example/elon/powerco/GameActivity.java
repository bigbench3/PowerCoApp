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
    private  TextView wattView;
    private TextView moneyView;
    private GameLoopView gameLoopView;
    private boolean loadData, shop, fromStart;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        context = getBaseContext();
        Intent intent = getIntent();
        fromStart = intent.getExtras().getBoolean("fromStart");
        loadData = intent.getExtras().getBoolean("loadData");
        wattView = (TextView) findViewById(R.id.wattView);
        moneyView = (TextView) findViewById(R.id.moneyView);
        gameLoopView = (GameLoopView) findViewById(R.id.GameLoopView);
        gameLoopView.setWattView(wattView);
        gameLoopView.setMoneyView(moneyView);

        if(!loadData && fromStart){

            System.out.println("It gets here");

            gameLoopView.resetGame();

            try{

                gameLoopView.putPersistentData(context);

            } catch (Exception e){

            }

            fromStart = false;
        }

    }

    public void onShop(View view){
        Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("data", gameLoopView.getData());
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<Integer> stats = data.getIntegerArrayListExtra("data");
        gameLoopView.setData(stats);

        try {
            gameLoopView.putPersistentData(context);
        }catch (Exception e){

        }
    }
}
