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

import com.lqr.tinker.crash.SampleUncaughtExceptionHandler;
import com.lqr.tinker.reporter.SampleLoadReporter;
import com.lqr.tinker.reporter.SamplePatchListener;
import com.lqr.tinker.reporter.SamplePatchReporter;
import com.lqr.tinker.service.SampleResultService;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.app.ApplicationLike;


/**
 * Created by zhangshaowen on 16/7/3.
 */
public class TinkerManager {
    private static final String TAG = "Tinker.TinkerManager";

    private static ApplicationLike applicationLike;
    private static SampleUncaughtExceptionHandler uncaughtExceptionHandler;
    private static boolean isInstalled = false;

    public static void setTinkerApplicationLike(ApplicationLike appLike) {
        applicationLike = appLike;
    }

    public static ApplicationLike getTinkerApplicationLike() {
        return applicationLike;
    }

    /**
     * 初始化全局异常捕获
     */
    public static void initFastCrashProtect() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = new SampleUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
    }

    /**
     * 开启或关闭补丁升级重试功能
     */
    public static void setUpgradeRetryEnable(boolean enable) {
        UpgradePatchRetry.getInstance(applicationLike.getApplication()).setRetryEnable(enable);
    }


    /**
     * 默认安装Tinker（使用默认的reporter类：DefaultLoadReporter、DefaultPatchReporter、DefaultPatchListener、DefaultTinkerResultService）
     * 如果你不需要对监听app打补丁的情况（如：当打补丁失败时上传失败信息），则使用该方法
     */
    public static void sampleInstallTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        TinkerInstaller.install(appLike);
        isInstalled = true;

    }

    /**
     * 自定义安装Tinker（使用你自定义的reporter类：SampleLoadReporter、SamplePatchReporter、SamplePatchListener、SampleResultService）
     * you can specify all class you want.
     * sometimes, you can only install tinker in some process you want!
     */
    public static void installTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        //or you can just use DefaultLoadReporter
        LoadReporter loadReporter = new SampleLoadReporter(appLike.getApplication());
        //or you can just use DefaultPatchReporter
        PatchReporter patchReporter = new SamplePatchReporter(appLike.getApplication());
        //or you can just use DefaultPatchListener
        PatchListener patchListener = new SamplePatchListener(appLike.getApplication());
        //you can set your own upgrade patch if you need
        AbstractPatch upgradePatchProcessor = new UpgradePatch();

        TinkerInstaller.install(appLike,
                loadReporter, patchReporter, patchListener,
                SampleResultService.class, upgradePatchProcessor);

        isInstalled = true;
    }
}
