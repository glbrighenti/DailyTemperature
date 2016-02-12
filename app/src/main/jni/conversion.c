#include <jni.h>


jfloatArray
Java_com_gbrighen_dailytemperature_TemperatureActivity_convertToFahrenheit( JNIEnv* env, jobject obj,jfloatArray temp_c ){
    int i=0;
    jfloatArray result;
    jsize size = (*env)->GetArrayLength(env, temp_c);

    result = (*env)->NewFloatArray(env, size);

    jfloat temp_f[size];
    jfloat *element_c = (*env)->GetFloatArrayElements(env, temp_c, 0);

    for(i=0;i<size;i++){
        temp_f[i]=1.8*element_c[i]+32;
    }

    (*env)->SetFloatArrayRegion(env, result, 0, size, temp_f);

    return result;
}

