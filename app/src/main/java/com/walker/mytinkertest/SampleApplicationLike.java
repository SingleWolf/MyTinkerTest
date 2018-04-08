package com.walker.mytinkertest;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.io.File;
import java.io.IOException;

/**
 * 功能   ：ApplicationLike为Tinker生成Context对象 官方建议 而不是继承我们自己的Application
 * 作用   ：使用这个ApplicationLike这个类作为Application的委托代理是因为，Tinker需要监听Application
 * 的生命周期并针对不同的生命周期来做相应的初始化与处理，这样就减少使用者需要自己处理。
 */
@DefaultLifeCycle(application = ".SampleApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class SampleApplicationLike extends DefaultApplicationLike {
    public SampleApplicationLike(Application application,
                                 int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag,
                                 long applicationStartElapsedTime,
                                 long applicationStartMillisTime,
                                 Intent tinkerResultIntent) {
        super(application,
                tinkerFlags,
                tinkerLoadVerifyFlag,
                applicationStartElapsedTime,
                applicationStartMillisTime,
                tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        TinkerManager.installedTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}