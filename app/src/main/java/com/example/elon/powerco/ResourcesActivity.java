package com.example.elon.powerco;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import java.util.ArrayList;

public class ResourcesActivity extends Activity {
    private Intent intent;
    private ArrayList<Integer> data;
    private int money;
    private GameActivity gameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        intent = getIntent();
        data = intent.getExtras().getIntegerArrayList("data");
        money = data.get(1);
    }

    public void onWaterUpgrade(View view){

        int waterLevel = data.get(4);

        if(money >= (500 * waterLevel + 1) && waterLevel < 3) {
            money = money - (500 * waterLevel + 1);
            data.set(1, money);
            data.set(4, waterLevel + 1);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }

    }

    public void onWindUpgrade(View view){

        int windLevel = data.get(6);

        if(money >= (300 * windLevel + 1) && windLevel < 3) {

            money = money - (300 * windLevel + 1);
            data.set(1, money);
            data.set(6, windLevel + 1);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }

    }

    public void onSolarUpgrade(View view){

        int solarLevel = data.get(5);

        if(money >= (400 * solarLevel + 1) && solarLevel < 3) {

            money = money - (400 * solarLevel + 1);
            data.set(1, money);
            data.set(5, solarLevel + 1);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }
    }

    public void onCoalUpgrade(View view){

        int coalLevel = data.get(7);

        if(money >= (500)){

            money = money - 500;
            data.set(1, money);
            data.set(7, coalLevel);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }
    }

}
