package com.example.elon.powerco;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;

public class AmenitiesActivity extends Activity {

    private int money;
    private ArrayList<Integer> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenities);

        Intent intent = getIntent();
        data = intent.getExtras().getIntegerArrayList("data");
        money = data.get(1);
    }

    public void onRunUpgrade(View view){

        int hamsterLevel = data.get(2);

        if(money >= 200 && hamsterLevel < 3) {
            money = money - 200;
            data.set(1, money);
            data.set(2, hamsterLevel + 1);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }

    }

    public void onWheelUpgrade(View view){

        int wheelLevel = data.get(3);

        if(money >= (300 * (wheelLevel + 1) * 2) && wheelLevel < 3) {

            money = money - 200;
            data.set(1, money);
            data.set(3, wheelLevel + 1);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }
    }

    public void onPriceUpgrade(View view){

        int price = data.get(9);

        if(money >= (1000) && price < 3) {

            money = money - 200;
            data.set(1, money);
            data.set(9, price + 1);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }
    }

    public void onSaleSpeedUpgrade(View view){

        int speed = data.get(8);

        if(money >= (1500) && speed < 3) {

            money = money - 200;
            data.set(1, money);
            data.set(8, speed + 1);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }
    }

    public void onHouseUpgrade(View view){

        int houseLevel = data.get(10);

        if(money >= 10000 && houseLevel < 3){
            money = money - 200;
            data.set(1, money);
            data.set(10, houseLevel + 1);

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }
    }

}
