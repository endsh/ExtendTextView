package com.endsh.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.NoCopySpan;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.view.View;

public class ViewSpan extends ReplacementSpan implements NoCopySpan {
	private View mView;
	public ViewSpan(View view) {
		mView = view;
		mView.measure(0, 0);
	}
	
	public void layout(int l, int t) {
		mView.layout(l, t, l + mView.getMeasuredWidth(), t + mView.getMeasuredHeight());
	}
	
	public int getSize(Paint paint, CharSequence text, int start, int end,
			FontMetricsInt fm) {
		
		if (fm != null) {
            fm.ascent = -mView.getMeasuredHeight(); 
            Log.e("ViewSpan", mView.getMeasuredHeight() + "");
            fm.descent = 0; 
            fm.top = fm.ascent;
            fm.bottom = 0;
        }
		
		return mView.getMeasuredWidth();
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end,
			float x, int top, int y, int bottom, Paint paint) {
	}
	
	public View getView() {
		return mView;
	}
}
