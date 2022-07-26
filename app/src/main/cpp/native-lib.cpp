#include <sys/types.h>
#include <pthread.h>
#include <jni.h>
#include <unistd.h>
#include "Logger.h"
#include <jni.h>
#include <jni.h>
#include <jni.h>
#include <cstdio>
#include <string>
#include "obfuscate.h"

bool loggedin = false;

extern "C"
JNIEXPORT void JNICALL
Java_com_hydra_modz_toram_Login_Check(JNIEnv
* env,
jclass clazz
) {
loggedin = true;
}

//Simple security check
void *new_thread(void *) {
    //Loops until logged in
    do {
        sleep(1);
    } while (!loggedin);

    //Successfully logged in and load your stuff here

    LOGD("Logged in!");

    return nullptr;
}

__attribute__((constructor))
void lib_main() {
    // Create a new thread so it does not block the main thread, means the app would not freeze
    pthread_t ptid;
    pthread_create(&ptid, nullptr, new_thread, nullptr);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_hydra_modz_toram_Login_username(JNIEnv *env, jclass clazz) {
    std::string username(AY_OBFUSCATE("admin@hydramodz.com"));
    return env->NewStringUTF(username.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_hydra_modz_toram_Login_password(JNIEnv *env, jclass clazz) {
    std::string password(AY_OBFUSCATE("Admin@123"));
    return env->NewStringUTF(password.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_hydra_modz_toram_Login_url(JNIEnv *env, jclass clazz, jstring path) {
    const char* target = env->GetStringUTFChars(path, nullptr);
    const char* deviceid = "getdeviceid";
    std::string urlPath(AY_OBFUSCATE("https://hydramodz-api.herokuapp.com/api/user/getbydeviceid/"));
    std::string defaultUrlPath(AY_OBFUSCATE("https://hydramodz-api.herokuapp.com/api/user"));
    if (strcmp(target, deviceid) == 0) {
        return env->NewStringUTF(urlPath.c_str());
    }
    return env->NewStringUTF(defaultUrlPath.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_hydra_modz_toram_Login_Download_1URL(JNIEnv *env, jclass clazz) {
    std::string downloadUrl(AY_OBFUSCATE("https://github.com/MintzHydra/H260101M/raw/main/hydramodz.so"));
    return env->NewStringUTF(downloadUrl.c_str());
}