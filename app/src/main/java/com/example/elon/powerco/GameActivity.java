package com.example.elon.powerco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class GameActivity extends Activity {

    private Context context;
    private Resource water, wind, solar, coal;
    private Hamster hamster;
    private Salesman salesman;
    private final String filename = "myoutput.txt";
    ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        context = getBaseContext();
        Intent intent = getIntent();
        Boolean loadData = intent.getExtras().getBoolean("loadData");
        TextView wattView = (TextView) findViewById(R.id.wattView);
        TextView moneyView = (TextView) findViewById(R.id.moneyView);
        GameLoopView gameLoopView = (GameLoopView) findViewById(R.id.GameLoopView);
        gameLoopView.setWattView(wattView);
        gameLoopView.setMoneyView(moneyView);

        water = new Resource(context);
        wind = new Resource(context);
        solar = new Resource(context);
        coal = new Resource(context);
        hamster = new Hamster(context);
        salesman = new Salesman(context);


        if(loadData){
            getPersistentData();

            int hamsterLevel = Integer.parseInt(data.get(0));
            int wheelLevel = Integer.parseInt(data.get(1));
            hamster.setHamsterLevel(hamsterLevel);
            hamster.setWheelLevel(wheelLevel);

            int salesPrice = Integer.parseInt(data.get(2));
            int salesSpeed = Integer.parseInt(data.get(3));
            salesman.setPrice(salesPrice);
            salesman.setSpeed(salesSpeed);

            int waterLevel = Integer.parseInt(data.get(4));
            int windLevel = Integer.parseInt(data.get(5));
            int solarLevel = Integer.parseInt(data.get(6));
            int coalLevel = Integer.parseInt(data.get(7));
            water.setLevel(waterLevel);
            wind.setLevel(windLevel);
            solar.setLevel(solarLevel);
            coal.setLevel(coalLevel);

            int currentWatts = Integer.parseInt(data.get(8));
            int currentMoney = Integer.parseInt(data.get(9));
            gameLoopView.setWatts(currentWatts);
            gameLoopView.setMoney(currentMoney);

        } else {
            hamster.setHamsterLevel(0);
            hamster.setWheelLevel(0);

            salesman.setPrice(1);
            salesman.setSpeed(10);

            water.setType("water");
            water.setLevel(0);

            wind.setType("wind");
            wind.setLevel(0);

            solar.setType("solar");
            solar.setLevel(0);

            coal.setType("coal");
            coal.setLevel(0);
        }

        gameLoopView.setHamster(hamster);
        gameLoopView.setSalesman(salesman);
        gameLoopView.setWater(water);
        gameLoopView.setWind(wind);
        gameLoopView.setSolar(solar);
    }

    public void onShop(){
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    private void getPersistentData() {
        Context context = getBaseContext();
        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            data.add(line);
        } catch (IOException e){
            System.out.println(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e){
                System.out.println(e);
            }
        }
    }
}
