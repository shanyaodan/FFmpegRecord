
/**
 * Created by jianxi on 2017/5/18.
 * https://github.com/mabeijianxi
 * mabeijianxi@gmail.com
 */

#ifndef JIANXIFFMPEG_BASE_INCLUDE_H
#define JIANXIFFMPEG_BASE_INCLUDE_H

extern "C"
{
#include <libswscale/swscale.h>
#include <libavutil/log.h>
#include <libavutil/opt.h>
#include <libavcodec/avcodec.h>

#include <libavformat/avformat.h>
#include <libavutil/imgutils.h>
#include <libavutil/time.h>
}

#include "threadsafe_queue.cpp"
#include <jni.h>
#include <string>

#define END_STATE 1
#define START_STATE 0

#define JX_TRUE 1
#define JX_FALSE 0

#define ROTATE_0_CROP_LT 0

/**
 * 旋转90度剪裁左上
 */
#define ROTATE_90_CROP_LT 1
/**
 * 暂时没处理
 */
#define ROTATE_180 2
/**
 * 旋转270(-90)裁剪左上，左右镜像
 */
#define ROTATE_270_CROP_LT_MIRROR_LR 3

using namespace std;


#endif //JIANXIFFMPEG_BASE_INCLUDE_H
