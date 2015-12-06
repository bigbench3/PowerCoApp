package com.example.elon.powerco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by bhay on 12/5/2015.
 */
public class Salesman {
    private int speed, price;
    private int level;
    private float width, height;
    private Bitmap bitmap;
    private int screenWidth, screenHeight;
    private final float SCALE = 0.1f;
    private ArrayList<Integer> salesman;

    public Salesman(Context context){

        salesman = new ArrayList<>();

        salesman.add(R.drawable.salesman0);
        salesman.add(R.drawable.salesman1);
        salesman.add(R.drawable.salesman2);
        salesman.add(R.drawable.salesman3);

        bitmap = BitmapFactory.decodeResource(context.getResources(), salesman.get(level));

        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

        // figure out the screen width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // start in top/left corner
        x = screenWidth/4;
        y = screenHeight/4;
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
