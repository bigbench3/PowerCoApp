package com.example.elon.powerco;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class AmenitiesActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenities);

    }

    public void onRunUpgrade(View view){
        GameLoopView loop = (GameLoopView)findViewById(R.id.GameLoopView);
        Hamster hamster = loop.getHamster();
        hamster.setHamsterLevel(hamster.getHamsterLevel() + 1);
        loop.setHamster(hamster);
    }

    public void onWheelUpgrade(View view){
        GameLoopView loop = (GameLoopView)findViewById(R.id.GameLoopView);
        Hamster hamster = loop.getHamster();
        hamster.setWheelLevel(hamster.getWheelLevel() + 1);
        loop.setHamster(hamster);
    }

    public void onPriceUpgrade(View view){
        GameLoopView loop = (GameLoopView)findViewById(R.id.GameLoopView);
        Salesman salesman = loop.getSalesman();
        salesman.setPrice((int)salesman.getPrice() + 1);
        loop.setSalesman(salesman);
    }

    public void onSaleSpeedUpgrade(View view){
        GameLoopView loop = (GameLoopView)findViewById(R.id.GameLoopView);
        Salesman salesman = loop.getSalesman();
        salesman.setSpeed((int)salesman.getSpeed() + 10);
        loop.setSalesman(salesman);
    }

    public void onHouseUpgrade(View view){
        GameLoopView loop = (GameLoopView)findViewById(R.id.GameLoopView);
        House house = loop.getHouse();
        house.setHouseLevel(house.getHouseLevel() + 1);
        loop.setHouse(house);
    }

}
