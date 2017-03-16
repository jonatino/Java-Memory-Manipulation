#define _GNU_SOURCE
#include <sys/uio.h>
#include "com_github_jonatino_natives_unix_unixc.h"

// gcc -I/usr/lib/jvm/java-8-openjdk-amd64/include/ -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -lc -shared -fPIC -o native_mem.so com_github_jonatino_natives_unix_unixc.c

JNIEXPORT jlong JNICALL Java_com_github_jonatino_natives_unix_unixc_mem_1read__IJJI
  (JNIEnv *env, jclass class, jint pid, jlong localaddr, jlong remoteaddr, jint length) {
        struct iovec local[1];
        struct iovec remote[1];

        local[0].iov_base = (void*) localaddr;
        local[0].iov_len = length;
        remote[0].iov_base = (void*) remoteaddr;
        remote[0].iov_len = length;

        return process_vm_readv(pid, local, 1, remote, 1, 0);
  }

JNIEXPORT jlong JNICALL Java_com_github_jonatino_natives_unix_unixc_mem_1write__IJJI
  (JNIEnv *env, jclass class, jint pid, jlong localaddr, jlong remoteaddr, jint length) {
        struct iovec local[1];
        struct iovec remote[1];

        local[0].iov_base = (void*) localaddr;
        local[0].iov_len = length;
        remote[0].iov_base = (void*) remoteaddr;
        remote[0].iov_len = length;

        return process_vm_writev(pid, local, 1, remote, 1, 0);
  }

JNIEXPORT jlong JNICALL Java_com_github_jonatino_natives_unix_unixc_mem_1read__I_3J_3J_3I
  (JNIEnv *env, jclass class, jint pid, jlongArray localaddrarr, jlongArray remoteaddrarr, jintArray lengtharr) {
  	jsize count = (*env)->GetArrayLength(env, localaddrarr);
  	struct iovec local[count];
  	struct iovec remote[count];

  	jlong *localaddr = (*env)->GetLongArrayElements(env, localaddrarr, 0);
  	jlong *remoteaddr = (*env)->GetLongArrayElements(env, remoteaddrarr, 0);
  	jint *length = (*env)->GetIntArrayElements(env, lengtharr, 0);

	for (int i = 0; i < count; i++) {
		local[i].iov_base = (void*) localaddr[i];
        local[i].iov_len = length[i];
        remote[i].iov_base = (void*) remoteaddr[i];
        remote[i].iov_len = length[i];
	}

	(*env)->ReleaseLongArrayElements(env, localaddrarr, localaddr, 0);
	(*env)->ReleaseLongArrayElements(env, remoteaddrarr, remoteaddr, 0);
	(*env)->ReleaseIntArrayElements(env, lengtharr, length, 0);

	return process_vm_readv(pid, local, count, remote, count, 0);
}

JNIEXPORT jlong JNICALL Java_com_github_jonatino_natives_unix_unixc_mem_1write__I_3J_3J_3I
  (JNIEnv *env, jclass class, jint pid, jlongArray localaddrarr, jlongArray remoteaddrarr, jintArray lengtharr) {
  	jsize count = (*env)->GetArrayLength(env, localaddrarr);
  	struct iovec local[count];
  	struct iovec remote[count];

  	jlong *localaddr = (*env)->GetLongArrayElements(env, localaddrarr, 0);
  	jlong *remoteaddr = (*env)->GetLongArrayElements(env, remoteaddrarr, 0);
  	jint *length = (*env)->GetIntArrayElements(env, lengtharr, 0);

	for (int i = 0; i < count; i++) {
		local[i].iov_base = (void*) localaddr[i];
        local[i].iov_len = length[i];
        remote[i].iov_base = (void*) remoteaddr[i];
        remote[i].iov_len = length[i];
	}

	(*env)->ReleaseLongArrayElements(env, localaddrarr, localaddr, 0);
	(*env)->ReleaseLongArrayElements(env, remoteaddrarr, remoteaddr, 0);
	(*env)->ReleaseIntArrayElements(env, lengtharr, length, 0);

	return process_vm_writev(pid, local, count, remote, count, 0);
}