#include <jni.h>
/*
 * Providing implementation of native conversion code.
 */


/*
 * This method receives a float array with celsius temperatures, and return another float array
 * with each element converted to fahrenheit.
 * Since this is JNI we need to use proper method to deal with arrays. So to manipulate we need to
 * use methods like:
 * GetArrayLength to get the size of jfloatArray,
 * GetFloatArrayElements to access the first member of jfloatArray,
 * NewFloatArray, to allocate a new jfloatArray to return the new values.
 */
JNIEXPORT jfloatArray
Java_com_gbrighen_dailytemperature_TemperatureActivity_convertToFahrenheit(
        JNIEnv* env, jobject obj,jfloatArray temp_c ){
    int i=0;
    jfloatArray result;
    jsize size = (*env)->GetArrayLength(env, temp_c);

    //Alloc new array to store fahrenheit results
    result = (*env)->NewFloatArray(env, size);

    jfloat temp_f[size];
    //Access first element of jfloatarray. After this we can use standard pointers operations in C
    jfloat *element_c = (*env)->GetFloatArrayElements(env, temp_c, 0);

    for(i=0;i<size;i++){
        temp_f[i]=1.8*element_c[i]+32; //formula to convert C to F
    }

    //Fill return array previously allocated with float[] result from conversion above
    (*env)->SetFloatArrayRegion(env, result, 0, size, temp_f);
    
	//We need release arrays that used GetFloatArrayElements previously
    (*env)->ReleaseFloatArrayElements(env, temp_c, element_c, 0);

    return result;
}

