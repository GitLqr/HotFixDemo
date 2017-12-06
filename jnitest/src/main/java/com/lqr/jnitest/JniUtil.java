package com.lqr.jnitest;

public class JniUtil {

    public JniUtil() {

    }

    static {
        System.loadLibrary("LQRJni");
    }

    public static native String hello();
}
