package granch.sps.graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import granch.sps.CONST;
import granch.sps.UdpServer;
import granch.sps.pars.MeasuredDistance;


public class CanvasView extends View implements UdpServer.DistanceMessageReceived {

    private static boolean clickedDraw;
    private Paint mGridPaint; //Сетка рисования
    public static Point mWinSize; //Размер экрана
    private Point mCoo; //Положение системы координат
    private final AlertPlayer mAlertPlayer;


    private float mGreen = 10;
    private float mYellow = 5;
    private float mRed = 2;

    private final Map<String, Map<String, MeasuredDistance>> distances = new TreeMap<>();

    static int ox1;
    static int ox2;
    static int ox3;
    static int ox4;

    public static int oy1;
    public static int oy2;
    public static int oy3;
    public static int oy4;


    public void setZones(float green, float yellow, float red, String mac1, String mac2, String mac3, String mac4,
                         int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4,  boolean clickedDraw) {

        mRed = red * CONST.SCALE;
        mYellow = yellow * CONST.SCALE;
        mGreen = green * CONST.SCALE;

        CanvasView.ox1 = (int) (x1 * CONST.SCALE/ 100);
        CanvasView.ox2 = (int) (x2 * CONST.SCALE/ 100);
        CanvasView.ox3 = (int) (x3 * CONST.SCALE/ 100);
        CanvasView.ox4 = (int) (x4 * CONST.SCALE/ 100);

        CanvasView.oy1 = (int) (y1 * CONST.SCALE/ 100);
        CanvasView.oy2 = (int) (y2 * CONST.SCALE/ 100);
        CanvasView.oy3 = (int) (y3 * CONST.SCALE/ 100);
        CanvasView.oy4 = (int) (y4 * CONST.SCALE/ 100);

        CanvasView.clickedDraw = clickedDraw;

        invalidate();
    }


    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        mAlertPlayer = new AlertPlayer(context);

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                invalidate();
            }
        }, 1000, 1000);
    }

    private void init() {
        //Подготовка размера экрана
        mWinSize = new Point();
        Utils.loadWinSize(getContext(), mWinSize);
        mCoo = new Point( mWinSize.x / 2, (mWinSize.y / 2));
        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {

        List<Float> distancesToUo = new ArrayList<>();
        List<String> macUo = new ArrayList<>();

        Point anchor1 = new Point(mCoo.x + ox1, mCoo.y - oy1);
        Point anchor2 = new Point(mCoo.x - ox2, mCoo.y - oy2);
        Point anchor3 = new Point(mCoo.x - ox3, mCoo.y + oy3);
        Point anchor4 = new Point(mCoo.x + ox4, mCoo.y + oy4);
        super.onDraw(canvas);
        HelpDraw.drawColor(canvas);
        //Положение центра для отрисовки текста системы координат
        boolean locationTop;//определние крайней верхней точки
        boolean locationBottom = true;//опрделение крайней нижней точки
        if (anchor1.y < anchor2.y) {
            HelpDraw.drawLikeRectG(canvas, mGreen, anchor1.y);
            HelpDraw.drawLikeRectY(canvas, mYellow, anchor1.y);
            HelpDraw.drawLikeRectR(canvas, mRed, anchor1.y);
            locationTop = true;
        } else {
            HelpDraw.drawLikeRectG(canvas, mGreen, anchor2.y);
            HelpDraw.drawLikeRectY(canvas, mYellow, anchor2.y);
            HelpDraw.drawLikeRectR(canvas, mRed, anchor2.y);
            locationTop = false;
        }
        if (anchor3.y > anchor4.y) {
            HelpDraw.drawLikeRectG(canvas, -mGreen, anchor3.y);
            HelpDraw.drawLikeRectY(canvas, -mYellow, anchor3.y);
            HelpDraw.drawLikeRectR(canvas, -mRed, anchor3.y);
            locationBottom = false;
            // Текст для системы координат
            if (locationTop) {
                HelpDraw.drawText4Coo(canvas, anchor3.y, anchor1.y, anchor2.x, anchor1.x, mCoo, mWinSize, mGridPaint);
            } else {
                HelpDraw.drawText4Coo(canvas, anchor3.y, anchor2.y, anchor2.x, anchor1.x, mCoo, mWinSize, mGridPaint);
            }
        } else {
            HelpDraw.drawLikeRectG(canvas, -mGreen, anchor4.y);
            HelpDraw.drawLikeRectY(canvas, -mYellow, anchor4.y);
            HelpDraw.drawLikeRectR(canvas, -mRed, anchor4.y);
            // Текст для системы координат
            if (locationTop) {
                HelpDraw.drawText4Coo(canvas, anchor4.y, anchor1.y, anchor2.x, anchor1.x, mCoo, mWinSize, mGridPaint);
            } else {
                HelpDraw.drawText4Coo(canvas, anchor4.y, anchor2.y, anchor2.x, anchor1.x, mCoo, mWinSize, mGridPaint);
            }
        }

        // TODO drawGrid сетка
        HelpDraw.drawGrid(canvas, mWinSize, mGridPaint);
        // TODO drawCoo система координат
        HelpDraw.drawCoo(canvas, mCoo, mWinSize, mGridPaint);
        HelpDraw.drawAnchorPoint(canvas, anchor1, 1);
        HelpDraw.drawAnchorPoint(canvas, anchor2, 2);
        HelpDraw.drawAnchorPoint(canvas, anchor3, 3);
        HelpDraw.drawAnchorPoint(canvas, anchor4, 4);

        boolean isAlert = false;
        boolean isWarning = false;

        for (Map.Entry<String, Map<String, MeasuredDistance>> uo : distances.entrySet()) {
            if (uo.getValue() == null)
                continue;

            for (Map.Entry<String, MeasuredDistance> anchor : uo.getValue().entrySet()) {
                MeasuredDistance distToUo = anchor.getValue();

                if (distToUo.time < Instant.now().plusSeconds(-2).getEpochSecond())
                    continue;

                distancesToUo.add(distToUo.distance);
                macUo.add(anchor.getKey());
            }

            if (distancesToUo.size() > 1) {
                System.out.println("clickedDraw: " + clickedDraw);
                float cy = HelpDraw.drawDistances(canvas, distancesToUo, macUo, anchor1, anchor2, anchor3, anchor4, clickedDraw);
                float cyLargest12;
                float cyLower12;
                float cyLargest34;
                float cyLower34;

                //Определение крайней точки в 1-2 пи 3-4 четвертях координатоной плоскости
                if (locationTop){
                    //anchor1 - верхняя точка
                    cyLargest12 = anchor1.y;
                    cyLower12 = anchor2.y;
                }
                else {
                    //anchor2 - верхняя точка
                    cyLargest12 = anchor2.y;
                    cyLower12 = anchor1.y;
                }

                if (locationBottom){
                    //anchor4 - верхняя точка
                    cyLargest34 = anchor3.y;
                    cyLower34 = anchor4.y;
                }
                else {
                    //anchor3 - верхняя точка
                    cyLargest34 = anchor4.y;
                    cyLower34 = anchor3.y;
                }

                //Определение позиции движущейся точки относительно координатной четверти
                if (cy < cyLower12 && cy < cyLargest34)
                {
                    //Если точка находится в верхней части координатной плоскости
                    if (cy > cyLargest12 - mRed){
                        isAlert = true;
                    }
                    else if(cy > cyLargest12 - mYellow){
                        isWarning = true;
                    }

                }
                else if(cy>cyLower12 && cy > cyLargest34){
                    //Если точка находится в нижней части координатной плоскости
                    if (cy < cyLower34 + mRed){
                        isAlert = true;
                    }
                    else if(cy < cyLower34 + mYellow){
                        isWarning = true;
                    }
                }
                else
                {
                    isAlert = false;
                    isWarning = false;
                    //Если находится сбоку или внутри вагонеки
                }

                distancesToUo.clear();
                macUo.clear();
            }
        }

        try {
            if (isAlert) {
                mAlertPlayer.playAlert();
            }
            else if (isWarning)
                mAlertPlayer.playWarning();
            else
                mAlertPlayer.stopPlaying();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnDistanceReceived(Map<String, ConcurrentHashMap<String, MeasuredDistance>> newDistances) {

        System.out.println("Got distances to draw: " + distances);
        long timeThreshold = Instant.now().plusSeconds(-2).getEpochSecond();


        // Сначала удалим все расстояния, полученные раньше чем 2 секунды назад.

            Set<String> keys = newDistances.keySet();
            for (String key : keys) {
                Map<String, MeasuredDistance> oldDistancesToAnchorMap = distances.get(key);
                if (oldDistancesToAnchorMap != null){

                    ArrayList<String> keysToRemove = new ArrayList<>();
                    // Находим ключи, отправленные раньше, чем заданное время жизни.
                    for (String oldAnchorKey : oldDistancesToAnchorMap.keySet()) {
                        MeasuredDistance dist = oldDistancesToAnchorMap.get(oldAnchorKey);
                        assert dist != null;
                        if (dist.time < timeThreshold) {
                            keysToRemove.add(oldAnchorKey);
                        }
                    }
                    // Удаляем протухшие ключи
                    for (String expiredKey : keysToRemove)
                    {
                        oldDistancesToAnchorMap.remove(expiredKey);
                    }
                }
            }

            keys = newDistances.keySet();
            // Теперь обновим значения оставшихся расстояний или добавим новые.
            for (String tagKey : keys) {
                if (!distances.containsKey(tagKey)) {
                    distances.put(tagKey, new ConcurrentHashMap<>());
                }
                ConcurrentHashMap<String, MeasuredDistance> newDistancesToAnchor = (ConcurrentHashMap<String, MeasuredDistance>) newDistances.get(tagKey);
                Map<String, MeasuredDistance> oldDistancesToAnchor = distances.get(tagKey);

                if (newDistancesToAnchor == null)
                    continue;
                    Set<String> anchorKeys = newDistancesToAnchor.keySet();

                for (String anchorKey : anchorKeys) {
                        MeasuredDistance newDist = newDistancesToAnchor.get(anchorKey);
                        // отбрасываем пакеты без значения decawave
                        assert newDist != null;
                        if (newDist.decawave_m == 0)
                            continue;
                        assert oldDistancesToAnchor != null;
                        if (oldDistancesToAnchor.containsKey(anchorKey)) {
                            oldDistancesToAnchor.replace(anchorKey, newDist);
                        } else {
                            oldDistancesToAnchor.put(anchorKey, newDist);
                        }

                }
            }

         checkAlerts();
    }

    private void checkAlerts()
    {
        boolean hasAlert = false;
        boolean hasWarning = false;
    }
}
