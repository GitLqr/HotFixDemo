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

package com.lqr.tinker.reporter;

import android.content.Context;
import android.content.Intent;

import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;

import java.io.File;
import java.util.List;

/**
 * PatchReporter类定义了Tinker在修复或者升级补丁时的一些回调
 * <p>
 * optional, you can just use DefaultPatchReporter
 * Created by zhangshaowen on 16/4/8.
 */
public class SamplePatchReporter extends DefaultPatchReporter {
    private final static String TAG = "Tinker.SamplePatchReporter";

    public SamplePatchReporter(Context context) {
        super(context);
    }

    /**
     * 这个是Patch进程启动时的回调，我们可以在这里进行一个统计的工作。
     */
    @Override
    public void onPatchServiceStart(Intent intent) {
        super.onPatchServiceStart(intent);
        SampleTinkerReport.onApplyPatchServiceStart();
    }

    /**
     * 对合成的dex文件提前进行dexopt时出现异常，默认我们会删除临时文件。
     */
    @Override
    public void onPatchDexOptFail(File patchFile, List<File> dexFiles, Throwable t) {
        super.onPatchDexOptFail(patchFile, dexFiles, t);
        SampleTinkerReport.onApplyDexOptFail(t);
    }

    /**
     * 在补丁合成过程捕捉到异常，十分希望你可以把错误信息反馈给我们。默认我们会删除临时文件，并且将tinkerFlag设为不可用。
     */
    @Override
    public void onPatchException(File patchFile, Throwable e) {
        super.onPatchException(patchFile, e);
        SampleTinkerReport.onApplyCrash(e);
    }

    /**
     * patch.info是用来管理补丁包版本的文件，这是在更新info文件时发生损坏的回调。默认我们会卸载补丁包，因为此时我们已经无法恢复了。
     */
    @Override
    public void onPatchInfoCorrupted(File patchFile, String oldVersion, String newVersion) {
        super.onPatchInfoCorrupted(patchFile, oldVersion, newVersion);
        SampleTinkerReport.onApplyInfoCorrupted();
    }

    /**
     * 补丁合成过程对输入补丁包的检查失败，这里可以通过错误码区分，例如签名校验失败、tinkerId不一致等原因。默认我们会删除临时文件。
     */
    @Override
    public void onPatchPackageCheckFail(File patchFile, int errorCode) {
        super.onPatchPackageCheckFail(patchFile, errorCode);
        SampleTinkerReport.onApplyPackageCheckFail(errorCode);
    }

    /**
     * 这个是无论补丁合成失败或者成功都会回调的接口，它返回了本次合成的类型，时间以及结果等。
     * 默认我们只是简单的输出这个信息，你可以在这里加上监控上报逻辑。
     */
    @Override
    public void onPatchResult(File patchFile, boolean success, long cost) {
        super.onPatchResult(patchFile, success, cost);
        SampleTinkerReport.onApplied(cost, success);
    }

    /**
     * 从补丁包与原始安装包中合成某种类型的文件出现错误，默认我们会删除临时文件。
     */
    @Override
    public void onPatchTypeExtractFail(File patchFile, File extractTo, String filename, int fileType) {
        super.onPatchTypeExtractFail(patchFile, extractTo, filename, fileType);
        SampleTinkerReport.onApplyExtractFail(fileType);
    }

    /**
     * 对patch.info的校验版本合法性校验。若校验失败，默认我们会删除临时文件。
     */
    @Override
    public void onPatchVersionCheckFail(File patchFile, SharePatchInfo oldPatchInfo, String patchFileVersion) {
        super.onPatchVersionCheckFail(patchFile, oldPatchInfo, patchFileVersion);
        SampleTinkerReport.onApplyVersionCheckFail();
    }
}
