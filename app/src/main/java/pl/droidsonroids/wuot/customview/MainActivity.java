package pl.droidsonroids.wuot.customview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentViewProgrammatically();
	}

	private void setContentViewProgrammatically(){
		final FrameLayout scrollView = new FrameLayout(this);
		final PaintView paintViewBottom = new PaintView(this, false);
		final PaintView paintViewTop = new PaintView(this, true);

		scrollView.addView(paintViewBottom);
		scrollView.addView(paintViewTop);
		setContentView(scrollView);
	}

	private void setContentViewFromLayoutId() {
		setContentView(R.layout.activity_main);
	}

}
