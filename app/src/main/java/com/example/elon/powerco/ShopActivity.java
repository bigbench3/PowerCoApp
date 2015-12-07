package com.example.elon.powerco;

import android.app.Activity;
import android.app.Fragment;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.lang.reflect.Type;

public class ShopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

    }

    public void onAmenitiesClick(View view){

        Intent intent = new Intent(this, AmenitiesActivity.class);
        startActivity(intent);
    }

    public void onResourcesClick(View view){

        Intent intent = new Intent(this, ResourcesActivity.class);
        startActivity(intent);
    }





    public void onWaterUpgrade(){

    }

    public void onWindUpgrade(){

    }

    public void onSolarUpgrade(){

    }

    public void onCoalUpgrade(){

    }

}
