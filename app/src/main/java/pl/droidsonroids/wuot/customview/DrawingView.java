package pl.droidsonroids.wuot.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DrawingView extends View {

	private final LinkedList<LinkedList<PointF>> lines = new LinkedList<>();

	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();

    public DrawingView(final Context context) {
		this(context, null);
	}

	public DrawingView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DrawingView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public DrawingView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		final float widthPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, displayMetrics);
		paint.setStrokeWidth(widthPixels);
		paint.setStyle(Paint.Style.STROKE);
	}

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
	    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
		    lines.addLast(new LinkedList<PointF>());
	    }
	    addPointToLastLine(event);
	    invalidate();
	    return true;
    }

	private void addPointToLastLine(MotionEvent event) {
		lines.getLast().addLast(new PointF(event.getX(), event.getY()));
	}

	@Override
    protected void onDraw(final Canvas canvas) {
		for (LinkedList<PointF> line : lines) {
			final PointF firstPoint = line.getFirst();
			path.moveTo(firstPoint.x, firstPoint.y);
			for (PointF point : line) {
				path.lineTo(point.x, point.y);
			}
		}
		canvas.drawPath(path, paint);
    }

}
