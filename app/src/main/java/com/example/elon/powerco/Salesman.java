package com.example.elon.powerco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by bhay on 12/5/2015.
 */
public class Salesman {
    private int speed, price;
    private int level;
    private float width, height, x, y;
    private Bitmap bitmap;
    private int screenWidth, screenHeight;
    private final float SCALE = 0.1f;
    private ArrayList<Integer> salesman;

    public Salesman(Context context){

    }


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
