package granch.sps.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import java.util.List;

import granch.sps.CONST;

public class HelpDraw {


    /**
     * Отрисовка сетки
     *
     * @param canvas  canvas
     * @param winSize размер экрана
     * @param paint
     */
    public static void drawGrid(Canvas canvas, Point winSize, Paint paint) {
        // Инициализируем сетку кисти
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        // Установить эффект пунктирной линии new float [] {видимая длина, невидимая длина}, значение смещения
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        canvas.drawPath(HelpPath.gridPath(CONST.CELL_SIZE, winSize), paint);
    }

    /**
     * Отрисовка системы координат
     *
     * @param canvas  canvas
     * @param coo     начало системы координат
     * @param winSize размер экрана
     * @param paint
     */
    public static void drawCoo(@NonNull Canvas canvas, Point coo, Point winSize, Paint paint) {
        // Инициализация сетки кисти
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        // Установка эффекта пунктирной линии new float [] {видимая длина, невидимая длина}, значение смещения
        paint.setPathEffect(null);
        // Отрисовка прямой линии
        canvas.drawPath(HelpPath.cooPath(coo, winSize), paint);
        // Стрелка влево
        canvas.drawLine(winSize.x, coo.y, winSize.x - 0.4f*CONST.CELL_SIZE, coo.y - 0.2f*CONST.CELL_SIZE, paint);
        canvas.drawLine(winSize.x, coo.y, winSize.x - 0.4f*CONST.CELL_SIZE, coo.y + 0.2f*CONST.CELL_SIZE, paint);
        // Стрелка вниз
        canvas.drawLine(coo.x, winSize.y, coo.x - 0.2f*CONST.CELL_SIZE, winSize.y - 0.4f*CONST.CELL_SIZE, paint);
        canvas.drawLine(coo.x, winSize.y, coo.x + 0.2f*CONST.CELL_SIZE, winSize.y - 0.4f*CONST.CELL_SIZE, paint);


    }

    /**
     * Отрисовка тескта для системы координат
     *
     * @param canvas  canvas
     * @param winSize размер экрана
     * @param paint
     */
    public static void drawText4Coo(Canvas canvas, float cyBottom, float cyTop, float cxLeft, float cxRight, Point mCoo, Point winSize, Paint paint) {

         float share_10 = 0.1f*CONST.CELL_SIZE;
         float share_20 = 0.2f*CONST.CELL_SIZE;
         float share_40 = 0.4f*CONST.CELL_SIZE;
         float share_50 = 0.5f*CONST.CELL_SIZE;
         float share_60 = 0.6f*CONST.CELL_SIZE;

        // Рисуем текст
        paint.setTextSize(50);
        canvas.drawText("m", winSize.x - share_60, mCoo.y - share_40, paint);
        paint.setTextSize(25);
        // X текст положительной оси
        for (int i = 2; i < (winSize.x - cxRight) / share_50; i+=2) {
            paint.setStrokeWidth(2);
            canvas.drawText(i + "", cxRight - share_20 + CONST.CELL_SIZE * i, mCoo.y + share_40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(cxRight + CONST.CELL_SIZE * i, mCoo.y, cxRight + CONST.CELL_SIZE * i, mCoo.y - share_10, paint);
        }
        // X текст отрицательной оси
        for (int i = 2; i < cxLeft / share_50; i+=2) {
            paint.setStrokeWidth(2);
            canvas.drawText(i + "", cxLeft - share_20 - CONST.CELL_SIZE * i, mCoo.y + share_40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(cxLeft - CONST.CELL_SIZE * i, mCoo.y, cxLeft - CONST.CELL_SIZE * i, mCoo.y - share_10, paint);
        }
        // y текст положительной оси
        for (int i = 2; i < (winSize.y - cyTop); i+=2) {
            paint.setStrokeWidth(2);
            canvas.drawText(i + "", mCoo.x + share_20, cyTop + share_10 - CONST.CELL_SIZE * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(mCoo.x, cyTop + CONST.CELL_SIZE * (-i), mCoo.x + share_10, cyTop - CONST.CELL_SIZE * i, paint);
        }
        // y текст отрицательной оси
        for (int i = 1; i < cyBottom / share_50; i+=2) {
            paint.setStrokeWidth(2);
            canvas.drawText(i + "", mCoo.x+ share_20, cyBottom + share_10 + CONST.CELL_SIZE * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(mCoo.x, cyBottom + CONST.CELL_SIZE * i, mCoo.x + share_10, cyBottom + CONST.CELL_SIZE * i, paint);
        }

    }

    /**
     * Отрисовка цвета
     *
     * @param canvas
     */
    static void drawColor(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#C0C0C0"));
    }

    /**
     * Зеленая зона
     *
     * @param canvas
     */

    static void drawLikeRectG(Canvas canvas, float radius, float cy) {
        // Рисуем круг (прямоугольник, кисть)
        // создаём пустой прямоугольник и задаём координаты верхней левой и нижней правой точек
        Rect myRect = new Rect();
        myRect.set(0, (int) (cy - radius), (int) (Utils.getScreenWidth()), (int) cy);
        // кисть
        Paint mGreenPaint = new Paint();
        // цвет кисти - зелёный
        mGreenPaint.setColor(Color.parseColor("#00BC00"));
        // тип - заливка
        mGreenPaint.setStyle(Paint.Style.FILL);
        // закрашиваем  прямоугольником вторую половину экрана
        canvas.drawRect(myRect, mGreenPaint);
    }

    /**
     * Желтая зона
     *
     * @param canvas
     */

    static void drawLikeRectY(Canvas canvas, float radius, float cy) {
        // Рисуем круг (прямоугольник, кисть)
        // создаём пустой прямоугольник и задаём координаты верхней левой и нижней правой точек
        Rect myRect = new Rect();
        myRect.set(0, (int) (cy - radius), (int) (Utils.getScreenWidth()), (int) cy);
        // кисть
        Paint mYellowPaint = new Paint();
        // цвет кисти - желтый
        mYellowPaint.setColor(Color.parseColor("#FFD700"));
        // тип - заливка
        mYellowPaint.setStyle(Paint.Style.FILL);
        // закрашиваем  прямоугольником вторую половину экрана
        canvas.drawRect(myRect, mYellowPaint);
    }

    /**
     * Красная зона
     *
     * @param canvas radius
     */

    static void drawLikeRectR(Canvas canvas, float radius, float cy) {
        // Рисуем круг (прямоугольник, кисть)
        // создаём пустой прямоугольник и задаём координаты верхней левой и нижней правой точек
        Rect myRect = new Rect();
        myRect.set(0, (int) (cy - radius), (int) (Utils.getScreenWidth()), (int) cy);
        // кисть
        Paint mRedPaint = new Paint();
        // цвет кисти - красный
        mRedPaint.setColor(Color.parseColor("#B00000"));
        // тип - заливка
        mRedPaint.setStyle(Paint.Style.FILL);
        // закрашиваем  прямоугольником вторую половину экрана
        canvas.drawRect(myRect, mRedPaint);
    }


    public static void drawDistPoints(Canvas canvas, float cx, float cy, int color) {
        Paint mDistPaint = new Paint();
        mDistPaint.setAntiAlias(true);
        mDistPaint.setColor(color);
        mDistPaint.setTextSize(35.0f);
        mDistPaint.setStrokeWidth(2.0f);
        canvas.drawCircle(cx, cy, CONST.RADIUS, mDistPaint);
    }

    public static float drawDistances(Canvas canvas, List<Float> arrDist,List<String> arrMac,
                                     Point anchor1, Point anchor2, Point anchor3, Point anchor4, boolean clickedDraw) {

        int color = Color.parseColor("#003153");
        //параметры кругов
        Paint mRPaint = new Paint();
        mRPaint.setStyle(Paint.Style.STROKE);
        mRPaint.setStrokeWidth(10);
        mRPaint.setColor(Color.parseColor("#4C4A48"));
        Paint mBPaint = new Paint();
        mBPaint.setStyle(Paint.Style.STROKE);
        mBPaint.setStrokeWidth(10);
        mBPaint.setColor(Color.BLACK);

        //P3
        Paint mPPaint = new Paint();
        mPPaint.setAntiAlias(true);
        mPPaint.setColor(Color.YELLOW);
        mPPaint.setTextSize(35.0f);
        mPPaint.setStrokeWidth(2.0f);

        float r1;
        float r2;
        r1 = arrDist.get(0) * CONST.SCALE;
        r2 = arrDist.get(1) * CONST.SCALE;

        Point mP1;
        Point mP2;
        if (arrMac.contains(Anchor.mac1) || arrMac.contains(Anchor.mac2)) {
            if (Anchor.getZoneRightLeft(arrMac.get(0))) {
                mP1 = anchor1;
                mP2 = anchor2;
            } else {
                mP1 = anchor2;
                mP2 = anchor1;
            }

        } else if (arrMac.contains(Anchor.mac3) || arrMac.contains(Anchor.mac4)) {
            if (Anchor.getZoneRightLeft(arrMac.get(0))) {
                mP1 = anchor4;
                mP2 = anchor3;
            } else {
                mP1 = anchor3;
                mP2 = anchor4;
            }

        } else {
            mP1 = new Point(((int) (Utils.getScreenWidth() / 2)), ((int) (Utils.getScreenHeight() / 2)));
            mP2 = new Point(((int) (Utils.getScreenWidth() / 2)), ((int) (Utils.getScreenHeight() / 2)));
        }


        float d = (float) Math.sqrt(Math.pow(mP2.x - mP1.x, 2) + Math.pow(mP2.y - mP1.y, 2));
        float a = (float) ((Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(d, 2)) / (2 * d));
        float n = (float) (Math.pow(r1, 2) - Math.pow(a, 2));
        float h = (float) Math.sqrt(n);
        float x3 = mP1.x + a / d * (mP2.x - mP1.x);
        float y3 = mP1.y + a / d * (mP2.y - mP1.y);
        float cx = x3 + h / d * (mP2.y - mP1.y);
        float cy = y3 - h / d * (mP2.x - mP1.x);
        float cx_ = x3 - h / d * (mP2.y - mP1.y);
        float cy_ = y3 + h / d * (mP2.x - mP1.x);
        float cy_fin;
        //отрисовка кругов

        System.out.println("DRAW: " +clickedDraw);
        if (clickedDraw){
            canvas.drawCircle(mP1.x, mP1.y, r1, mRPaint);
            canvas.drawCircle(mP2.x, mP2.y, r2, mBPaint);
        }


        //отрисовка P3
        //canvas.drawCircle(x3, y3, CONST.RADIUS, mPPaint);


        if (Anchor.getZoneFrontBack(arrMac.get(0))) {
            if (cy > cy_) {
                HelpDraw.drawDistPoints(canvas, cx_, cy_, color);
                cy_fin = cy_;
            } else {
                HelpDraw.drawDistPoints(canvas, cx, cy, color);
                cy_fin = cy;
            }

            //рисуем точку с наименьшим y
        } else {
            if (cy > cy_) {
                HelpDraw.drawDistPoints(canvas, cx, cy, color);
                cy_fin = cy;
            } else {
                HelpDraw.drawDistPoints(canvas, cx_, cy_, color);
                cy_fin = cy_;
            }
        }
        return cy_fin;
    }


    public static void drawAnchorPoint(Canvas canvas, Point point, int number) {
        Paint mDistPaint = new Paint();
        mDistPaint.setAntiAlias(true);
        mDistPaint.setColor(Color.BLACK);
        Paint mNumberPaint = new Paint();
        mNumberPaint.setTextSize(40.0f);
        mNumberPaint.setStrokeWidth(2.0f);
        canvas.drawText(String.valueOf(number), point.x, point.y, mNumberPaint);
        canvas.drawCircle(point.x, point.y, 10, mDistPaint);
    }


}
