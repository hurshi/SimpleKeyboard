package hurshi.github.com.library;

import android.app.Activity;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Gavin on 16/5/4.
 */
public class SimpleKeyboard extends KeyboardView {
    private Keyboard normalKeyboard;
    private Keyboard numKeyboard;
    private Keyboard moreKeyboard;
    private Activity activity;
    public EditText currentEdittext;
    private Paint paint;
    private Drawable topBg;

    public SimpleKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVisibility(GONE);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void initKeyboard(Activity activity) {
        this.activity = activity;
        setKeyBoard2Normal();
        setPreviewEnabled(false);
        setOnKeyboardActionListener(new OnSimpleKeyboardActionListener(activity, this));

        initPaint();
    }

    private void initPaint() {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(sp2px(activity, 18));
        paint.setColor(ContextCompat.getColor(activity, R.color.key_text_color));
        topBg = new ColorDrawable(ContextCompat.getColor(activity, R.color.top_bg_color));
    }


    public void addEdittext(EditText editText) {
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard(v);
            }
        });
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        Keyboard.Key spaceKey = keys.get(keys.size() - 4);
        for (int i = 0; i < 6; i++) {
            Keyboard.Key key = keys.get(i);
            topBg.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
            topBg.draw(canvas);

            if (null != key.label) {
                canvas.drawText(key.label.toString(),
                        key.x + (key.width) / 2,
                        key.y + (key.height) / 2 + (paint.getTextSize() - paint.descent()) / 2,
                        paint);
            }
        }
        if (null != spaceKey.icon) {
            spaceKey.icon.setBounds(spaceKey.x, spaceKey.y + spaceKey.height / 4, spaceKey.x + spaceKey.width, spaceKey.y + spaceKey.height * 3 / 4);
            spaceKey.icon.draw(canvas);
        }
        paint.setTextSize(sp2px(activity, 15));
        canvas.drawText("空格",
                spaceKey.x + (spaceKey.width) / 2,
                spaceKey.y + (spaceKey.height) / 2 + (paint.getTextSize() - paint.descent()) / 2,
                paint);

    }

    public void removeEdittext(EditText editText) {
        editText.setOnTouchListener(null);
    }

    public void changeShift() {
        setShifted(!isShift());
    }

    public boolean isShift() {
        return getKeyboard().isShifted();
    }

    public void setKeyBoard2Normal() {
        if (null == normalKeyboard) {
            normalKeyboard = new Keyboard(activity, R.xml.keyboard_normal);
        }
        setKeyboard(normalKeyboard);
    }

    public void setKeyBoard2Nums() {
        if (null == numKeyboard) {
            numKeyboard = new Keyboard(activity, R.xml.keyboard_num);
        }
        setKeyboard(numKeyboard);
    }

    public void setKeyboard2More() {
        if (null == moreKeyboard) {
            moreKeyboard = new Keyboard(activity, R.xml.keyboard_more);
        }
        setKeyboard(moreKeyboard);
    }

    public void showKeyboard(View currentEditView) {
        this.currentEdittext = (EditText) currentEditView;
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
            Animation pullIn = AnimationUtils.loadAnimation(getContext(), R.anim.ime_pull_in_bottom);
            startAnimation(pullIn);
        }
    }

    public boolean hideKeyboard() {
        if (getVisibility() == VISIBLE) {
            Animation pushOut = AnimationUtils.loadAnimation(getContext(), R.anim.ime_push_out_bottom);
            pushOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            startAnimation(pushOut);
            return true;
        }
        return false;

    }

    private int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
