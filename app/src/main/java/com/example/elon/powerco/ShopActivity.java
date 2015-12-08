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
import java.util.ArrayList;

public class ShopActivity extends Activity {

    private ArrayList<Integer> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent = getIntent();
        data = intent.getExtras().getIntegerArrayList("data");
    }

    public void onAmenitiesClick(View view){

        Intent intent = new Intent(this, AmenitiesActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    public void onResourcesClick(View view){

        Intent intent = new Intent(this, ResourcesActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }


}
