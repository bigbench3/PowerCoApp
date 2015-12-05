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

    // Game Loop Thread
    private class GameLoopThread extends Thread {

        private boolean isRunning = false;
        private long lastTime;

        // the bird sprite
        private Bird bird;

        // the clouds
        private final int NUM_CLOUDS = 3;
        private ArrayList<Cloud> clouds;

        // frames per second calculation
        private int frames;
        private long nextUpdate;

        // the handler for updates to the TextView
        private Handler handler;

        public GameLoopThread(Handler handler) {

            this.handler = handler;

            bird = new Bird(context);

            clouds = new ArrayList<Cloud>();
            for (int i = 0; i < NUM_CLOUDS; i++) {
                clouds.add(new Cloud(context));
            }

            touchX = bird.x;
            touchY = bird.y;
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }
    }
}
