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
    private EditText edittext;
    private int watts;
    private float money;


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

    private void getPersistentData() throws IOException {
        Context context = getBaseContext();
        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            edittext.setText(line);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void putPersistentData() throws IOException {
        Context context = getBaseContext();
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(edittext.getText().toString());
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


        private Hamster hamster;
        private Resource water, coal, wind, solar;
        private Salesman salesman;
        private House house;

        private boolean isRunning = false;
        private long lastTime;

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

            lastTime = System.currentTimeMillis();

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
                    double elapsed = (now - lastTime) / 1000.0;
                    lastTime = now;

                    // update/draw
                    doUpdate(elapsed);
                    doDraw(canvas);

                    // compute and show FPS
                    updateFPS(now);

                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        // touch events
        @Override
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

    }




}
