package pl.droidsonroids.wuot.customview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createViewUsingXmlResource();
	}

	private void createPaintViewProgrammatically() {
		LinearLayout linearLayout = new LinearLayout(this);
		TextView textView = new TextView(this);
		linearLayout.addView(textView);

		textView.setText("Hello World!");

		setContentView(linearLayout);
	}

	private void createViewUsingXmlResource() {
		setContentView(R.layout.activity_main);
	}
}
