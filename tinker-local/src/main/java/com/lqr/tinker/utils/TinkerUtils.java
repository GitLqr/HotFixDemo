/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lqr.tinker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.StatFs;

import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by zhangshaowen on 16/4/7.
 */
public class TinkerUtils {
    private static final String TAG = "Tinker.Utils";

    /**
     * the error code define by myself
     * should after {@code ShareConstants.ERROR_PATCH_INSERVICE
     */
    public static final int ERROR_PATCH_GOOGLEPLAY_CHANNEL = -20;// GooglePlay渠道不允许使用热修复
    public static final int ERROR_PATCH_ROM_SPACE = -21;// 没有足够的空间
    public static final int ERROR_PATCH_MEMORY_LIMIT = -22;// 最大内存
    public static final int ERROR_PATCH_CRASH_LIMIT = -23;// 崩溃界限
    public static final int ERROR_PATCH_CONDITION_NOT_SATISFIED = -24;// 条件不满意

    public static final String PLATFORM = "platform";

    public static final int MIN_MEMORY_HEAP_SIZE = 45;

    private static boolean background = false;

    /**
     * 判断是否是GP渠道
     */
    public static boolean isGooglePlay() {
        return false;
    }

    /**
     * 判断当前App是否处于后台
     */
    public static boolean isBackground() {
        return background;
    }

    public static void setBackground(boolean back) {
        background = back;
    }

    /**
     * 判断当前是否非GP渠道，且空间足够
     */
    public static int checkForPatchRecover(long roomSize, int maxMemory) {
        if (TinkerUtils.isGooglePlay()) {
            return TinkerUtils.ERROR_PATCH_GOOGLEPLAY_CHANNEL;
        }
        if (maxMemory < MIN_MEMORY_HEAP_SIZE) {
            return TinkerUtils.ERROR_PATCH_MEMORY_LIMIT;
        }
        //or you can mention user to clean their rom space!
        if (!checkRomSpaceEnough(roomSize)) {
            return TinkerUtils.ERROR_PATCH_ROM_SPACE;
        }

        return ShareConstants.ERROR_PATCH_OK;
    }

    /**
     * 判断是否有Xposed框架的存在
     */
    public static boolean isXposedExists(Throwable thr) {
        StackTraceElement[] stackTraces = thr.getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            final String clazzName = stackTrace.getClassName();
            if (clazzName != null && clazzName.contains("de.robv.android.xposed.XposedBridge")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前ROM空间是否足够大
     */
    @Deprecated
    public static boolean checkRomSpaceEnough(long limitSize) {
        long allSize;
        long availableSize = 0;
        try {
            File data = Environment.getDataDirectory();
            StatFs sf = new StatFs(data.getPath());
            availableSize = (long) sf.getAvailableBlocks() * (long) sf.getBlockSize();
            allSize = (long) sf.getBlockCount() * (long) sf.getBlockSize();
        } catch (Exception e) {
            allSize = 0;
        }

        if (allSize != 0 && availableSize > limitSize) {
            return true;
        }
        return false;
    }

    /**
     * 获取异常原因字段串
     */
    public static String getExceptionCauseString(final Throwable ex) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(bos);

        try {
            // print directly
            Throwable t = ex;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            t.printStackTrace(ps);
            return toVisualString(bos.toString());
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String toVisualString(String src) {
        boolean cutFlg = false;

        if (null == src) {
            return null;
        }

        char[] chr = src.toCharArray();
        if (null == chr) {
            return null;
        }

        int i = 0;
        for (; i < chr.length; i++) {
            if (chr[i] > 127) {
                chr[i] = 0;
                cutFlg = true;
                break;
            }
        }

        if (cutFlg) {
            return new String(chr, 0, i);
        } else {
            return src;
        }
    }

    /*------------------ 判断手机是否锁屏状态 ------------------*/
    public static class ScreenState {
        public interface IOnScreenOff {
            void onScreenOff();
        }

        public ScreenState(final Context context, final IOnScreenOff onScreenOffInterface) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);

            context.registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent in) {
                    String action = in == null ? "" : in.getAction();
                    TinkerLog.i(TAG, "ScreenReceiver action [%s] ", action);
                    if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                        if (onScreenOffInterface != null) {
                            onScreenOffInterface.onScreenOff();
                        }
                    }
                    context.unregisterReceiver(this);
                }
            }, filter);
        }
    }
}
