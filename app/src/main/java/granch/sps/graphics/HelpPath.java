package granch.sps.graphics;

import android.graphics.Path;
import android.graphics.Point;

import granch.sps.CONST;

public class HelpPath {
    /**
     * Путь системы координат
     *
     * @param coo координатная точка
     * @param winSize размер экрана
     * возврат системы координат
     */
    public static Path cooPath(Point coo, Point winSize) {
        Path path = new Path();
        // х положительная полуось
        path.moveTo(coo.x, coo.y);
        path.lineTo(winSize.x, coo.y);
        // х отрицательная полуось
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x - winSize.x, coo.y);
        // у положительная полуось
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x, coo.y - (winSize.y));
        // у отрицательня полуось
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x, winSize.y);
        return path;
    }

    /**
     * Отрисовка сетки
     *
     * @param step маленькая квадратная длина стороны
     * @param winSize размер экрана
     */
    public static Path gridPath(float step, Point winSize) {
        Path path = new Path();
        float n = CONST.CELL_START/CONST.CELL_SIZE;
        for (float i = 1; i < winSize.y / step + 1; i++) {
            path.moveTo(1, step * i);
            path.lineTo(winSize.x, step * i);
        }
        for (float i = n; i < winSize.x / step + 1; i++) {
            path.moveTo(step * i, 0);
            path.lineTo(step * i, winSize.y);
        }
        return path;
    }


}
