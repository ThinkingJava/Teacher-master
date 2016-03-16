package com.aswifter.material;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by froger_mcs on 05.11.14.
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    public final static String DOMAIN="¡£¡£¡£";
    public  final static  String LOGIN="¡£¡£¡£";
    public  final static String LOGIN_CHECK="¡£¡£¡£";
    public final static String COURSE_INFORMATION="¡£¡£¡£";
    public final static String IMAGE_CODE="¡£¡£¡£";
    public final static String CURRENT_TERM="¡£¡£¡£";
    public final static String ATTEND_LOGIN="¡£¡£¡£";  //è€ƒå‹¤
    public final static String ATTEND="¡£¡£¡£";
    public final static String ATTEND_MAIN="¡£¡£¡£";
   public final static String checkAtndTimeOptionsURL = "¡£¡£¡£";

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}