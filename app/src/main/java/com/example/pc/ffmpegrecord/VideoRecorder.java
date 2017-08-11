package com.example.pc.ffmpegrecord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.Manifest;

/**
 * Created by pc on 2017/8/8.
 */

public class VideoRecorder extends Activity implements SurfaceHolder.Callback, android.hardware.Camera.PreviewCallback{

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private android.hardware.Camera mCamera;
    private int mCameraFacing = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;//默认后置摄像头
    private static final SparseIntArray orientations = new SparseIntArray();//手机旋转对应的调整角度
    private int  orientation;
    private int height,width;
    private boolean isCameraInit;
    private FFmpeg fFmpeg ;
    private StreamTask mStreamTask;
    private ArrayList<byte[]> lists = new ArrayList<>();
    private boolean mIsRecording;
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

    public static void start(Context context){
        context.startActivity(new Intent(context,VideoRecorder.class));
    }
    static {
        orientations.append(Surface.ROTATION_0, 90);
        orientations.append(Surface.ROTATION_90, 0);
        orientations.append(Surface.ROTATION_180, 270);
        orientations.append(Surface.ROTATION_270, 180);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.videorecord);

        fFmpeg = new FFmpeg();
        surfaceView = findViewById(R.id.surfaceview);

        surfaceHolder = surfaceView.getHolder();


        height = getResources().getDisplayMetrics().heightPixels;
        width = getResources().getDisplayMetrics().widthPixels;
       if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this,new String[]{
               android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO},123);
           return;
       }
        initCamera(mCameraFacing);
        fFmpeg.initial(mCamera.getParameters().getPreviewSize().width,mCamera.getParameters().getPreviewSize().height);

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.setPreviewCallback(null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fFmpeg.close();
                    }
                },1000);
            }
        });
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIsRecording = !mIsRecording;
//                if(mIsRecording) {
//                   new Thread(new Encoder()).start();
//                }
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initCamera(mCameraFacing);
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        if(null!=mCamera) {
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    }

    /**
     * 初始化相机
     */
    public void initCamera(int mCameraFacing) {
        Log.e("videoRecord","videoRecord initCamera");
        if (android.hardware.Camera.getNumberOfCameras() == 2) {
            mCamera = android.hardware.Camera.open(mCameraFacing);
        } else {
            mCamera = android.hardware.Camera.open();
        }
        android.hardware.Camera.Parameters parameters = mCamera.getParameters();
//        mSize = CameraHelper.getOptimalVideoSize(parameters.getSupportedVideoSizes(), parameters.getSupportedPreviewSizes(), glSurfaceView.getWidth(), glSurfaceView.getHeight());
        List<String> focusModesList = parameters.getSupportedFocusModes();

        //增加对聚焦模式的判断
        if (focusModesList.contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else if (focusModesList.contains(android.hardware.Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_AUTO);
        }

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int videoFrameWidth = 0, videoFrameHeight;
        orientation = orientations.get(rotation);
        if(orientation==90||orientation ==270) {
            videoFrameWidth = height;
            videoFrameHeight = width;
        }else {
            videoFrameWidth = width;
            videoFrameHeight = height;
        }
        parameters.setPreviewSize(videoFrameWidth, videoFrameHeight);
        mCamera.setParameters(parameters);

        mCamera.setDisplayOrientation(orientation);
        isCameraInit = true;
        mCamera.setPreviewCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        Log.e("videoRecord","videoRecord surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.e("videoRecord","videoRecord surfaceChanged");
        try {
            if(null!=mCamera) {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPreviewFrame(byte[] bytes, android.hardware.Camera camera) {

        if(mIsRecording) {
            fFmpeg.offer(bytes);
        }

//        if(null != mStreamTask){
//            switch(mStreamTask.getStatus()){
//                case RUNNING:
//                    return;
//                case PENDING:
//                    mStreamTask.cancel(false);
//                    break;
//            }
//        }
//        if(mIsRecording) {
//            lists.add(bytes);
//        }

    }

//    class Encoder implements Runnable{
//        @Override
//        public void run() {
//
//            while (null!=lists||mIsRecording){
//                if(lists.size()>0) {
//                    fFmpeg.encode(lists.remove(0));
//                }
//            }
//        }
//    }


    private class StreamTask extends AsyncTask<Void, Void, Void> {



        //构造函数
        StreamTask(){

        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mIsRecording) {
                execute((Void) null);
            }
        }
    }

}
