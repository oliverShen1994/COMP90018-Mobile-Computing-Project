package com.android.group_12.crushy.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.android.group_12.crushy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Utilities class to get the screen-related methods. It's because nowadays many mobile phones are
 * full-screen like, i.e. the screen ratio is 18:9.
 * <p>
 * Acknowledgement: https://juejin.im/post/5bc59db16fb9a05ce95c89fe
 */
public class ScreenUtil {
    public static Point getScreenSize(Context context) {
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }

        return new Point(widthPixels, heightPixels);
    }

    public static int getHeightOfNavigationBar(Context context) {
        // Return 0 if Xiaomi open Full-screen gesture and hide the navigation bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0) {
                return 0;
            }
        }

        int realHeight = getScreenSize(context).y;

        Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;

        return realHeight - displayHeight;
    }

    /**
     *
     * Acknowledgement: https://blog.csdn.net/zhangphil/article/details/80055964
     *
     * @param context
     * @return
     */
    public static int getHeightOfStatusBar(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
