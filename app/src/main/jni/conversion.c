#include <string.h>
#include <jni.h>

jint
Java_com_gbrighen_dailytemperature_TemperatureActivity_convertToCelsius( JNIEnv* env, jobject obj,jint temp_f ){
    int temp_c=0;
    temp_c=temp_f*2;

    return temp_c;
}
