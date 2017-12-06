LOCAL_PATH := $(call my-dir)  
include $(CLEAR_VARS)  
LOCAL_MODULE    := LQRJni
LOCAL_SRC_FILES := com_lqr_jnitest_JniUtil.c
include $(BUILD_SHARED_LIBRARY)  