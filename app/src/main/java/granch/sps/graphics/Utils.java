package granch.sps.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {


    /**
     * Получение высоты экрана
     *
     * @param ctx context
     * @param winSize размер экрана
     */
    public static void loadWinSize(Context ctx, Point winSize) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        winSize.x = outMetrics.widthPixels;
        winSize.y = outMetrics.heightPixels;
    }

    public static int getHeight(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }

        return outMetrics.heightPixels;
    }

    public static double getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static double getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getAverageValue() {
        double height = Utils.getScreenHeight();
        int i;
        int j = 200;
        for (i = 1000; i <= 2400; i += j) {
            if ((height >= i) & (height < (i + j))) {
                System.out.println("Average Value: " + i);
                return i;
            }
        }
        return i;
    }

    public static float getMultipleNumbers(float size){

        float i;
        for (i = 32; i<size; i++){
            if(size % i == 0){
                return i;
            }
        }
        return i;
    }




}
