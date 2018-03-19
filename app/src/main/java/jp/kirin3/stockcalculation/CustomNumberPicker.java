package jp.kirin3.stockcalculation;

/**
 * Created by etisu on 2018/03/04.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;


/**
 * Created by massa on 2016/11/15.
 */

public class CustomNumberPicker extends NumberPicker {

    // 表示用の値のリスト
    private String [] mValueSet;

    // 最大値・最小値
    private int mMaxValue;
    private int mMinValue;

    // 刻み幅
    private int mStep;

    public CustomNumberPicker(Context context) {
        super(context);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        if (this.isInEditMode()) {
            return;
        }

        // レイアウトで指定された値の取得
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomNumberPicker, 0, 0);
        try {
            mStep = typedArray.getInteger(R.styleable.CustomNumberPicker_step, 1);
            mMinValue = typedArray.getInteger(R.styleable.CustomNumberPicker_min, 0);
            mMaxValue = typedArray.getInteger(R.styleable.CustomNumberPicker_max, mMinValue);
        }
        finally {
            typedArray.recycle();
        }

        setValues();
    }

    private void setValues() {
        int value_num = 0;

        // minとmaxが逆だったら入れ替える
        if (mMinValue > mMaxValue) {
            int temp = mMinValue;
            mMinValue = mMaxValue;
            mMaxValue = temp;
        }

        // min ～ maxに値がいくつ入るか算出
        value_num = (mMaxValue - mMinValue + mStep - 1) / mStep;

        mValueSet = new String[value_num + 1];

        // 表示値のリストを作る
        for (int i = 0; i < value_num; i++) {
            mValueSet[i] = String.valueOf(mMinValue + mStep * i);
        }
        mValueSet[value_num] = String.valueOf(mMaxValue);

        // 表示値の数が変わるので、一旦初期化
        super.setMaxValue(0);

        setDisplayedValues(mValueSet);

        // 本来のValueはindexとして使用する
        super.setMinValue(0);
        super.setMaxValue(value_num);

        // 一旦最大値を設定
        super.setValue(value_num);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getClass().equals(AppCompatEditText.class) == true) {
                // Text部分のサイズを計算
                int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
                view.measure(expandSpec, expandSpec);

                // Text部分の幅を設定
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = view.getMeasuredWidth();
            }
        }

        // 初期表示のindex
        super.setValue(0);
    }

    @Override
    public int getValue() {
        // setDisplayedValuesでも使用しているので、ここに変更は加えない
        return super.getValue();
    }

    @Override
    public void setValue(int value) {
        // スクロール時に使用されているので、ここに変更は加えない
        super.setValue(value);
    }

    public int getDisplayedValue() {
        // 選択されている値を取得するときは、こちらを呼び出す
        int pos = super.getValue();
        return Integer.parseInt(mValueSet[pos]);
    }

    /*
    @Override
    public void setDisplayedValue(int value) {
        // プログラム中から表示値の設定をするときは、こちらを呼び出す
        int pos = 0;
        while (value > Integer.parseInt(mValueSet[pos])) {
            pos++;
        }
        super.setValue(pos);
    }
    */

    @Override
    public int getMinValue() {
        return mMinValue;
    }

    @Override
    public void setMinValue(int minValue) {
        mMinValue = minValue;
        setValues();
    }

    @Override
    public int getMaxValue() {
        return mMaxValue;
    }

    @Override
    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
        setValues();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    private void updateView(View child) {
        if (child instanceof EditText) {
            ((EditText) child).setTextSize(TypedValue.COMPLEX_UNIT_SP, 30.0f);
            ((EditText) child).setTextColor(Color.BLACK);
        }
    }

    public int getStep(){
        return mStep;
    }
}