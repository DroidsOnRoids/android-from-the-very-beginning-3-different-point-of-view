package pl.droidsonroids.wuot.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

public class DrawingView extends View {

	LinkedList<PointF> line = new LinkedList<>();

	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Path path = new Path();
	Bitmap cacheBitmap;

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

		final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		final float widthPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, displayMetrics);

		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeWidth(widthPixels);
		paint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {

			buildDrawingCache();
			final Bitmap drawingCache = getDrawingCache();
			cacheBitmap = drawingCache.copy(drawingCache.getConfig(), false);
			destroyDrawingCache();
			line.clear();
		} else {
			addPointToLastLine(event);
			invalidate();
		}

		return true;
	}

	private void addPointToLastLine(MotionEvent event) {
		line.add(new PointF(event.getX(), event.getY()));
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		if (cacheBitmap != null) {
			canvas.drawBitmap(cacheBitmap, 0, 0, null);
		}
		if (line.size() > 1) {
			path.reset();
			final PointF firstPoint = line.getFirst();
			path.moveTo(firstPoint.x, firstPoint.y);
			for (PointF point : line) {
				path.lineTo(point.x, point.y);
			}
			canvas.drawPath(path, paint);

		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		return new SavedState(super.onSaveInstanceState(), cacheBitmap, line);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		cacheBitmap = savedState.cacheBitmap;
		line = savedState.line;
	}
}
