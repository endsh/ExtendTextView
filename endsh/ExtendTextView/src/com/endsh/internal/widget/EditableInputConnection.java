/*
 * Copyright (C) 2007-2008 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.endsh.internal.widget;

import com.endsh.view.inputmethod.BaseInputConnection;
import com.endsh.widget.ExtendTextView;

import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;

public class EditableInputConnection extends BaseInputConnection {
    private static final boolean DEBUG = false;
    private static final String TAG = "EditableInputConnection";

    private final ExtendTextView mExtendTextView;

    public EditableInputConnection(ExtendTextView textview) {
        super(textview, true);
        mExtendTextView = textview;
    }

    public Editable getEditable() {
        ExtendTextView tv = mExtendTextView;
        if (tv != null) {
            return tv.getEditableText();
        }
        return null;
    }
    
    public boolean beginBatchEdit() {
        mExtendTextView.beginBatchEdit();
        return true;
    }
    
    public boolean endBatchEdit() {
        mExtendTextView.endBatchEdit();
        return true;
    }
    
    public boolean clearMetaKeyStates(int states) {
        final Editable content = getEditable();
        if (content == null) return false;
        KeyListener kl = mExtendTextView.getKeyListener();
        if (kl != null) {
            try {
                kl.clearMetaKeyState(mExtendTextView, content, states);
            } catch (AbstractMethodError e) {
                // This is an old listener that doesn't implement the
                // new method.
            }
        }
        return true;
    }
    
    public boolean commitCompletion(CompletionInfo text) {
        if (DEBUG) Log.v(TAG, "commitCompletion " + text);
        mExtendTextView.beginBatchEdit();
        mExtendTextView.onCommitCompletion(text);
        mExtendTextView.endBatchEdit();
        return true;
    }

    public boolean performEditorAction(int actionCode) {
        if (DEBUG) Log.v(TAG, "performEditorAction " + actionCode);
        mExtendTextView.onEditorAction(actionCode);
        return true;
    }
    
    public boolean performContextMenuAction(int id) {
        if (DEBUG) Log.v(TAG, "performContextMenuAction " + id);
        mExtendTextView.beginBatchEdit();
        mExtendTextView.onTextContextMenuItem(id);
        mExtendTextView.endBatchEdit();
        return true;
    }
    
    public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
        if (mExtendTextView != null) {
            ExtractedText et = new ExtractedText();
            if (mExtendTextView.extractText(request, et)) {
                if ((flags&GET_EXTRACTED_TEXT_MONITOR) != 0) {
                    mExtendTextView.setExtracting(request);
                }
                return et;
            }
        }
        return null;
    }
    
    public boolean performPrivateCommand(String action, Bundle data) {
        mExtendTextView.onPrivateIMECommand(action, data);
        return true;
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        if (mExtendTextView == null) {
            return super.commitText(text, newCursorPosition);
        }

        CharSequence errorBefore = mExtendTextView.getError();
        boolean success = super.commitText(text, newCursorPosition);
        CharSequence errorAfter = mExtendTextView.getError();

        if (errorAfter != null && errorBefore == errorAfter) {
            mExtendTextView.setError(null, null);
        }

        return success;
    }

	@Override
	public boolean commitCorrection(CorrectionInfo correctionInfo) {
		// TODO Auto-generated method stub
		return false;
	}
}
