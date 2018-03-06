package kabukeisan.kirin3.jp.kabukeisan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public EditText mEditMeigara, mEditShutokuKabuka, mEditShutokuKabusuu;
    private TextView mTextShutokuKingaku, mTextSoneki, mTextYosouKingaku, mTextGensenChouShuu;

    private CustomNumberPicker mNumPickerKabuka1,mNumPickerKabuka2,mNumPickerKabuka3,mNumPickerKabuka4,mNumPickerKabuka5;

    public Integer YosouKabuka = 0;

    // 株価ナンバーピッカーの表示範囲最大倍率(*4)、最小倍率(/4)
    final Integer NUMBER_PICKER_KABUKA_RANGE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditMeigara = (EditText) findViewById(R.id.editMeigara);
        mEditShutokuKabuka = (EditText) findViewById(R.id.editShutokuKabuka);
        mEditShutokuKabusuu = (EditText) findViewById(R.id.editShutokuKabusuu);

        mEditMeigara.addTextChangedListener(new GenericTextWatcher(mEditMeigara));
        mEditShutokuKabuka.addTextChangedListener(new GenericTextWatcher(mEditShutokuKabuka));
        mEditShutokuKabusuu.addTextChangedListener(new GenericTextWatcher(mEditShutokuKabusuu));


        mNumPickerKabuka1 = (CustomNumberPicker) findViewById(R.id.numPickerKabuka1);
        mNumPickerKabuka2 = (CustomNumberPicker) findViewById(R.id.numPickerKabuka2);
        mNumPickerKabuka3 = (CustomNumberPicker) findViewById(R.id.numPickerKabuka3);
        mNumPickerKabuka4 = (CustomNumberPicker) findViewById(R.id.numPickerKabuka4);
        mNumPickerKabuka5 = (CustomNumberPicker) findViewById(R.id.numPickerKabuka5);

        mTextShutokuKingaku = (TextView) findViewById(R.id.textShutokuKingaku);
        mTextSoneki = (TextView) findViewById(R.id.textSoneki);
        mTextYosouKingaku = (TextView) findViewById(R.id.textYosouKingaku);
        mTextGensenChouShuu = (TextView) findViewById(R.id.textGensenChoshuu);

        Log.w("DEBUG_DATA", "aaaaaaaaaaaaa1 " );

        getChangeNumberPicker();
    }

    public void getChangeNumberPicker() {

        // ナンバーピッカーの変更を受け取るリスナー
        mNumPickerKabuka1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(1,oldVal,newVal);
            }
        });
        mNumPickerKabuka2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(2,oldVal,newVal);
            }
        });
        mNumPickerKabuka3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(3,oldVal,newVal);
            }
        });
        mNumPickerKabuka4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(4,oldVal,newVal);
            }
        });
        mNumPickerKabuka5.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(5,oldVal,newVal);
            }
        });
    }

    /***
     * ナンバーピッカーが動いた時の処理
     *
     * @param no
     * @param oldVal
     * @param newVal
     */
    public void exeChangeNumPicker(int no,int oldVal,int newVal ){

        // ・変更時に現在の予想株価を変更
        // 数字UP or 繰り上がり
        /*
        if(oldVal + 1 == newVal ||  oldVal == 9 && newVal == 0 ){
            if( no == 1 ) YosouKabuka += 1;
            else if( no == 2 ) YosouKabuka += 10;
            else if( no == 3 ) YosouKabuka += 100;
            else if( no == 4 ) YosouKabuka += 1000;
            else if( no == 5 ) YosouKabuka += 10000;
        }
        // 数字DOWN
        else{
            if( no == 1 ) YosouKabuka -= 1;
            else if( no == 2 ) YosouKabuka -= 10;
            else if( no == 3 ) YosouKabuka -= 100;
            else if( no == 4 ) YosouKabuka -= 1000;
            else if( no == 5 ) YosouKabuka -= 10000;
        }
        */


        int v1 = mNumPickerKabuka1.getValue();
        int v2 = mNumPickerKabuka2.getValue();
        int v3 = mNumPickerKabuka3.getValue();
        int v4 = mNumPickerKabuka4.getValue();
        int v5 = mNumPickerKabuka5.getValue();


        int bufYosouKabuka = v1 + v2 * 10 + v3 * 100 + v4 * 1000 + v5 * 10000;

        // 桁が繰り上がりしたら
        if( oldVal == 9 && newVal == 0 ) {
            if (no == 1 && bufYosouKabuka < 99990) bufYosouKabuka += 10;
            else if (no == 2 && bufYosouKabuka < 99900) bufYosouKabuka += 100;
            else if (no == 3 && bufYosouKabuka < 99000) bufYosouKabuka += 1000;
            else if (no == 4 && bufYosouKabuka < 90000) bufYosouKabuka += 10000;
            setNumPickerKabuka(bufYosouKabuka); // 繰り上がり、下がりを反映させる
          }
        // 桁が繰り下がりしたら
        else if( oldVal == 0 && newVal == 9 ) {
            if (no == 1 && bufYosouKabuka > 10 ) bufYosouKabuka -= 10;
            else if (no == 2 && bufYosouKabuka > 100) bufYosouKabuka -= 100;
            else if (no == 3 && bufYosouKabuka > 1000) bufYosouKabuka -= 1000;
            else if (no == 4 && bufYosouKabuka > 10000) bufYosouKabuka -= 10000;
            setNumPickerKabuka(bufYosouKabuka); // 繰り上がり、下がりを反映させる
        }

        YosouKabuka = bufYosouKabuka;


        Log.w( "DEBUG_DATA", "YosouKabuka = " + YosouKabuka );

        // 株数を入力していなければ表示しない
        if( mEditShutokuKabusuu.getText().toString() == null || mEditShutokuKabusuu.getText().toString().length() < 1 ) return;

        // ( ナンバーピッカー株価（newVal + newVal/NUMBER_PICKER_KABUKA_RANGE） - 取得株価 ) * 取得株数
        Integer shutokuKabuka = Integer.parseInt(mEditShutokuKabuka.getText().toString());
        Integer editShutokuKabusuu = Integer.parseInt(mEditShutokuKabusuu.getText().toString());
        Integer soneki = ( YosouKabuka - shutokuKabuka ) * editShutokuKabusuu;

        mTextSoneki.setText(String.valueOf(soneki));

        setYosouKingaku(YosouKabuka,mEditShutokuKabusuu.getText().toString());
    }

    /**
     * 取得株価、取得株数から取得金額を表示
     *
     * @param kabuka
     * @param kabusuu
     */
    public void setShutokuKingaku(String kabuka, String kabusuu){
        Long kingaku;

        if(kabuka.length() < 1 || kabusuu.length() < 1) return;

        kingaku = Long.parseLong(kabuka) * Long.parseLong(kabusuu);
        mTextShutokuKingaku.setText(String.valueOf(kingaku));
    }

    /**
     * 予想株価、取得株数から予想金額を表示
     *
     * @param yosouKabuka
     * @param kabusuu
     */
    public void setYosouKingaku(int yosouKabuka, String kabusuu){
        Long kingaku;
        Long gensenchoushuu;

        if(yosouKabuka < 1 || kabusuu.length() < 1) return;

        kingaku = yosouKabuka * Long.parseLong(kabusuu);
        gensenchoushuu = kingaku / 80;
        mTextYosouKingaku.setText(String.valueOf(kingaku));
        mTextGensenChouShuu.setText(String.valueOf(gensenchoushuu));
    }

    /**
     * 取得株価、取得金額から取得株数を表示
     *
     * @param kabuka
     * @param kingaku
     */
    public void setShutokuKabusuu(String kabuka, String kingaku){
        Integer kabusuu;

        if(kabuka.length() < 1 || kingaku.length() < 1) return;

        kabusuu = Integer.parseInt(kingaku) / Integer.parseInt(kabuka);

        mEditShutokuKabusuu.setText(String.valueOf(kabusuu));
    }

    /**
     * 予想株価数から、予想株価ナンバーピッカーを設定
     *
     * @param kabuka 設定株価
     */
    public void setNumPickerKabuka(int kabuka){

        int kabukaBuf;
        int num1 = 0,num2 = 0,num3 = 0,num4 = 0,num5 = 0;

        if(kabuka < 1) return;

        kabukaBuf = kabuka;

        if( kabukaBuf >= 10000 ){
            num5 = kabukaBuf / 10000;
            kabukaBuf = kabukaBuf - ( num5 * 10000 );
        }
        if( kabukaBuf >= 1000 ){
            num4 = kabukaBuf / 1000;
            kabukaBuf = kabukaBuf - ( num4 * 1000 );
        }
        if( kabukaBuf >= 100 ){
            num3 = kabukaBuf / 100;
            kabukaBuf = kabukaBuf - ( num3 * 100 );
        }
        if( kabukaBuf >= 10 ){
            num2 = kabukaBuf / 10;
            kabukaBuf = kabukaBuf - ( num2 * 10 );
        }
        if( kabukaBuf >= 1 ){
            num1 = kabukaBuf / 1;
            kabukaBuf = kabukaBuf - ( num1 * 1 );
        }

        Log.w( "DEBUG_DATA", "num5 = " + num5 );
        Log.w( "DEBUG_DATA", "num4 = " + num4 );
        Log.w( "DEBUG_DATA", "num3 = " + num3 );
        Log.w( "DEBUG_DATA", "num2 = " + num2 );
        Log.w( "DEBUG_DATA", "num1 = " + num1 );

        mNumPickerKabuka5.setValue(num5);
        mNumPickerKabuka4.setValue(num4);
        mNumPickerKabuka3.setValue(num3);
        mNumPickerKabuka2.setValue(num2);
        mNumPickerKabuka1.setValue(num1);

    }


    // EidtTextのイベントを取得
    public class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable editable) {

            switch (view.getId()) {
                case R.id.editMeigara:
                    Log.w( "DEBUG_DATA", "afterTextChanged editMeigara" );

                    break;
                case R.id.editShutokuKabuka:
                    Log.w( "DEBUG_DATA", "afterTextChanged editShutokuKabuka" );
                    YosouKabuka = Integer.parseInt(mEditShutokuKabuka.getText().toString());
                    setNumPickerKabuka(YosouKabuka);
                    break;
                case R.id.editShutokuKabusuu:
                    Log.w( "DEBUG_DATA", "afterTextChanged editShutokuKabusuu" );
                    setShutokuKingaku(mEditShutokuKabuka.getText().toString(),mEditShutokuKabusuu.getText().toString());
                    break;
            }
        }
    }
}
