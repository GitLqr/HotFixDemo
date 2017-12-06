该项目使用ndk命令编译制作so库
1、为了编译不报错，已经在settings.gradle中把当前的module删除掉了，所以需要你先在settings.gradle中恢复当前module。
2、到项目的jni目录下，使用ndk-build命令即可。
    如：E:\AndroidProject\HotFixDemo\jnitest\src\main\jni>ndk-build
源码在hello.cpp，修改其中字符串即可。