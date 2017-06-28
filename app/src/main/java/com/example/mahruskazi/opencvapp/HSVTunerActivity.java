package com.example.mahruskazi.opencvapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by Mahrus Kazi on 2017-06-27.
 */

public class HSVTunerActivity extends Activity{

    private static final String TAG = "HSVTest";

    private int width;
    private int height;
    private static boolean isRunning = true;
    private static boolean started = false;

    private int[] curHueRange = new int[]{0, 180};
    private int[] curSatRange = new int[]{0, 255};
    private int[] curValRange = new int[]{0, 255};

    private static RangeSeekBar<Integer> hSeekBar;
    private static RangeSeekBar<Integer> sSeekBar;
    private static RangeSeekBar<Integer> vSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
        height = dm.heightPixels;

        getWindow().setLayout(width, (int)(height*0.33));

        hSeekBar = findViewById(R.id.h_seek_bar);
        hSeekBar.setRangeValues(0, 180);

        sSeekBar = findViewById(R.id.s_seek_bar);
        sSeekBar.setRangeValues(0, 255);

        vSeekBar = findViewById(R.id.v_seek_bar);
        vSeekBar.setRangeValues(0, 255);

        Bundle HSVData = getIntent().getExtras();
        if(HSVData != null){
            curHueRange = HSVData.getIntArray("Hue Data");
            curSatRange = HSVData.getIntArray("Sat Data");
            curValRange = HSVData.getIntArray("Val Data");
        }




        hSeekBar.setSelectedMinValue(curHueRange[0]);
        hSeekBar.setSelectedMaxValue(curHueRange[1]);

        sSeekBar.setSelectedMinValue(curSatRange[0]);
        sSeekBar.setSelectedMaxValue(curSatRange[1]);

        vSeekBar.setSelectedMinValue(curValRange[0]);
        vSeekBar.setSelectedMaxValue(curValRange[1]);

        started = true;

        Log.d(TAG, "In Create" + curHueRange[0]);
    }

    public void onResume(){
        super.onResume();

        Log.d(TAG, "In Resume");
    }

    public void onPause(){
        super.onPause();

        Log.d(TAG, "In Pause");
    }

    public void onStop(){
        super.onStop();
        Log.d(TAG, "In Stop");
    }

    public static int[] getHueRange(){
        return new int[]{hSeekBar.getSelectedMinValue(), hSeekBar.getSelectedMaxValue()};
    }

    public static int[] getSatRange(){
        return new int[]{sSeekBar.getSelectedMinValue(), sSeekBar.getSelectedMaxValue()};
    }

    public static int[] getValRange(){
        return new int[]{vSeekBar.getSelectedMinValue(), vSeekBar.getSelectedMaxValue()};
    }

    public static void setIsRunning(boolean state){
        isRunning = state;
    }

    public static boolean isPopUp(){
        return isRunning;
    }

    public static boolean hasStarted() { return started; }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.START | Gravity.BOTTOM;
        //lp.x = 10;
        //lp.y = 100;
        lp.width = width;
        lp.height = (int)(height*0.33);
        getWindowManager().updateViewLayout(view, lp);
    }
}
