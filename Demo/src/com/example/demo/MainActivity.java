package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.endsh.text.Selection;
import com.endsh.text.style.ViewSpan;
import com.endsh.widget.ExtendEditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ExtendEditText editor = (ExtendEditText)findViewById(R.id.EditTextBody);
	    Button insertBtn = (Button)findViewById(R.id.ButtonLocal);
	    insertBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editable text = editor.getText();
			    int i = Selection.getSelectionStart(text);
			    int j = Selection.getSelectionEnd(text);
			    text = text.replace(i, j, "abc");
			    View view = View.inflate(MainActivity.this, R.layout.image_view, null);
			    ViewSpan localViewSpan = new ViewSpan(view);
			    text.setSpan(localViewSpan, i, "abc".length() + i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
