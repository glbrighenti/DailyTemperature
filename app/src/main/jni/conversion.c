#include <jni.h>

jfloat
Java_com_gbrighen_dailytemperature_TemperatureActivity_convertToCelsius( JNIEnv* env, jobject obj,jfloat temp_f ){
    jfloat temp_c=(temp_f-32)*5/9;
    return temp_c;
}


jfloat
Java_com_gbrighen_dailytemperature_TemperatureActivity_convertToFahrenheit( JNIEnv* env, jobject obj,jfloat temp_c ){
    jfloat temp_f=1.8*temp_c+32;
    return temp_f;
}
