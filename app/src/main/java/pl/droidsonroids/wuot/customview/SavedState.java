package pl.droidsonroids.wuot.customview;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.AbsSavedState;

import java.util.Arrays;
import java.util.LinkedList;

public class SavedState extends AbsSavedState {
	Bitmap cacheBitmap;
	LinkedList<PointF> line;

	public SavedState(Parcelable superState, Bitmap cacheBitmap, LinkedList<PointF> line) {
		super(superState);
		this.cacheBitmap = cacheBitmap;
		this.line = line;
	}

	public SavedState(Parcel source) {
		super(source);
		final ClassLoader classLoader = getClass().getClassLoader();
		cacheBitmap = source.readParcelable(classLoader);
		final PointF[] points = (PointF[]) source.readParcelableArray(classLoader);
		line = new LinkedList<>(Arrays.asList(points));
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeParcelable(cacheBitmap, 0);
		out.writeParcelableArray(line.toArray(new PointF[line.size()]), 0);
	}

	public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
		public SavedState createFromParcel(Parcel in) {
			return new SavedState(in);
		}

		public SavedState[] newArray(int size) {
			return new SavedState[size];
		}
	};
}
