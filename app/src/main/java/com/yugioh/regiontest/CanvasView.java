package com.yugioh.regiontest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CanvasView extends FrameLayout {
    private Path mPath = new Path();
    private Paint mPaint = new Paint();

    // 是否画区域
    private boolean mIsRegion = false;
    private boolean mIsOnDown = false;
    private float mPointX = 0f;
    private float mPointY = 0f;
    private float mX = 0f;
    private float mY = 0f;

    public CanvasView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(30f);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(102);
        mPaint.setPathEffect(new CornerPathEffect(20f));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void setOnDown(boolean isOnDown) {
        mIsOnDown = isOnDown;
    }

    public void setRegion() {
        mIsRegion = !mIsRegion;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                if (mIsOnDown) {
                    mPointX = event.getX();
                    mPointY = event.getY();
                }else {
                    mPath.reset();
                    mPath.moveTo(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsOnDown) {
                    mPath.lineTo(event.getX(), event.getY());
                }else {
                    // 移动path
//                    float x = event.getX() - mX;
//                    float y = event.getY() - mY;
//                    Matrix matrix = new Matrix();
//                    matrix.postTranslate(x, y);
//                    mPath.transform(matrix);
//                    invalidate();
//                    mX = event.getX();
//                    mY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                invalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        canvas.drawPath(mPath, mPaint);

        Paint paint2 = new Paint();
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(16f);
        paint2.setColor(Color.RED);
        paint2.setAlpha(255);
        paint2.setPathEffect(new CornerPathEffect(16f));
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawPath(new Path(mPath), paint2);


        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        RectF bounds = new RectF();
        mPath.computeBounds(bounds, true);
        canvas.drawRect(bounds, paint);

//        RectF boundsx = new RectF(bounds);
//        // todo
//        boundsx.left -= 10f;
//        boundsx.top -= 10f;
//        boundsx.right += 10f;
//        boundsx.bottom += 10f;
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(boundsx, paint);

        Region region = new Region();
        if (mIsRegion) {
            boolean test = region.setPath(mPath, new Region((int)(bounds.left), (int)(bounds.top),(int)(bounds.right), (int)(bounds.bottom)));
            Log.e("ousyxx", "isTrue--" + test);
            drawRegion(canvas, region, Color.WHITE);
        }

        if (mIsOnDown) {
//            paint.setColor(Color.BLUE);
//            RectF rectPoint = new RectF(mPointX - 10f, mPointY - 10f, mPointX + 10f, mPointY + 10f);
//            canvas.drawRect(rectPoint, paint);
//
//            boolean result = !region.quickReject((int)rectPoint.left, (int)rectPoint.top, (int)rectPoint.right, (int)rectPoint.bottom);

            Region regionPoint = new Region((int)(mPointX - 10f), (int)(mPointY - 10f), (int)(mPointX + 10f), (int)(mPointY + 10f));
            drawRegion(canvas, regionPoint, Color.BLUE);

            regionPoint.op(region, Region.Op.INTERSECT);
            if (!regionPoint.isEmpty()) {
                drawRegion(canvas, regionPoint, Color.BLACK);
            }
            Log.e("ousyxx", "相交--" + !regionPoint.isEmpty());
        }

//
//        Path pathx = region.getBoundaryPath();
//        mPaint.setColor(Color.BLUE);
//        canvas.drawPath(pathx, mPaint);

//        Region regionx = new Region();
//        regionx.setPath(mPath, new Region((int)(bounds.left + 20f), (int)(bounds.top + 20f),(int)(bounds.right - 20f), (int)(bounds.bottom - 20f)));
//        drawRegion(canvas, regionx, Color.BLUE);


//        Region region1 = new Region((int)bounds.left, (int)bounds.top,(int)bounds.right, (int)bounds.bottom);
//        region.op(region1, Region.Op.REVERSE_DIFFERENCE);
//        drawRegion(canvas, region, Color.WHITE);
    }

    //这个函数不懂没关系，下面会细讲
    private void drawRegion(Canvas canvas, Region rgn, int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        RegionIterator iter = new RegionIterator(rgn);
        Rect r = new Rect();

        while (iter.next(r)) {
            canvas.drawRect(r, paint);
        }
    }
}
