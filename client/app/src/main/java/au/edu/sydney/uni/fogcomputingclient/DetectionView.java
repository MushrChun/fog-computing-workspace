package au.edu.sydney.uni.fogcomputingclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by cshe6391 on 22/03/18.
 */

public class DetectionView extends View {

    private Paint mPaint4frame;
    private Paint mPaint4label;

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
        mPaint4frame = new Paint();
        mPaint4frame.setColor(Color.BLUE);
        mPaint4frame.setStrokeWidth(8);
        mPaint4frame.setStyle(Paint.Style.STROKE);


        mPaint4label = new Paint();
        mPaint4label.setColor(Color.YELLOW);
        mPaint4label.setTextSize(48);
        mFrames = new ArrayList<>();
    }

    private Collection<DetectionFrame> mFrames;

    public void refreshDetectionFrame(Collection<DetectionFrame>  newFrameSet){
        mFrames.clear();
        mFrames.addAll(newFrameSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(DetectionFrame frame: mFrames){
            canvas.drawRect(frame.x, frame.y, frame.x + frame.w, frame.y + frame.h, mPaint4frame);
            canvas.drawText(frame.label, frame.x, frame.y - 20, mPaint4label);
        }

    }
}
