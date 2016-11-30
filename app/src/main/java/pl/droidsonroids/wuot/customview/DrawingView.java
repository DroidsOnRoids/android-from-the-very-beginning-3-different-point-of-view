package pl.droidsonroids.wuot.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View {

	private final ArrayList<PointF> points = new ArrayList<>();

	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
		points.add(new PointF(event.getX(), event.getY()));
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		for (PointF point : points) {
			canvas.drawPoint(point.x, point.y, paint);
		}
	}
}
