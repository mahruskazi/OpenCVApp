package com.example.mahruskazi.opencvapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by Mahrus Kazi on 2017-06-28.
 */

public class FilterSettings extends Activity {

    private static final String TAG = "FilterSettings";

    private EditText minAreaField;
    private EditText minPerimeterField;

    private RangeSeekBar<Double> minMaxWidth;
    private RangeSeekBar<Double> minMaxHeight;
    private RangeSeekBar<Double> minMaxSolidity;
    private RangeSeekBar<Double> minMaxVertices;
    private RangeSeekBar<Double> minMaxRatio;

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter_settings);

        minAreaField = findViewById(R.id.min_area);
        minPerimeterField = findViewById(R.id.min_perimeter);

        minAreaField.setHint("Minimum Area (30)");
        minAreaField.setText(""+VisionPipeline.currentSettings()[0][0]);
        minPerimeterField.setHint("Minimum Perimeter (0)");
        minPerimeterField.setText(""+VisionPipeline.currentSettings()[0][1]);

        minMaxWidth = findViewById(R.id.min_max_width);
        minMaxHeight = findViewById(R.id.min_max_height);
        minMaxSolidity = findViewById(R.id.min_max_solidity);
        minMaxVertices = findViewById(R.id.min_max_vertices);
        minMaxRatio = findViewById(R.id.min_max_ratio);

        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(
               new View.OnClickListener(){
                   public void onClick(View v){
                       buttonClicked(v);
                   }
               }
        );

        minMaxWidth.setRangeValues(0.0, 1000.0);
        minMaxHeight.setRangeValues(0.0, 1000.0);
        minMaxSolidity.setRangeValues(0.0, 100.0);
        minMaxVertices.setRangeValues(0.0, 1000000.0);
        minMaxRatio.setRangeValues(0.0, 1000.0);

        minMaxWidth.setSelectedMinValue(VisionPipeline.currentSettings()[1][0]);
        minMaxWidth.setSelectedMaxValue(VisionPipeline.currentSettings()[1][1]);

        minMaxHeight.setSelectedMinValue(VisionPipeline.currentSettings()[2][0]);
        minMaxHeight.setSelectedMaxValue(VisionPipeline.currentSettings()[2][1]);

        minMaxSolidity.setSelectedMinValue(VisionPipeline.currentSettings()[3][0]);
        minMaxSolidity.setSelectedMaxValue(VisionPipeline.currentSettings()[3][1]);

        minMaxVertices.setSelectedMinValue(VisionPipeline.currentSettings()[4][0]);
        minMaxVertices.setSelectedMaxValue(VisionPipeline.currentSettings()[4][1]);

        minMaxRatio.setSelectedMinValue(VisionPipeline.currentSettings()[5][0]);
        minMaxRatio.setSelectedMaxValue(VisionPipeline.currentSettings()[5][1]);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "In pause");
    }

    public void onStop(){
        super.onStop();
        Log.d(TAG, "In Stop");
    }

    public void buttonClicked(View v){
        VisionPipeline.changeSettings(getSettings());
        Intent launchActivity = new Intent(this, MainActivity.class);
        launchActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(launchActivity);
        finish();
    }

    public double[][] getSettings(){
        double[][] settings = new double[][]{{minArea(), minPerimeter()},
                                        widthRange(),
                                        heightRange(),
                                        solidityRange(),
                                        verticesRange(),
                                        ratioRange()};

        return settings;
    }

    public double minArea(){
        if(!minAreaField.getText().toString().contentEquals(""))
            return Double.parseDouble(minAreaField.getText().toString());
        else
            return VisionPipeline.MIN_AREA;
    }

    public double minPerimeter(){
        if(!minPerimeterField.getText().toString().contentEquals(""))
            return Double.parseDouble(minPerimeterField.getText().toString());
        else
            return VisionPipeline.MIN_PERIMETER;
    }

    public double[] widthRange(){
        return new double[]{(double) minMaxWidth.getSelectedMinValue(), (double) minMaxWidth.getSelectedMaxValue()};
    }

    public double[] heightRange(){
        return new double[]{(double) minMaxHeight.getSelectedMinValue(), (double)minMaxHeight.getSelectedMaxValue()};
    }

    public double[] solidityRange(){
        return new double[]{(double) minMaxSolidity.getSelectedMinValue(), (double)minMaxSolidity.getSelectedMaxValue()};
    }

    public double[] verticesRange(){
        return new double[]{(double) minMaxVertices.getSelectedMinValue(), (double) minMaxVertices.getSelectedMaxValue()};
    }

    public double[] ratioRange(){
        return new double[]{(double) minMaxRatio.getSelectedMinValue(), (double)minMaxRatio.getSelectedMaxValue()};
    }
}
