package com.example.mahruskazi.opencvapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by Mahrus Kazi on 2017-06-28.
 */

public class FilterSettings extends Activity {

    private EditText minAreaField;
    private EditText minPerimeterField;

    private RangeSeekBar minMaxWidth;
    private RangeSeekBar minMaxHeight;
    private RangeSeekBar minMaxSolidity;
    private RangeSeekBar minMaxVerticies;
    private RangeSeekBar minMaxRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter_settings);

        minAreaField = findViewById(R.id.min_area);
        minPerimeterField = findViewById(R.id.min_perimeter);

        minMaxWidth = findViewById(R.id.min_max_width);
        minMaxHeight = findViewById(R.id.min_max_height);
        minMaxSolidity = findViewById(R.id.min_max_solidity);
        minMaxVerticies = findViewById(R.id.min_max_verticies);
        minMaxRatio = findViewById(R.id.min_max_ratio);

        minMaxWidth.setRangeValues(0, 1000);
        minMaxHeight.setRangeValues(0, 1000);
        minMaxSolidity.setRangeValues(0, 100);
        minMaxVerticies.setRangeValues(0, 1000000);
        minMaxRatio.setRangeValues(0, 1000);


    }
}
