package com.example.pc.ffmpegrecord;

/**
 * Created by pc on 2017/8/9.
 */

public class FFmpeg {
    static {
        System.loadLibrary("native-lib");
    }
    public native int initial(int width,int height);
    public native int encode(byte[] yuvimage);
    public native int flush();
    public native int close();
    public native String test123();
    public native void offer(byte[]onframe);
}
