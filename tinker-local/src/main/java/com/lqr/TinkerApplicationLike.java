package com.lqr;


import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.lqr.tinker.Log.MyLogImp;
import com.lqr.tinker.utils.TinkerManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * 使用DefaultLifeCycle注解生成Application（这种方式是Tinker官方推荐的）
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.lqr.tinker.MyApplication",// application类名。只能用字符串，这个MyApplication文件是不存在的，但可以在AndroidManifest.xml的application标签上使用（name）
        flags = ShareConstants.TINKER_ENABLE_ALL,// tinkerFlags
        loaderClass = "com.tencent.tinker.loader.TinkerLoader",//loaderClassName, 我们这里使用默认即可!（可不写）
        loadVerifyFlag = false)//tinkerLoadVerifyFlag
public class TinkerApplicationLike extends DefaultApplicationLike {

    private Application mApplication;
    private Context mContext;
    private Tinker mTinker;

    // 固定写法
    public TinkerApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    // 固定写法
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        // 可以将之前自定义的Application中onCreate()方法所执行的操作搬到这里...
//    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        mApplication = getApplication();
        mContext = getApplication();
        initTinker(base);
        // 可以将之前自定义的Application中onCreate()方法所执行的操作搬到这里...
    }

    private void initTinker(Context base) {
        // tinker需要你开启MultiDex
        MultiDex.install(base);

        TinkerManager.setTinkerApplicationLike(this);
        // 设置全局异常捕获
        TinkerManager.initFastCrashProtect();
        //开启升级重试功能（在安装Tinker之前设置）
        TinkerManager.setUpgradeRetryEnable(true);
        //设置Tinker日志输出类
        TinkerInstaller.setLogIml(new MyLogImp());
        //安装Tinker(在加载完multiDex之后，否则你需要将com.tencent.tinker.**手动放到main dex中)
        TinkerManager.installTinker(this);
        mTinker = Tinker.with(getApplication());
    }

}
