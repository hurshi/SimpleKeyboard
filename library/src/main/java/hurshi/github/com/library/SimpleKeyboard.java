package hurshi.github.com.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Gavin on 16/5/4.
 */
public class SimpleKeyboard extends KeyboardView {
    private Keyboard normalKeyboard;
    private Keyboard numKeyboard;
    private Keyboard moreKeyboard;
    public EditText currentEdittext;
    private Paint paint;
    private Drawable topBg;

    public SimpleKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setKeyBoard2Normal();
        setPreviewEnabled(false);
        setOnKeyboardActionListener(new OnSimpleKeyboardActionListener(context, this));
        initPaint(context);
    }


    private void initPaint(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(sp2px(context, 18));
        paint.setColor(ContextCompat.getColor(context, R.color.key_text_color));
        topBg = new ColorDrawable(ContextCompat.getColor(context, R.color.top_bg_color));
    }


    public void initEdittext(EditText editText) {
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(v);
                }
            }
        });

        editText.setFocusable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        } else {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
//            editText.setInputType(InputType.TYPE_NULL);
//            editText.setRawInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        Keyboard.Key spaceKey = keys.get(keys.size() - 4);
        canvas.drawLine(0, 0, getWidth(), 0, paint);
        if (null != spaceKey.icon) {
            spaceKey.icon.setBounds(spaceKey.x, spaceKey.y + spaceKey.height / 4, spaceKey.x + spaceKey.width, spaceKey.y + spaceKey.height * 3 / 4);
            spaceKey.icon.draw(canvas);
        }
        paint.setTextSize(sp2px(getContext(), 15));
        canvas.drawText("空格",
                spaceKey.x + (spaceKey.width) / 2,
                spaceKey.y + (spaceKey.height) / 2 + (paint.getTextSize() - paint.descent()) / 2,
                paint);

    }

    public void changeShift() {
        setShifted(!isShift());
    }

    public boolean isShift() {
        return getKeyboard().isShifted();
    }

    public void setKeyBoard2Normal() {
        if (null == normalKeyboard) {
            normalKeyboard = new Keyboard(getContext(), R.xml.keyboard_normal);
        }
        setKeyboard(normalKeyboard);
    }

    public void setKeyBoard2Nums() {
        if (null == numKeyboard) {
            numKeyboard = new Keyboard(getContext(), R.xml.keyboard_num);
        }
        setKeyboard(numKeyboard);
    }

    public void setKeyboard2More() {
        if (null == moreKeyboard) {
            moreKeyboard = new Keyboard(getContext(), R.xml.keyboard_more);
        }
        setKeyboard(moreKeyboard);
    }

    public void showKeyboard(View currentEditView) {
        this.currentEdittext = (EditText) currentEditView;
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
    }

    private int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
