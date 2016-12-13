package pl.droidsonroids.wuot.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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

public class PaintView extends View {

	LinkedList<PointF> line = new LinkedList<>();

	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Path path = new Path();
	Bitmap cacheBitmap;

	public PaintView(final Context context) {
		this(context, null);
	}

	public PaintView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PaintView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public PaintView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

		final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PaintView, defStyleAttr, defStyleRes);

		final int color = attributes.getColor(R.styleable.PaintView_paintColor, Color.BLACK);
		paint.setColor(color);

		final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		final float defaultWidthPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, displayMetrics);
		final float strokeWidth = attributes.getDimension(R.styleable.PaintView_paintStrokeWidth, defaultWidthPixels);
		paint.setStrokeWidth(strokeWidth);

		attributes.recycle();

		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
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
