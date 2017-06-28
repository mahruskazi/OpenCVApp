package com.example.mahruskazi.opencvapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final String TAG = "OpenCVTest";

    private static final int VIEW_MODE_RGB = 0;
    private static final int VIEW_MODE_HSV = 1;
    private static final int VIEW_MODE_CONTOUR = 2;
    private static final int VIEW_MODE_OUTPUT = 3;

    private int[] curHueRange = new int[]{0, 180};
    private int[] curSatRange = new int[]{0, 255};
    private int[] curValRange = new int[]{0, 255};

    private int mViewMode = 0;

    JavaCameraView cameraView;
    Mat mRgba;
    Mat mHsv;
    Mat mContour;
    Mat mOutput;

    MenuItem mItemHSVTune;
    MenuItem mItemContour;
    MenuItem mItemPreview;
    MenuItem mItemOutput;

    VisionPipeline pipeline;

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                {
                    cameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    static {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        cameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        cameraView.enableFpsMeter();
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        mItemHSVTune = menu.add("Start/Tune HSV");
        mItemContour = menu.add("Find Contours");
        mItemPreview = menu.add("Preview Image");
        mItemOutput = menu.add("Show Output");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item == mItemHSVTune){
            Intent launchNewActivity = new Intent(MainActivity.this, HSVTunerActivity.class);


            launchNewActivity.putExtra("Hue Data", curHueRange);
            launchNewActivity.putExtra("Sat Data", curSatRange);
            launchNewActivity.putExtra("Val Data", curValRange);

            startActivityForResult(launchNewActivity, 0);
            HSVTunerActivity.setIsRunning(true);
            mViewMode = VIEW_MODE_HSV;
        }
        else if(item == mItemContour){
            mViewMode = VIEW_MODE_CONTOUR;
        }
        else if(item == mItemOutput){
            mViewMode = VIEW_MODE_OUTPUT;
        }
        else if(item == mItemPreview){
            mViewMode = VIEW_MODE_RGB;
        }
        else if(item.getItemId() == android.R.id.home)
            finish();

        return true;
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (cameraView != null && !HSVTunerActivity.isPopUp()) {
            Log.d(TAG, "Camera Disabled " + HSVTunerActivity.isPopUp());
            cameraView.disableView();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            Log.d(TAG, "Camera Destroy " + HSVTunerActivity.isPopUp());
            cameraView.disableView();
        }
    }

    public void onResume(){
        super.onResume();

        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV Initialized");
            HSVTunerActivity.setIsRunning(false);
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else {
            Log.d(TAG, "OpenCV did not load");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        }
    }

    public void onCameraViewStarted(int width, int height) {

        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mHsv = new Mat(height, width, CvType.CV_8UC1);
        mContour = new Mat(height, width, CvType.CV_8UC1);
        mOutput = new Mat(height, width, CvType.CV_8UC4);
        pipeline = new VisionPipeline(width, height);
    }

    public void onCameraViewStopped() {
        mRgba.release();
        mHsv.release();
        mContour.release();
        mOutput.release();
        pipeline.release();
        Log.d(TAG, "CameraView Stopped");
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final int viewMode = mViewMode;
        final int orientation = this.getResources().getConfiguration().orientation;

        mRgba = inputFrame.rgba();


        if(HSVTunerActivity.hasStarted()) {
            curHueRange = HSVTunerActivity.getHueRange();
            curSatRange = HSVTunerActivity.getSatRange();
            curValRange = HSVTunerActivity.getValRange();
        }

        if(viewMode != VIEW_MODE_RGB)
            pipeline.process(mRgba, curHueRange,
                                    curSatRange,
                                    curValRange);

        switch (viewMode){
            case VIEW_MODE_HSV:
                mHsv = pipeline.hsvThresholdOutput();
                mOutput = mHsv;
                break;
            case VIEW_MODE_CONTOUR:
                pipeline.findContoursOutput();
                break;
            case VIEW_MODE_RGB:
                mOutput = mRgba;
                break;

        }
        return mOutput;
    }
}
