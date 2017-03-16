# Java-Memory-Manipulation
_Java Based Memory, Process, Module Interfacing_

This library is licensed under Apache License 2.0 and was created for debugging/testing security and functionality of your software.

This is a heavily optimized and **thread-safe** version of Jonatino's memory library. Be sure to check out his GitHub and awesome projects, without his work my life would've been a nightmare!

---

## Dependencies

- [Java Native Access (JNA)](https://github.com/java-native-access/jna) as the backbone for interfacing with native libraries. You can download precompiled files from [their GitHub](https://github.com/java-native-access/jna#download).

### Compiling JNI wrapper
Since native JNA functions can clutter your RAM in a matter of minutes, I was forced to create my own process_vm_readv/process_vm_writev wrapper for Linux. This needs to be compiled separately and the directory with libnative_mem.so inside **must be in java.library.path**.
```
gcc -I/usr/lib/jvm/java-8-openjdk-amd64/include/ -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -lc -shared -fPIC -o libnative_mem.so com_github_jonatino_natives_unix_unixc.c
```
In Eclipse, you need to set your "native" directory as the library path in Project Build Path > Source > Native library location > Edit...  
There's a precompiled binary in the [native](native) folder of this repository.