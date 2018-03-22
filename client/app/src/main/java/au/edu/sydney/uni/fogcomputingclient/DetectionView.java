package au.edu.sydney.uni.fogcomputingclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cshe6391 on 22/03/18.
 */

public class DetectionView extends View {

    Paint paint;
    Path path;

    public DetectionView(Context context) {
        super(context);
        init();
    }

    public DetectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DetectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        canvas.drawRect(30, 50, 200, 350, paint);
        canvas.drawRect(100, 100, 300, 400, paint);
        //drawRect(left, top, right, bottom, paint)

    }
}
