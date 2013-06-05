/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.endsh.text;

import android.text.Spannable;


/**
 * Utility class for manipulating cursors and selections in CharSequences.
 * A cursor is a selection where the start and end are at the same offset.
 */
public class Selection {
    private Selection() { /* cannot be instantiated */ }

    /*
     * Retrieving the selection
     */

    /**
     * Return the offset of the selection anchor or cursor, or -1 if
     * there is no selection or cursor.
     */
    public static final int getSelectionStart(CharSequence text) {
    	return android.text.Selection.getSelectionStart(text);
    }
   
    /**
     * Return the offset of the selection edge or cursor, or -1 if
     * there is no selection or cursor.
     */
    public static final int getSelectionEnd(CharSequence text) {
    	return android.text.Selection.getSelectionEnd(text);
    }

    /*
     * Setting the selection
     */

    // private static int pin(int value, int min, int max) {
    //     return value < min ? 0 : (value > max ? max : value);
    // }
   
    /**
     * Set the selection anchor to <code>start</code> and the selection edge
     * to <code>stop</code>.
     */
    public static void setSelection(Spannable text, int start, int stop) {
        android.text.Selection.setSelection(text, start, stop);
    }

    /**
     * Move the cursor to offset <code>index</code>.
     */
    public static final void setSelection(Spannable text, int index) {
        setSelection(text, index, index);
    }

    /**
     * Select the entire text.
     */
    public static final void selectAll(Spannable text) {
        setSelection(text, 0, text.length());
    }

    /**
     * Move the selection edge to offset <code>index</code>.
     */
    public static final void extendSelection(Spannable text, int index) {
    	android.text.Selection.extendSelection(text, index);
    }

    /**
     * Remove the selection or cursor, if any, from the text.
     */
    public static final void removeSelection(Spannable text) {
        android.text.Selection.removeSelection(text);
    }

    /*
     * Moving the selection within the layout
     */

    /**
     * Move the cursor to the buffer offset physically above the current
     * offset, or return false if the cursor is already on the top line.
     */
    public static boolean moveUp(Spannable text, Layout layout) {
        int start = getSelectionStart(text);
        int end = getSelectionEnd(text);

        if (start != end) {
            int min = Math.min(start, end);
            int max = Math.max(start, end);

            setSelection(text, min);

            if (min == 0 && max == text.length()) {
                return false;
            }

            return true;
        } else {
            int line = layout.getLineForOffset(end);

            if (line > 0) {
                int move;

                if (layout.getParagraphDirection(line) ==
                    layout.getParagraphDirection(line - 1)) {
                    float h = layout.getPrimaryHorizontal(end);
                    move = layout.getOffsetForHorizontal(line - 1, h);
                } else {
                    move = layout.getLineStart(line - 1);
                }

                setSelection(text, move);
                return true;
            }
        }

        return false;
    }

    /**
     * Move the cursor to the buffer offset physically below the current
     * offset, or return false if the cursor is already on the bottom line.
     */
    public static boolean moveDown(Spannable text, Layout layout) {
        int start = getSelectionStart(text);
        int end = getSelectionEnd(text);

        if (start != end) {
            int min = Math.min(start, end);
            int max = Math.max(start, end);

            setSelection(text, max);

            if (min == 0 && max == text.length()) {
                return false;
            }

            return true;
        } else {
            int line = layout.getLineForOffset(end);

            if (line < layout.getLineCount() - 1) {
                int move;

                if (layout.getParagraphDirection(line) ==
                    layout.getParagraphDirection(line + 1)) {
                    float h = layout.getPrimaryHorizontal(end);
                    move = layout.getOffsetForHorizontal(line + 1, h);
                } else {
                    move = layout.getLineStart(line + 1);
                }

                setSelection(text, move);
                return true;
            }
        }

        return false;
    }

    /**
     * Move the cursor to the buffer offset physically to the left of
     * the current offset, or return false if the cursor is already
     * at the left edge of the line and there is not another line to move it to.
     */
    public static boolean moveLeft(Spannable text, Layout layout) {
        int start = getSelectionStart(text);
        int end = getSelectionEnd(text);

        if (start != end) {
            setSelection(text, chooseHorizontal(layout, -1, start, end));
            return true;
        } else {
            int to = layout.getOffsetToLeftOf(end);

            if (to != end) {
                setSelection(text, to);
                return true;
            }
        }

        return false;
    }

    /**
     * Move the cursor to the buffer offset physically to the right of
     * the current offset, or return false if the cursor is already at
     * at the right edge of the line and there is not another line
     * to move it to.
     */
    public static boolean moveRight(Spannable text, Layout layout) {
        int start = getSelectionStart(text);
        int end = getSelectionEnd(text);

        if (start != end) {
            setSelection(text, chooseHorizontal(layout, 1, start, end));
            return true;
        } else {
            int to = layout.getOffsetToRightOf(end);

            if (to != end) {
                setSelection(text, to);
                return true;
            }
        }

        return false;
    }

    /**
     * Move the selection end to the buffer offset physically above
     * the current selection end.
     */
    public static boolean extendUp(Spannable text, Layout layout) {
        int end = getSelectionEnd(text);
        int line = layout.getLineForOffset(end);

        if (line > 0) {
            int move;

            if (layout.getParagraphDirection(line) ==
                layout.getParagraphDirection(line - 1)) {
                float h = layout.getPrimaryHorizontal(end);
                move = layout.getOffsetForHorizontal(line - 1, h);
            } else {
                move = layout.getLineStart(line - 1);
            }

            extendSelection(text, move);
            return true;
        } else if (end != 0) {
            extendSelection(text, 0);
            return true;
        }

        return true;
    }

    /**
     * Move the selection end to the buffer offset physically below
     * the current selection end.
     */
    public static boolean extendDown(Spannable text, Layout layout) {
        int end = getSelectionEnd(text);
        int line = layout.getLineForOffset(end);

        if (line < layout.getLineCount() - 1) {
            int move;

            if (layout.getParagraphDirection(line) ==
                layout.getParagraphDirection(line + 1)) {
                float h = layout.getPrimaryHorizontal(end);
                move = layout.getOffsetForHorizontal(line + 1, h);
            } else {
                move = layout.getLineStart(line + 1);
            }

            extendSelection(text, move);
            return true;
        } else if (end != text.length()) {
            extendSelection(text, text.length());
            return true;
        }

        return true;
    }

    /**
     * Move the selection end to the buffer offset physically to the left of
     * the current selection end.
     */
    public static boolean extendLeft(Spannable text, Layout layout) {
        int end = getSelectionEnd(text);
        int to = layout.getOffsetToLeftOf(end);

        if (to != end) {
            extendSelection(text, to);
            return true;
        }

        return true;
    }

    /**
     * Move the selection end to the buffer offset physically to the right of
     * the current selection end.
     */
    public static boolean extendRight(Spannable text, Layout layout) {
        int end = getSelectionEnd(text);
        int to = layout.getOffsetToRightOf(end);

        if (to != end) {
            extendSelection(text, to);
            return true;
        }

        return true;
    }

    public static boolean extendToLeftEdge(Spannable text, Layout layout) {
        int where = findEdge(text, layout, -1);
        extendSelection(text, where);
        return true;
    }

    public static boolean extendToRightEdge(Spannable text, Layout layout) {
        int where = findEdge(text, layout, 1);
        extendSelection(text, where);
        return true;
    }

    public static boolean moveToLeftEdge(Spannable text, Layout layout) {
        int where = findEdge(text, layout, -1);
        setSelection(text, where);
        return true;
    }

    public static boolean moveToRightEdge(Spannable text, Layout layout) {
        int where = findEdge(text, layout, 1);
        setSelection(text, where);
        return true;
    }

    private static int findEdge(Spannable text, Layout layout, int dir) {
        int pt = getSelectionEnd(text);
        int line = layout.getLineForOffset(pt);
        int pdir = layout.getParagraphDirection(line);

        if (dir * pdir < 0) {
            return layout.getLineStart(line);
        } else {
            int end = layout.getLineEnd(line);

            if (line == layout.getLineCount() - 1)
                return end;
            else
                return end - 1;
        }
    }

    private static int chooseHorizontal(Layout layout, int direction,
                                        int off1, int off2) {
        int line1 = layout.getLineForOffset(off1);
        int line2 = layout.getLineForOffset(off2);

        if (line1 == line2) {
            // same line, so it goes by pure physical direction

            float h1 = layout.getPrimaryHorizontal(off1);
            float h2 = layout.getPrimaryHorizontal(off2);

            if (direction < 0) {
                // to left

                if (h1 < h2)
                    return off1;
                else
                    return off2;
            } else {
                // to right

                if (h1 > h2)
                    return off1;
                else
                    return off2;
            }
        } else {
            // different line, so which line is "left" and which is "right"
            // depends upon the directionality of the text

            // This only checks at one end, but it's not clear what the
            // right thing to do is if the ends don't agree.  Even if it
            // is wrong it should still not be too bad.
            int line = layout.getLineForOffset(off1);
            int textdir = layout.getParagraphDirection(line);

            if (textdir == direction)
                return Math.max(off1, off2);
            else
                return Math.min(off1, off2);
        }
    }
}
