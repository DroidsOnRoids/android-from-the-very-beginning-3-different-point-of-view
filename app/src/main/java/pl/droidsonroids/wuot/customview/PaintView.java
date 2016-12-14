package pl.droidsonroids.wuot.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {

    Paint paint;
    Path path = new Path();
    private Bitmap drawingCache;

    public PaintView(final Context context) {
        this(context, null);
    }

    public PaintView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PaintView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        paint = new Paint();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        final float defaultLineWidth = getResources().getDimension(R.dimen.default_line_width);
        if (attributeSet != null) {
            final TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.PaintView);
            final int color = typedArray.getColor(R.styleable.PaintView_lineColor, Color.BLACK);
            paint.setColor(color);
            final float lineWidth = typedArray.getDimension(R.styleable.PaintView_lineWidth, defaultLineWidth);
            paint.setStrokeWidth(lineWidth);
            typedArray.recycle();
        } else {
            paint.setStrokeWidth(defaultLineWidth);
            paint.setColor(Color.BLACK);
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        Log.d("onMeasure", "width mode: " + MeasureSpec.getMode(widthMeasureSpec)
                + ", width size: " + MeasureSpec.getSize(widthMeasureSpec) +
                ", height mode: " + MeasureSpec.getMode(heightMeasureSpec) + ", height size: " + MeasureSpec.getSize(heightMeasureSpec));
        int smallerSize = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(smallerSize, smallerSize);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            buildDrawingCache();
            drawingCache = getDrawingCache();
            drawingCache=drawingCache.copy(drawingCache.getConfig(), false);
            destroyDrawingCache();
            path.reset();
            path.moveTo(event.getX(), event.getY());
        } else {
            path.lineTo(event.getX(), event.getY());
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (drawingCache!=null){
            canvas.drawBitmap(drawingCache,0,0,null);
        }
        canvas.drawPath(path, paint);
    }
}
