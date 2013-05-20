LOCAL_PATH := $(call my-dir)
 
include $(CLEAR_VARS)
 
# Here we give our module name and source file(s)
LOCAL_MODULE    := xulyanh
LOCAL_CFLAGS    := -Wall
LOCAL_SRC_FILES := ConvertYUV.cpp jniapi.cpp renderer.cpp
LOCAL_LDLIBS    := -llog -landroid -lEGL -lGLESv1_CM 
 
include $(BUILD_SHARED_LIBRARY)