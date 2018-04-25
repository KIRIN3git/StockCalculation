package jp.kirin3.stockcalculation;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import static jp.kirin3.stockcalculation.CommonMng.DpToPx2;
import static jp.kirin3.stockcalculation.CommonMng.costString;
import static jp.kirin3.stockcalculation.CommonMng.showAlert;

public class MainActivity extends AppCompatActivity {




    private static Context mContext;

    private LinearLayout mLlScroll;
    private TextView mTextAdd,mTextReset;
    private EditText mEditMeigara, mEditShutokuKabuKa, mEditShutokuKabuSuu;
    private TextView mTextShutokuKingaku, mTextYosouSoneki, mTextYosouKingaku, mTextGensenChouShuu;
    private CustomNumberPicker mNumPickerKabuKa1,mNumPickerKabuKa2,mNumPickerKabuKa3,mNumPickerKabuKa4,mNumPickerKabuKa5;

    private Integer mShutokuKabuKa,mShutokuKabuSuu,mYosouKabuKa;
    private Long mShutokuKingaku,mYosouSoneki,mYosouKingaku,mGensenChoshuu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_main);

        mLlScroll = (LinearLayout) findViewById(R.id.llScroll);

        mTextAdd = (TextView) findViewById(R.id.textNew);
        mTextReset = (TextView) findViewById(R.id.textReset);

        mEditMeigara = (EditText) findViewById(R.id.editMeigara);
        mEditShutokuKabuKa = (EditText) findViewById(R.id.editShutokuKabuKa);
        mEditShutokuKabuSuu = (EditText) findViewById(R.id.editShutokuKabuSuu);

        mEditMeigara.addTextChangedListener(new GenericTextWatcher(mEditMeigara));
        mEditShutokuKabuKa.addTextChangedListener(new GenericTextWatcher(mEditShutokuKabuKa));
        mEditShutokuKabuSuu.addTextChangedListener(new GenericTextWatcher(mEditShutokuKabuSuu));

        mNumPickerKabuKa1 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa1);
        mNumPickerKabuKa2 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa2);
        mNumPickerKabuKa3 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa3);
        mNumPickerKabuKa4 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa4);
        mNumPickerKabuKa5 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa5);

        mNumPickerKabuKa1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerKabuKa2.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerKabuKa3.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerKabuKa4.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerKabuKa5.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mTextShutokuKingaku = (TextView) findViewById(R.id.textShutokuKingaku);
        mTextYosouSoneki = (TextView) findViewById(R.id.textYosouSonekiP);
        mTextYosouKingaku = (TextView) findViewById(R.id.textYosouKingakuP);
        mTextGensenChouShuu = (TextView) findViewById(R.id.textGensenChoshuuP);

        StockData.InitStockData(mContext);

        // 最初に一人分を表示
        if( StockData.sUserMeigaraNum == 0 ) {
            StockData.AddPreUserMeigaraNum();
            String meigara = "銘柄1";
            StockData.SetPreMeigara(0, meigara);
            mEditMeigara.setText(meigara);
        }

        // ヘッダーに銘柄を設定
        HeaderSetText();

        // 新規データ作成ボタン
        mTextAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                StockData.AddPreUserMeigaraNum();

                // 新しい銘柄を先に保存
                int no = StockData.GetPreUserMeigaraNum();
                String meigara = "銘柄" + (no);
                StockData.SetPreMeigara(no-1,meigara);


                Log.w( "DEBUG_DATA", "aaa set no "+ no);
                Log.w( "DEBUG_DATA", "aaa set meigara "+ meigara);

                HeaderSetText();
                Toast.makeText(mContext,"銘柄を追加しました",Toast.LENGTH_LONG).show();
            }
        });


        // 入力データリセットボタン
        mTextReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 予想株価を取得株価にリセット
                mYosouKabuKa = mShutokuKabuKa;
                setNumPickerKabuKa(mYosouKabuKa);
                setYosouAll();

                Toast.makeText(mContext,"ピッカーをリセットしました",Toast.LENGTH_LONG).show();
            }
        });

        getChangeNumberPicker();
    }


    public void HeaderSetText(){
        float density = mContext.getResources().getDisplayMetrics().density;

        int height_px = DpToPx2(40,density);
        int padding_px = DpToPx2(5,density);
        int margin_px = DpToPx2(2,density);

        mLlScroll.removeAllViews();

        for( int i = 0; i < StockData.sUserMeigaraNum; i++) {

            String meigara = StockData.GetPreMeigara(i);
            Integer kabuKa = StockData.GetPreKabuKa(i);
            Integer kabuSuu = StockData.GetPreKabuSuu(i);

            Log.w( "DEBUG_DATA", "aaa get i "+ i);
            Log.w( "DEBUG_DATA", "aaa get meigara "+ meigara);

            TextView tv = new TextView(mContext);
            tv = new TextView(mContext);
            tv.setId(i);
            tv.setText(meigara);
            tv.setTextSize(20);
            tv.setBackgroundColor(getResources().getColor(R.color.pYellow));
            if (i == StockData.GetPreNowMeigaraNo()){
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                tv.setTextColor(getResources().getColor(R.color.darkGray));
            }
            tv.setGravity(Gravity.CENTER);
//            tv.setHeight(height_px);
            tv.setPadding(padding_px, padding_px, padding_px, padding_px);
            // レイアウトとマージンの指定
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height_px);
            layoutParams.setMargins(0, margin_px, margin_px, margin_px);
            tv.setLayoutParams(layoutParams);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key;

                    StockData.SetPreNowMeigaraNo(v.getId());

                    int no = v.getId();

                    String meigara = StockData.GetPreMeigara(no);
                    Integer kabuKa = StockData.GetPreKabuKa(no);
                    Integer kabuSuu = StockData.GetPreKabuSuu(no);

                    Log.w( "DEBUG_DATA", "v.getId() = " + v.getId());
                    Log.w( "DEBUG_DATA", "meigara = " + meigara);

                    mEditMeigara.setText(meigara);
                    if(kabuKa != 0) mEditShutokuKabuKa.setText(kabuKa.toString());
                    else mEditShutokuKabuKa.setText("");
                    if(kabuSuu != 0) mEditShutokuKabuSuu.setText(kabuSuu.toString());
                    else mEditShutokuKabuSuu.setText("");

                    HeaderSetText();
                }
            });
            mLlScroll.addView(tv);

            // 初期値設定
            if (i == StockData.sUserMeigaraNum) {

                mEditMeigara.setText(meigara);
                if(kabuKa != 0) mEditShutokuKabuKa.setText(kabuKa.toString());
                if(kabuSuu != 0) mEditShutokuKabuSuu.setText(kabuSuu.toString());
                mShutokuKabuKa = kabuKa;
                mShutokuKabuSuu = kabuSuu;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mShutokuKabuKa = 0;
        mShutokuKabuSuu = 0;
        mShutokuKingaku = 0L;
        mYosouKabuKa = 0;
        mYosouSoneki = 0L;
        mYosouKingaku = 0L;
        mGensenChoshuu = 0L;
    }

    public void getChangeNumberPicker() {

        // ナンバーピッカーの変更を受け取るリスナー
        mNumPickerKabuKa1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(1,oldVal,newVal);
            }
        });
        mNumPickerKabuKa2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(2,oldVal,newVal);
            }
        });
        mNumPickerKabuKa3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(3,oldVal,newVal);
            }
        });
        mNumPickerKabuKa4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPicker(4,oldVal,newVal);
            }
        });
        mNumPickerKabuKa5.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
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


        int v1 = mNumPickerKabuKa1.getValue();
        int v2 = mNumPickerKabuKa2.getValue();
        int v3 = mNumPickerKabuKa3.getValue();
        int v4 = mNumPickerKabuKa4.getValue();
        int v5 = mNumPickerKabuKa5.getValue();


        int bufYosouKabuKa = v1 + v2 * 10 + v3 * 100 + v4 * 1000 + v5 * 10000;

        // 桁が繰り上がりしたら
        if( oldVal == 9 && newVal == 0 ) {
            if (no == 1 && bufYosouKabuKa < 99990) bufYosouKabuKa += 10;
            else if (no == 2 && bufYosouKabuKa < 99900) bufYosouKabuKa += 100;
            else if (no == 3 && bufYosouKabuKa < 99000) bufYosouKabuKa += 1000;
            else if (no == 4 && bufYosouKabuKa < 90000) bufYosouKabuKa += 10000;
            setNumPickerKabuKa(bufYosouKabuKa); // 繰り上がり、下がりを反映させる
          }
        // 桁が繰り下がりしたら
        else if( oldVal == 0 && newVal == 9 ) {
            if (no == 1 && bufYosouKabuKa > 10 ) bufYosouKabuKa -= 10;
            else if (no == 2 && bufYosouKabuKa > 100) bufYosouKabuKa -= 100;
            else if (no == 3 && bufYosouKabuKa > 1000) bufYosouKabuKa -= 1000;
            else if (no == 4 && bufYosouKabuKa > 10000) bufYosouKabuKa -= 10000;
            setNumPickerKabuKa(bufYosouKabuKa); // 繰り上がり、下がりを反映させる
        }

        mYosouKabuKa = bufYosouKabuKa;

        setYosouAll();
    }


    /**
     * 予想株価、取得株数から予想金額を表示
     *
     */
    public void setYosouAll(){

        Log.w( "DEBUG_DATA", "setYosouAll" );

        long shutokuKabuKa,shutokuKabuSuu,yosouSoneki;
        long gensenChoushuu,yosouKingaku;

        /*
        // 株価を入力していなければ表示しない
        if( ( mEditShutokuKabuKa.getText().toString() == null || mEditShutokuKabuKa.getText().toString().length() < 1 ) ||
                ( mEditShutokuKabuSuu.getText().toString() == null || mEditShutokuKabuSuu.getText().toString().length() < 1 ) ){
            mTextYosouSoneki.setText("0");
            mTextYosouKingaku.setText("0");
            mTextGensenChouShuu.setText("0");
            return;
        }
        */

        yosouSoneki = ( (long)mYosouKabuKa - (long)mShutokuKabuKa ) * (long)mShutokuKabuSuu;
        yosouKingaku = (long)mYosouKabuKa * (long)mShutokuKabuSuu;
        gensenChoushuu = yosouSoneki * 20 / 100;
        if(gensenChoushuu < 0) gensenChoushuu = 0;

        mTextYosouSoneki.setText(costString(yosouSoneki));
        if(yosouSoneki == 0) mTextYosouSoneki.setTextColor(getResources().getColor(R.color.gray));
        else if(yosouSoneki >= 0) mTextYosouSoneki.setTextColor(getResources().getColor(R.color.red));
        else mTextYosouSoneki.setTextColor(getResources().getColor(R.color.blue));
        mTextYosouKingaku.setText(costString(yosouKingaku));
        mTextGensenChouShuu.setText(costString(gensenChoushuu));
    }

    /**
     * 取得株価、取得株数から取得金額を表示
     */
    public void setShutokuKingaku(){
        long kingaku;

        if( mShutokuKabuKa == null || mShutokuKabuSuu == null ) return;
        Log.w( "DEBUG_DATA", "mShutokuKabuKa" + mShutokuKabuKa);
        Log.w( "DEBUG_DATA", "mShutokuKabuSuu" + mShutokuKabuSuu);
        kingaku = (long)mShutokuKabuKa * (long)mShutokuKabuSuu;
        mTextShutokuKingaku.setText(costString(kingaku));
    }


    /**
     * 予想株価数から、予想株価ナンバーピッカーを初期設定
     *
     * @param KabuKa 設定株価
     */
    public void setNumPickerKabuKa(int KabuKa){

        int KabuKaBuf;
        int num1 = 0,num2 = 0,num3 = 0,num4 = 0,num5 = 0;

        if(KabuKa < 1){
            mNumPickerKabuKa5.setValue(0);
            mNumPickerKabuKa4.setValue(0);
            mNumPickerKabuKa3.setValue(0);
            mNumPickerKabuKa2.setValue(0);
            mNumPickerKabuKa1.setValue(0);

            return;
        }

        KabuKaBuf = KabuKa;

        if( KabuKaBuf >= 10000 ){
            num5 = KabuKaBuf / 10000;
            KabuKaBuf = KabuKaBuf - ( num5 * 10000 );
        }
        if( KabuKaBuf >= 1000 ){
            num4 = KabuKaBuf / 1000;
            KabuKaBuf = KabuKaBuf - ( num4 * 1000 );
        }
        if( KabuKaBuf >= 100 ){
            num3 = KabuKaBuf / 100;
            KabuKaBuf = KabuKaBuf - ( num3 * 100 );
        }
        if( KabuKaBuf >= 10 ){
            num2 = KabuKaBuf / 10;
            KabuKaBuf = KabuKaBuf - ( num2 * 10 );
        }
        if( KabuKaBuf >= 1 ){
            num1 = KabuKaBuf / 1;
            KabuKaBuf = KabuKaBuf - ( num1 * 1 );
        }

        Log.w( "DEBUG_DATA", "num5 = " + num5 );
        Log.w( "DEBUG_DATA", "num4 = " + num4 );
        Log.w( "DEBUG_DATA", "num3 = " + num3 );
        Log.w( "DEBUG_DATA", "num2 = " + num2 );
        Log.w( "DEBUG_DATA", "num1 = " + num1 );

        mNumPickerKabuKa5.setValue(num5);
        mNumPickerKabuKa4.setValue(num4);
        mNumPickerKabuKa3.setValue(num3);
        mNumPickerKabuKa2.setValue(num2);
        mNumPickerKabuKa1.setValue(num1);

    }


    // EidtTextのイベントを取得
    public class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }


        // EditTextが1文字編集されるごとに呼び出される
        public void afterTextChanged(Editable editable) {

            switch (view.getId()) {
                case R.id.editMeigara:
                    Log.w( "DEBUG_DATA", "afterTextChanged editMeigara" );
                    // 1文字入力ごとにデータを保管
                    StockData.SetMeigara(mEditMeigara.getText().toString());
                    HeaderSetText();

                    break;
                case R.id.editShutokuKabuKa:
                    Log.w( "DEBUG_DATA", "afterTextChanged editShutokuKabuKa" );
                    if( mEditShutokuKabuKa.getText().toString() !=null && mEditShutokuKabuKa.getText().toString().length() > 0 ) {
                        mShutokuKabuKa = Integer.parseInt(mEditShutokuKabuKa.getText().toString());

                        if(mShutokuKabuKa > 100000){
                            mShutokuKabuKa = 99999;
                            mEditShutokuKabuKa.setText(mShutokuKabuKa.toString());
                            showAlert("オーバーフロー","株価は99999まで設定可能です",mContext );
                            break;
                        }

                        // 予想初期値セット
                        mYosouKabuKa = Integer.parseInt(mEditShutokuKabuKa.getText().toString());
                        // 取得株価のリアルタイム保存
                        StockData.SetKabuKa(mYosouKabuKa);
                        setNumPickerKabuKa(mYosouKabuKa);
                    }
                    else{
                        mShutokuKabuKa = 0;
                        mYosouKabuKa = 0;
                        setYosouAll();
                    }

                    setShutokuKingaku();
                    break;
                case R.id.editShutokuKabuSuu:
                    Log.w( "DEBUG_DATA", "afterTextChanged editShutokuKabuSuu" );
                    if( mEditShutokuKabuSuu.getText().toString() !=null && mEditShutokuKabuSuu.getText().toString().length() > 0 ) {
                        mShutokuKabuSuu = Integer.parseInt(mEditShutokuKabuSuu.getText().toString());

                        Log.w( "DEBUG_DATA", "AERAAERA1 mShutokuKabuSuu" + mShutokuKabuSuu);
                        if(mShutokuKabuSuu > 100000000){
                            mShutokuKabuSuu = 99999999;
                            Log.w( "DEBUG_DATA", "AERAAERA2");
                            mEditShutokuKabuSuu.setText(mShutokuKabuSuu.toString());
                            showAlert("オーバーフロー","株数は999999999まで設定可能です",mContext);
                            break;
                        }

                        mShutokuKabuSuu = Integer.parseInt(mEditShutokuKabuSuu.getText().toString());
                        // 取得株数のリアルタイム保存
                        StockData.SetKabuSuu(mShutokuKabuSuu);

                    }
                    else mShutokuKabuSuu = 0;
                    setShutokuKingaku();
                    setYosouAll();
                    break;
            }
        }
    }
}
