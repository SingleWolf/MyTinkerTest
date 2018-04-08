package com.walker.mytinkertest;

import android.content.Context;

import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.app.ApplicationLike;

/**
 * @author Walker
 * @date on 2018/4/2 0002 下午 14:40
 * @email feitianwumu@163.com
 * @desc tinker管理类
 */

public class TinkerManager {
    private static boolean isInstalled = false;//是否已经初始化标志位
    private static ApplicationLike mApplicationLike;

    /**
     * 完成Tinker初始化
     *
     * @param applicationLike ApplicationLike实例
     */
    public static void installedTinker(ApplicationLike applicationLike) {
        mApplicationLike = applicationLike;
        if (isInstalled) {
            return;
        }
        //should set before tinker is installed
        UpgradePatchRetry.getInstance(applicationLike.getApplication()).setRetryEnable(true);
        //默认简单接入
//        TinkerInstaller.install(mApplicationLike);
        //************拓展功能 START****************
        //这两个是监听patch文件安装的日志上报结果 也就是补丁文件安装监听
        LoadReporter loadReporter = new DefaultLoadReporter(applicationLike.getApplication());//一些在加载补丁文件时的回调
        PatchReporter patchReporter = new DefaultPatchReporter(applicationLike.getApplication());//补丁文件在合成时一些事件的回调
        PatchListener patchListener = new DefaultPatchListener(applicationLike.getApplication());//一些补丁文件的校验工作
        AbstractPatch upgradePatchProcessor = new UpgradePatch();//决定patch文件安装策略  不会去修改与自定义

        TinkerInstaller.install(applicationLike,
                loadReporter, patchReporter, patchListener,
                MyTinkerResultService.class, upgradePatchProcessor);
        //************拓展功能 END****************
        isInstalled = true;
    }

    /**
     * 完成patch文件的加载
     *
     * @param path 补丁文件路径
     */
    public static void loadPatch(String path) {
        //是否已经安装过
        if (Tinker.isTinkerInstalled()) {
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);
        }
    }

    /**
     * 利用Tinker代理Application 获取应用全局的上下文
     *
     * @return 全局的上下文
     */
    private static Context getApplicationContext() {
        if (mApplicationLike != null) {
            return mApplicationLike.getApplication().getApplicationContext();
        }
        return null;
    }

}
