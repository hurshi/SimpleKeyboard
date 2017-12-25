
package hurshi.github.com.library;

import android.app.Activity;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;


public class OnSimpleKeyboardActionListener implements OnKeyboardActionListener {

    private Activity activity;
    private SimpleKeyboard keyboardView;

    public OnSimpleKeyboardActionListener(Activity activity, SimpleKeyboard keyboardView) {
        this.activity = activity;
        this.keyboardView = keyboardView;
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
        EditText currentEdittext = keyboardView.currentEdittext;
        Editable editable = currentEdittext.getText();
        int start = currentEdittext.getSelectionStart();
        editable.insert(start, text);
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case -1010:
                keyboardView.changeShift();
                return;
            case -1011:
                EditText currentEdittext = keyboardView.currentEdittext;
                Editable editable = currentEdittext.getText();
                int start = currentEdittext.getSelectionStart();
                editable.delete(start > 0 ? (start - 1) : 0, start);
                return;
            case -1012:
                activity.dispatchKeyEvent(new KeyEvent(EditorInfo.IME_ACTION_DONE, KeyEvent.KEYCODE_ENTER));
                return;
            case -1000:
                keyboardView.setKeyBoard2Normal();
                return;
            case -1001:
                keyboardView.setKeyBoard2Nums();
                return;
            case -1002:
                keyboardView.setKeyboard2More();
                return;
        }
        if (keyboardView.isShift() && isLetter(primaryCode)) {
            primaryCode = Character.toUpperCase(primaryCode);
        }

        EditText currentEdittext = keyboardView.currentEdittext;
        Editable editable = currentEdittext.getText();
        int start = currentEdittext.getSelectionStart();
        editable.insert(start, Character.toString((char) primaryCode));
    }

    private boolean isLetter(int primaryCode) {//判断是否为字母
        if (primaryCode >= 97 && primaryCode <= 122) {
            return true;
        } else {
            return false;
        }
    }
}