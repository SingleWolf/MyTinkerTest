package com.walker.mytinkertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

/**
 * @author Walker
 * @date on 2018/4/3 0003 下午 13:54
 * @email feitianwumu@163.com
 * @desc 决定在patch安装以后的后续操作 默认实现是杀死进程
 */
public class MyTinkerResultService extends DefaultTinkerResultService {
    private static final String TAG = "Tinker.MyTinkerResultService";

    @Override
    public void onPatchResult(final PatchResult result) {
        if (result == null) {
            TinkerLog.e(TAG, "SampleResultService received null result!!!!");
            return;
        }
        TinkerLog.i(TAG, "SampleResultService receive result: %s", result.toString());

        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (result.isSuccess) {
                    Toast.makeText(getApplicationContext(), "patch success, please restart process", Toast.LENGTH_LONG).show();
                    deleteRawPatchFile(new File(result.rawPatchFilePath));
                    //not like TinkerResultService, I want to restart just when I am at background!
                    //if you have not install tinker this moment, you can use TinkerApplicationHelper api
                    if (checkIfNeedKill(result)) {
                        if (ScreenUtils.isBackground()) {
                            TinkerLog.i(TAG, "it is in background, just restart process");
                            restartProcess();
                        } else {
                            TinkerLog.i(TAG, "tinker wait screen to restart process");
                            onScreenStateListener(getApplicationContext(), new IOnScreenOff() {
                                @Override
                                public void onScreenOff() {
                                    restartProcess();
                                }
                            });
                        }
                    } else {
                        TinkerLog.i(TAG, "I have already install the newly patch version!");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "patch fail:" + result.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 杀死当前进程
     */
    private void restartProcess() {
        TinkerLog.i(TAG, "app is background now, i can kill quietly");
        //you can send service or broadcast intent to restart your process
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    interface IOnScreenOff {
        void onScreenOff();
    }

    /**
     * @date on 2018/4/3 0003 下午 16:22
     * @author Walker
     * @email feitianwumu@163.com
     * @desc 监听锁屏广播
     */
    private void onScreenStateListener(Context context, final IOnScreenOff listener) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent in) {
                String action = in == null ? "" : in.getAction();
                TinkerLog.i(TAG, "ScreenReceiver action [%s] ", action);
                if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    if (listener != null) {
                        listener.onScreenOff();
                    }
                }
                context.unregisterReceiver(this);
            }
        }, filter);
    }
}
