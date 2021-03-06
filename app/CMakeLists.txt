# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/threadsafe_queue.cpp
             src/main/cpp/base_include.h
             src/main/cpp/encoder.cpp
              )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

set(distribution_DIR ${CMAKE_SOURCE_DIR}/../../../../libs)

add_library( avutil-55
             SHARED
             IMPORTED )
set_target_properties( avutil-55
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi-v7a/libavutil-54.so )

add_library( swresample-2
             SHARED
             IMPORTED )
set_target_properties( swresample-2
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi-v7a/libswresample-1.so )
add_library( avcodec-57
             SHARED
             IMPORTED )
set_target_properties( avcodec-57
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi-v7a/libavcodec-56.so )
add_library( avfilter-6
             SHARED
             IMPORTED)
set_target_properties( avfilter-6
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi-v7a/libavfilter-5.so )
add_library( swscale-4
             SHARED
             IMPORTED)
set_target_properties( swscale-4
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi-v7a/libswscale-3.so )
add_library( avdevice-57
             SHARED
             IMPORTED)
set_target_properties( avdevice-57
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi-v7a/libavdevice-56.so )
add_library( avformat-57
             SHARED
             IMPORTED)
set_target_properties( avformat-57
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi-v7a/libavformat-56.so )

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")



include_directories(libs/include)

#target_include_directories(native-lib PRIVATE libs/include)

target_link_libraries( native-lib avutil-55 swresample-2 avcodec-57 avfilter-6 swscale-4 avdevice-57 avformat-57
                       ${log-lib} )