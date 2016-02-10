LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := conversion_module
LOCAL_SRC_FILES := conversion.c

include $(BUILD_SHARED_LIBRARY)
