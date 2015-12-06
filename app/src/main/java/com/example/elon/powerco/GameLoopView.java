package com.example.elon.powerco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;


/**
 * Created by bhay on 11/30/2015.
 */
public class GameLoopView extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoopThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private final String filename = "myoutput.txt";
    private int watts;
    private float money;
    private TextView wattView, moneyView;
    private Hamster hamster;
    private Resource water, coal, wind, solar;
    private Salesman salesman;
    private House house;

    public GameLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // remember the context for finding resources
        this.context = context;

        // want to know when the surface changes
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);



        // game loop thread -- add a handler to update the TextView
        thread = new GameLoopThread(msgHandler);

    }

    Handler msgHandler = new Handler() {
        public void handleMessage(Message m) {
            wattView.setText(m.getData().getString("data"));
            moneyView.setText(m.getData().getString("data"));
        }
    };

    public void setWattView(TextView textView){this.wattView = textView;}

    public void setMoneyView(TextView textView){this.moneyView = textView;}

    public int getWatts() { return watts; }

    public void setWatts(int watts) { this.watts = watts; }

    public float getMoney() { return money; }

    public void setMoney(float money) { this.money = money; }

    public Hamster getHamster() {
        return hamster;
    }

    public void setHamster(Hamster hamster) {
        this.hamster = hamster;
    }

    public Resource getWater() {
        return water;
    }

    public void setWater(Resource water) {
        this.water = water;
    }

    public Resource getCoal() {
        return coal;
    }

    public void setCoal(Resource coal) {
        this.coal = coal;
    }

    public Resource getWind() {
        return wind;
    }

    public void setWind(Resource wind) {
        this.wind = wind;
    }

    public Resource getSolar() {
        return solar;
    }

    public void setSolar(Resource solar) {
        this.solar = solar;
    }

    public Salesman getSalesman() { return salesman; }

    public void setSalesman(Salesman salesman) { this.salesman = salesman; }

    public House getHouse() { return house; }

    public void setHouse(House house) { this.house = house; }


    private void getPersistentData(Context context) throws IOException {
        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            wattView.setText("Watts: " + watts);
            moneyView.setText("Money: $" + money);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void putPersistentData(Context context) throws IOException {
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // thread exists, but is in terminated state
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new GameLoopThread(msgHandler);
        }

        // start the game loop
        thread.setIsRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setIsRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    // Game Loop Thread
    private class GameLoopThread extends Thread {

        private boolean isRunning = false;
        private long lastTimeMoney, lastTimeResources;

        // frames per second calculation
        private int frames;
        private long nextUpdate;

        // the handler for updates to the TextView
        private Handler handler;



        public GameLoopThread(Handler handler) {

            this.handler = handler;

            hamster = new Hamster(context);
            hamster.setHamsterLevel(0);
            hamster.setWheelLevel(0);

            water = new Resource(context);
            water.setType("water");
            water.setLevel(0);

            coal = new Resource(context);
            coal.setType("coal");
            coal.setLevel(0);

            wind = new Resource(context);
            wind.setType("wind");
            wind.setLevel(0);

            solar = new Resource(context);
            solar.setType("solar");
            solar.setLevel(0);

        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        // the main loop
        @Override
        public void run() {


            lastTimeMoney = System.currentTimeMillis();
            lastTimeResources = System.currentTimeMillis();

            while (isRunning) {

                // grab hold of the canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    // trouble -- exit nicely
                    isRunning = false;
                    continue;
                }

                synchronized (surfaceHolder) {

                    // compute how much time since last time around
                    long now = System.currentTimeMillis();



                    if(now >= lastTimeMoney + 10000){
                        lastTimeMoney = now;
                        doUpdateMoney(salesman);
                    }

                    if(now >= lastTimeResources + 1000){
                        lastTimeResources = now;
                        doUpdateResource(water);
                        doUpdateResource(solar);
                        doUpdateResource(wind);
                        doUpdateResource(coal);
                    }

                    doDraw(canvas);


                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        // touch events
        public boolean onTouchEvent(MotionEvent event) {

            watts = watts + (hamster.getHamsterLevel()*hamster.getHamsterLevel());


            return true;
        }

    }

    /* THE GAME */

    // move all objects in the game
    private void doUpdateResource(Resource resource) {

        if(resource.getType().equals("water")){
            watts = watts + (resource.getLevel()*10);
        }

        if(resource.getType().equals("solar")){
            watts = watts + (resource.getLevel()*7);
        }

        if(resource.getType().equals("wind")){
            watts = watts + (resource.getLevel()*4);
        }

        if(resource.getType().equals("coal")){
            watts = watts + (resource.getLevel()*200);
            if(resource.getLevel() > 0) {
                resource.setLevel(resource.getLevel() - 1);
            }
        }
    }

    private void doUpdateMoney(Salesman salesman) {

        if(watts >= salesman.getSpeed()){
            watts = watts - (int)salesman.getSpeed();
            money = money + (salesman.getSpeed() * salesman.getPrice());
        }
    }

    // draw all objects in the game
    private void doDraw(Canvas canvas) {
        hamster.doDraw(canvas);
        water.doDraw(canvas);
        solar.doDraw(canvas);
        wind.doDraw(canvas);
        coal.doDraw(canvas);
        house.doDraw(canvas);
        salesman.doDraw(canvas);
    }




}
