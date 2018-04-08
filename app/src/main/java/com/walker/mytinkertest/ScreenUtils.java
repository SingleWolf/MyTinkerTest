package com.walker.mytinkertest;

/**
 * @author Walker
 * @date on 2018/4/3 0003 下午 16:25
 * @email feitianwumu@163.com
 * @desc 屏幕工具类
 */
public class ScreenUtils {
    private static boolean mBackground = false;

    public static boolean isBackground() {
        return mBackground;
    }

    public static void setBackground(boolean back) {
        mBackground = back;
    }
}
