package granch.sps.graphics;

import android.graphics.Point;

import granch.sps.CONST;

public class Anchor {

    public static final String mac1 = "[00, 1F, 04, 00, 5B, 52]";
    public static final String mac2 = "[00, 1F, 04, 00, 5B, 50]";
    public static final String mac3 = "[00, 1F, 04, 00, 5B, 4D]";
    public static final String mac4 = "[00, 1F, 04, 00, 5B, 51]";
    private static final Point mCoo = new Point((int) Utils.getScreenWidth() / 2, (int) (Utils.getScreenHeight() / 2) - 15);
    public static final Point anchor1 = new Point((int) (mCoo.x + 0.5F * CONST.SCALE), (int) (mCoo.y - 0.5F * CONST.SCALE));
    public static final Point anchor2 = new Point((int) (mCoo.x - 0.5F * CONST.SCALE), (int) (mCoo.y - 0.5F * CONST.SCALE));
    public static final Point anchor3 = new Point((int) (mCoo.x - 0.5F * CONST.SCALE), (int) (mCoo.y + 0.5F * CONST.SCALE));  
    public static final Point anchor4 = new Point((int) (mCoo.x + 0.5F * CONST.SCALE), (int) (mCoo.y + 0.5F * CONST.SCALE));


    static boolean getZoneFrontBack(String mac) {
        boolean zone = true;
        if (mac.equals(mac1) || mac.equals(mac2)) {
            //front
        } else if (mac.equals(mac3) || mac.equals(mac4)) {
            zone = false;//back
        }
        return zone;
    }

    static boolean getZoneRightLeft(String mac) {
        boolean zone = true;
        if (mac.equals(mac1) || mac.equals(mac4)) {
            //right
        } else if (mac.equals(mac2) || mac.equals(mac3)) {
            zone = false;//left
        }
        return zone;
    }


}
