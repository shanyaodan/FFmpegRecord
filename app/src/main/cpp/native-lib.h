
#ifndef FFMPEGANDROID_NATIVE_LIB_H
#define FFMPEGANDROID_NATIVE_LIB_H



#include "jni.h"

#ifdef __cplusplus
extern "C" {

#include <libswscale/swscale.h>
#include <libavutil/log.h>
#include <libavutil/opt.h>
#include <libavcodec/avcodec.h>

#include <libavformat/avformat.h>
#include <libavutil/imgutils.h>
#include <libavutil/time.h>
#endif

JNIEXPORT void JNICALL
Java_com_example_pc_ffmpegrecord_FFmpeg_offer(JNIEnv *, jobject , jbyteArray ) ;

JNIEXPORT jint JNICALL Java_com_example_pc_ffmpegrecord_FFmpeg_initial
        (JNIEnv *, jobject, jint , jint );

JNIEXPORT jint JNICALL Java_com_example_pc_ffmpegrecord_FFmpeg_encode
        (JNIEnv *, jobject , jbyteArray );

JNIEXPORT jint JNICALL Java_com_example_pc_ffmpegrecord_FFmpeg_flush
        (JNIEnv *, jobject );

JNIEXPORT jint JNICALL Java_com_example_pc_ffmpegrecord_FFmpeg_close
        (JNIEnv *, jobject );

#ifdef __cplusplus
}
#endif
#endif