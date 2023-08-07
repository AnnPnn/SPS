package granch.sps;

import granch.sps.graphics.CanvasView;

public final class CONST {
    public static final float SCALE;//1m=100px
    public static final float RADIUS = 15;
    public static final String APP_PREFERENCES = "spsSettings";
    public final static int SERVICE_PORT = 23123;
    public static final float CELL_SIZE = SCALE = (((float) CanvasView.mWinSize.y / 2) / 18);
    public static final float CELL_START = (CanvasView.mWinSize.x / 2) % CELL_SIZE;


}
