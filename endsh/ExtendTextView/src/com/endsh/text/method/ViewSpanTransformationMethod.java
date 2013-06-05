package com.endsh.text.method;

import com.endsh.text.TextUtils;
import com.endsh.text.style.ViewSpan;

import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.TransformationMethod;
import android.view.View;

public class ViewSpanTransformationMethod implements TransformationMethod {

	@Override
	public CharSequence getTransformation(CharSequence source, View view) {
		SpannableString spanned = null;
		CharSequence ret = source;
		if (source instanceof Spanned)
			spanned = new SpannableString(source);
		
		if (spanned != null) {
			ViewSpan[] vsp = spanned.getSpans(0, spanned.length(), ViewSpan.class);
			for (int i = 0; i < vsp.length; i++) {
        		int a = spanned.getSpanStart(vsp[i]);
        		int b = spanned.getSpanEnd(vsp[i]);
        		spanned.removeSpan(vsp[i]);
			}
		}
		
		return ret;
	}

	@Override
	public void onFocusChanged(View view, CharSequence sourceText,
			boolean focused, int direction, Rect previouslyFocusedRect) {
		
	}

}
