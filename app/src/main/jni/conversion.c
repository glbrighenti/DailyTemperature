#include <jni.h>

jfloat
Java_com_gbrighen_dailytemperature_TemperatureActivity_convertToCelsius( JNIEnv* env, jobject obj,jfloat temp_f ){
    jfloat temp_c=(temp_f-32)*5/9;
    return temp_c;
}
