package jp.kirin3.stockcalculation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.math.BigDecimal;

import static jp.kirin3.stockcalculation.CommonMng.DpToPx2;
import static jp.kirin3.stockcalculation.CommonMng.costString;
import static jp.kirin3.stockcalculation.CommonMng.showAlert;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;

    private static ConstraintLayout mClMain;
    private static LinearLayout mLlScroll;
    private static TextView mTextAdd,mTextReset1,mTextReset2,mTextHide,mTextManual;
    private static EditText mEditMeigara, mEditShutokuKabuKa, mEditShutokuKabuSuu;
    private static EditText mEditHitokabuHaitou1,mEditHitokabuHaitou2;
    private static TextView mTextShutokuKingaku;
    private static TextView mTextYosouSonekiP, mTextYosouKingakuP, mTextYosouGencyouP;
    private static TextView mTextYosouSonekiD, mTextYosouKingakuD, mTextYosouGencyouD;
    private static TextView mTextYosouSonekiT, mTextYosouKingakuT, mTextYosouGencyouT;
    private static CustomNumberPicker mNumPickerKabuKa1,mNumPickerKabuKa2,mNumPickerKabuKa3,mNumPickerKabuKa4,mNumPickerKabuKa5;
    private static CustomNumberPicker mNumPickerNenSuu;
    // キーボード表示制御のオブジェクト
    private InputMethodManager inputMethodManager;

    private FirebaseAnalytics mFirebaseAnalytics;


    // Firebase
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_main);

        mClMain = (ConstraintLayout) findViewById(R.id.layoutMain);
        mLlScroll = (LinearLayout) findViewById(R.id.llScroll);

        mTextAdd = (TextView) findViewById(R.id.textNew);
        mTextReset1 = (TextView) findViewById(R.id.textReset1);
        mTextReset2 = (TextView) findViewById(R.id.textReset2);
        mTextHide = (TextView) findViewById(R.id.textHide);
        mTextManual = (TextView) findViewById(R.id.textManual);

        mEditMeigara = (EditText) findViewById(R.id.editMeigara);
        mEditShutokuKabuKa = (EditText) findViewById(R.id.editShutokuKabuKa);
        mEditShutokuKabuSuu = (EditText) findViewById(R.id.editShutokuKabuSuu);
        mEditHitokabuHaitou1 = (EditText) findViewById(R.id.editHitokabuHaitou1);
        mEditHitokabuHaitou2 = (EditText) findViewById(R.id.editHitokabuHaitou2);

        mEditMeigara.addTextChangedListener(new GenericTextWatcher(mEditMeigara));
        mEditShutokuKabuKa.addTextChangedListener(new GenericTextWatcher(mEditShutokuKabuKa));
        mEditShutokuKabuSuu.addTextChangedListener(new GenericTextWatcher(mEditShutokuKabuSuu));
        mEditHitokabuHaitou1.addTextChangedListener(new GenericTextWatcher(mEditHitokabuHaitou1));
        mEditHitokabuHaitou2.addTextChangedListener(new GenericTextWatcher(mEditHitokabuHaitou2));

        mNumPickerKabuKa1 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa1);
        mNumPickerKabuKa2 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa2);
        mNumPickerKabuKa3 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa3);
        mNumPickerKabuKa4 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa4);
        mNumPickerKabuKa5 = (CustomNumberPicker) findViewById(R.id.numPickerKabuKa5);
        mNumPickerNenSuu = (CustomNumberPicker) findViewById(R.id.numPickerNensuu);

        mNumPickerKabuKa1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerKabuKa2.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerKabuKa3.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerKabuKa4.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerKabuKa5.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerNenSuu.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mTextShutokuKingaku = (TextView) findViewById(R.id.textShutokuKingaku);
        mTextYosouSonekiP = (TextView) findViewById(R.id.textYosouSonekiP);
        mTextYosouKingakuP = (TextView) findViewById(R.id.textYosouKingakuP);
        mTextYosouGencyouP = (TextView) findViewById(R.id.textYosouGencyouP);

        mTextYosouSonekiD = (TextView) findViewById(R.id.textYosouSonekiD);
        mTextYosouKingakuD = (TextView) findViewById(R.id.textYosouKingakuD);
        mTextYosouGencyouD = (TextView) findViewById(R.id.textYosouGencyouD);

        mTextYosouSonekiT = (TextView) findViewById(R.id.textYosouSonekiT);
        mTextYosouKingakuT = (TextView) findViewById(R.id.textYosouKingakuT);
        mTextYosouGencyouT = (TextView) findViewById(R.id.textYosouGencyouT);

        //キーボード表示を制御するためのオブジェクト
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        // ヘッダーに銘柄を設定
        HeaderSetText();

        // 新規データ作成ボタン
        mTextAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if( StockData.sSaveNum == StockData.SAVE_MAX_NUM ){
                    Toast.makeText(mContext, R.string.over_save_num,Toast.LENGTH_LONG).show();

                    return;
                }

                StockData.AddPreSaveNum();

                // 新しい銘柄を先に保存
                int no = StockData.GetPreUsingNum();
                String meigara = "銘柄" + (no);
                StockData.SetPreMeigara(no-1,meigara);
                changeMeigaraData(no-1);
                HeaderSetText();

                Toast.makeText(mContext,R.string.add_meigara,Toast.LENGTH_LONG).show();
            }
        });

        // 入力データリセットボタン
        mTextReset1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 予想株価を取得株価にリセット
                StockData.sYosouKabuKa = StockData.sShutokuKabuKa;
                setNumPickerKabuKa(StockData.sYosouKabuKa);
                setYosouAll();

                Toast.makeText(mContext,R.string.reset_yosou_kabuka,Toast.LENGTH_LONG).show();

                //キーボードを隠す
                inputMethodManager.hideSoftInputFromWindow(mClMain.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        mTextReset2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 予想年数を0にリセット
                StockData.sYosouNenSuu = 0;
                setNumPickerNenSuu(StockData.sYosouNenSuu);
                setYosouAll();

                Toast.makeText(mContext,R.string.reset_yosou_nensuu,Toast.LENGTH_LONG).show();
            }
        });

        mTextHide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //キーボードを隠す
                inputMethodManager.hideSoftInputFromWindow(mClMain.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


        mTextManual.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // インテントのインスタンス生成
                Intent intent = new Intent(MainActivity.this, ManualActivity.class);
                // マニュアル画面起動
                startActivity(intent);
            }
        });


        getChangeNumberPicker();

        ///////////// 〇Firebase
        // addMob設定
        MobileAds.initialize(this,getResources().getString(R.string.admob_app_id) );
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // Event
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        StockData.InitStockData(mContext,mEditMeigara,mEditShutokuKabuKa,mEditShutokuKabuSuu,mEditHitokabuHaitou1,mEditHitokabuHaitou2);
        setYosouAll();
    }

    /*****
     *  ヘッダー部分の表示
     *****/
    public void HeaderSetText(){
        float density = mContext.getResources().getDisplayMetrics().density;

        int height_px = DpToPx2(40,density);
        int padding_px = DpToPx2(5,density);
        int margin_px = DpToPx2(2,density);

        mLlScroll.removeAllViews();

        for(int i = 0; i < StockData.sSaveNum; i++) {

            String meigara = StockData.GetPreMeigara(i);
            Integer kabuKa = StockData.GetPreShutokuKabuKa(i);
            Integer kabuSuu = StockData.GetPreShutokuKabuSuu(i);

            TextView tv = new TextView(mContext);
            tv = new TextView(mContext);
            tv.setId(i);
            tv.setText(meigara);
            tv.setTextSize(20);
            tv.setBackgroundColor(getResources().getColor(R.color.pYellow));
            // 編集中のデータは目立つように
            if (i == StockData.GetPreSaveNo()){
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

            // 〇タブを切り替えた時の処理
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeMeigaraData(v.getId());
                }
            });
            mLlScroll.addView(tv);

            /*
            // 初期値設定
            if (i == StockData.sSaveNum) {
                mEditMeigara.setText(meigara);
                if(kabuKa != 0) mEditShutokuKabuKa.setText(kabuKa.toString());
                if(kabuSuu != 0) mEditShutokuKabuSuu.setText(kabuSuu.toString());
                StockData.sShutokuKabuKa = kabuKa;
                StockData.sShutokuKabuSuu = kabuSuu;
            }
            */
        }
    }

    // ユーザーが選択している銘柄データを切り替える
    public void changeMeigaraData(int no) {

        StockData.SetUsingNo(no);

        String meigara = StockData.GetPreMeigara(no);
        Integer shutokuKabuKa = StockData.GetPreShutokuKabuKa(no);
        Integer shutokuKabuSuu = StockData.GetPreShutokuKabuSuu(no);
        Integer buf_yosouKabuKa = StockData.GetPreYosouKabuKa(no);
        Integer hitokabuHaitou1 = StockData.GetPreHitokabuHaitou1(no);
        Integer hitokabuHaitou2 = StockData.GetPreHitokabuHaitou2(no);
        Integer yosouNenSuu = StockData.GetPreYosouNenSuu(no);

        mEditMeigara.setText(meigara);
        if(shutokuKabuKa != 0) mEditShutokuKabuKa.setText(shutokuKabuKa.toString()); // ◇1初回登録、変更時に予想株価、ピッカーも変更されてしまう。
        else mEditShutokuKabuKa.setText("");
        if(shutokuKabuSuu != 0) mEditShutokuKabuSuu.setText(shutokuKabuSuu.toString());
        else mEditShutokuKabuSuu.setText("");
        if(hitokabuHaitou1 != 0) mEditHitokabuHaitou1.setText(hitokabuHaitou1.toString());
        else mEditHitokabuHaitou1.setText("");
        if(hitokabuHaitou2 != 0) mEditHitokabuHaitou2.setText(hitokabuHaitou2.toString());
        else mEditHitokabuHaitou2.setText("");

        // ◇1そのため保存しておいた予想損益で上書き
        StockData.SetYosouKabuKa(buf_yosouKabuKa);
        setNumPickerKabuKa(buf_yosouKabuKa);

        StockData.SetYosouNenSuu(yosouNenSuu);
        setNumPickerNenSuu(yosouNenSuu);

        setYosouAll();

        HeaderSetText();

    }

    public void getChangeNumberPicker() {
        // ナンバーピッカーの変更を受け取るリスナー
        mNumPickerKabuKa1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPickerYosouKabuka(1,oldVal,newVal);
            }
        });
        mNumPickerKabuKa2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPickerYosouKabuka(2,oldVal,newVal);
            }
        });
        mNumPickerKabuKa3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPickerYosouKabuka(3,oldVal,newVal);
            }
        });
        mNumPickerKabuKa4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPickerYosouKabuka(4,oldVal,newVal);
            }
        });
        mNumPickerKabuKa5.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPickerYosouKabuka(5,oldVal,newVal);
            }
        });

        mNumPickerNenSuu.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                exeChangeNumPickerYosouNenSuu();
            }
        });
    }

    /***
     * 予想株価ナンバーピッカーが動いた時の処理
     *
     * @param no
     * @param oldVal
     * @param newVal
     */
    public void exeChangeNumPickerYosouKabuka(int no, int oldVal, int newVal ){

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

        StockData.SetYosouKabuKa(bufYosouKabuKa);

        setYosouAll();
    }


    /***
     * 年数ナンバーピッカーが動いた時の処理
     */
    public void exeChangeNumPickerYosouNenSuu( ){

        int YosouNenSuu = mNumPickerNenSuu.getValue();

        StockData.SetYosouNenSuu(YosouNenSuu);
        setYosouAll();
    }


    /**
     * 予想株価、取得株数から予想金額を表示
     *
     */
    public void setYosouAll(){

        long yosouSonekiP,yosouKingakuP,yosouGencyouP;
        long yosouSonekiD,yosouKingakuD,yosouGencyouD;
        long yosouSonekiT,yosouKingakuT,yosouGencyouT;
        BigDecimal bd;
        double buf;

        /*
        // 株価を入力していなければ表示しない
        if( ( mEditShutokuKabuKa.getText().toString() == null || mEditShutokuKabuKa.getText().toString().length() < 1 ) ||
                ( mEditShutokuKabuSuu.getText().toString() == null || mEditShutokuKabuSuu.getText().toString().length() < 1 ) ){
            mTextYosouSonekiP.setText("0");
            mTextYosouKingakuP.setText("0");
            mTextYosouGencyouP.setText("0");
            return;
        }
        */

        yosouSonekiP = ( (long)StockData.sYosouKabuKa - (long)StockData.sShutokuKabuKa ) * (long)StockData.sShutokuKabuSuu;
        yosouKingakuP = (long)StockData.sYosouKabuKa * (long)StockData.sShutokuKabuSuu;
        buf = (double)yosouSonekiP * StockData.ZEIRITSU / 100.0;
        bd = new BigDecimal(buf);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        yosouGencyouP = (long)bd.doubleValue();

        if(yosouGencyouP < 0) yosouGencyouP = 0;

        mTextYosouSonekiP.setText(costString(yosouSonekiP));
        if(yosouSonekiP == 0) mTextYosouSonekiP.setTextColor(getResources().getColor(R.color.gray));
        else if(yosouSonekiP >= 0) mTextYosouSonekiP.setTextColor(getResources().getColor(R.color.red));
        else mTextYosouSonekiP.setTextColor(getResources().getColor(R.color.blue));
        mTextYosouKingakuP.setText(costString(yosouKingakuP));
        mTextYosouGencyouP.setText(costString(yosouGencyouP));

        yosouSonekiD = (long)StockData.sHitokabuHaitou1 * (long)StockData.sYosouNenSuu *(long)StockData.sShutokuKabuSuu;
        // 小数点以下を計算
        if(StockData.sHitokabuHaitou2 != 0){
            double waru;
            if(StockData.sHitokabuHaitou2 < 10){
                waru = 10.0;
            }
            else{
                waru = 100.0;
            }

            buf = (double)StockData.sHitokabuHaitou2  * (double)StockData.sYosouNenSuu * (double)StockData.sShutokuKabuSuu / waru;
            bd = new BigDecimal(buf);
            bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);

            yosouSonekiD += bd.doubleValue();
        }

        yosouKingakuD = StockData.sShutokuKingaku + yosouSonekiD;
        buf = (double)yosouSonekiD * StockData.ZEIRITSU / 100.0;
        bd = new BigDecimal(buf);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        yosouGencyouD = (long)bd.doubleValue();

        mTextYosouSonekiD.setText(costString(yosouSonekiD));
        if(yosouSonekiP == 0) mTextYosouSonekiD.setTextColor(getResources().getColor(R.color.gray));
        else mTextYosouSonekiD.setTextColor(getResources().getColor(R.color.red));
        mTextYosouKingakuD.setText(costString(yosouKingakuD));
        mTextYosouGencyouD.setText(costString(yosouGencyouD));

        yosouSonekiT = yosouSonekiP + yosouSonekiD;
        yosouKingakuT = StockData.sShutokuKingaku + yosouSonekiT;

        buf = (double)yosouSonekiT * StockData.ZEIRITSU / 100.0;
        bd = new BigDecimal(buf);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        yosouGencyouT = (long)bd.doubleValue();


        if(yosouGencyouT < 0) yosouGencyouT = 0;

        mTextYosouSonekiT.setText(costString(yosouSonekiT));
        if(yosouSonekiT == 0) mTextYosouSonekiT.setTextColor(getResources().getColor(R.color.gray));
        else if(yosouSonekiT >= 0) mTextYosouSonekiT.setTextColor(getResources().getColor(R.color.red));
        else mTextYosouSonekiT.setTextColor(getResources().getColor(R.color.blue));
        mTextYosouKingakuT.setText(costString(yosouKingakuT));
        mTextYosouGencyouT.setText(costString(yosouGencyouT));

    }

    /**
     * 取得株価、取得株数から取得金額を表示
     */
    public void setShutokuKingaku(){

        if( StockData.sShutokuKabuKa == null || StockData.sShutokuKabuSuu == null ) return;
        StockData.sShutokuKingaku = (long)StockData.sShutokuKabuKa * (long)StockData.sShutokuKabuSuu;
        mTextShutokuKingaku.setText(costString(StockData.sShutokuKingaku));
    }


    /**
     * 予想株価から、予想株価ナンバーピッカーを初期設定
     *
     * @param KabuKa 設定株価
     */
    public static void setNumPickerKabuKa(int KabuKa){

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

        mNumPickerKabuKa5.setValue(num5);
        mNumPickerKabuKa4.setValue(num4);
        mNumPickerKabuKa3.setValue(num3);
        mNumPickerKabuKa2.setValue(num2);
        mNumPickerKabuKa1.setValue(num1);

    }

    /**
     * 予想年数から、予想年数ナンバーピッカーを初期設定
     *
     * @param NenSuu 予想年数
     */
    public static void setNumPickerNenSuu(int NenSuu){
        mNumPickerNenSuu.setValue(NenSuu);
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
                    // 1文字入力ごとにデータを保管
                    StockData.SetMeigara(mEditMeigara.getText().toString());
                    HeaderSetText();

                    break;
                case R.id.editShutokuKabuKa:
                    if( mEditShutokuKabuKa.getText().toString() !=null && mEditShutokuKabuKa.getText().toString().length() > 0 ) {
                        int shutokuKabuka = Integer.parseInt(mEditShutokuKabuKa.getText().toString());

                        if(shutokuKabuka > 100000){
                            shutokuKabuka = 99999;
                            mEditShutokuKabuKa.setText(String.valueOf(shutokuKabuka));
                            // 取得株価のリアルタイム保存
                            StockData.SetShutokuKabuKa(shutokuKabuka);
                            showAlert(getString(R.string.over_flow),getString(R.string.over_kabuka),mContext );
                            break;
                        }

                        // 取得株価のリアルタイム保存
                        StockData.SetShutokuKabuKa(shutokuKabuka);

                        // 予想初期値セット
                        StockData.SetYosouKabuKa(shutokuKabuka);
                        setNumPickerKabuKa(StockData.sYosouKabuKa);
                    }
                    else{

                        StockData.SetShutokuKabuKa(0);
                        StockData.SetYosouKabuKa(0);
                        setYosouAll();
                    }

                    setShutokuKingaku();
                    break;
                case R.id.editShutokuKabuSuu:
                    if( mEditShutokuKabuSuu.getText().toString() !=null && mEditShutokuKabuSuu.getText().toString().length() > 0 ) {
                        int shutokuKabusuu = Integer.parseInt(mEditShutokuKabuSuu.getText().toString());

                        if(shutokuKabusuu > 10000000){
                            shutokuKabusuu = 9999999;
                            mEditShutokuKabuSuu.setText(String.valueOf(shutokuKabusuu));
                            showAlert(getString(R.string.over_flow),getString(R.string.over_kabusuu),mContext);
                            break;
                        }

                        // 取得株数のリアルタイム保存
                        StockData.SetShutokuKabuSuu(Integer.parseInt(mEditShutokuKabuSuu.getText().toString()));

                    }
                    else StockData.sShutokuKabuSuu = 0;
                    setShutokuKingaku();
                    setYosouAll();
                    break;

                case R.id.editHitokabuHaitou1:
                    if( mEditHitokabuHaitou1.getText().toString() !=null && mEditHitokabuHaitou1.getText().toString().length() > 0 ) {
                        int hitokabuHaitou = Integer.parseInt(mEditHitokabuHaitou1.getText().toString());

                        if(hitokabuHaitou > 10000){
                            hitokabuHaitou = 9999;
                            mEditHitokabuHaitou1.setText(String.valueOf(hitokabuHaitou));
                            showAlert(getString(R.string.over_flow),getString(R.string.over_haitou1),mContext );
                            break;
                        }

                        // 取得株価のリアルタイム保存
                        StockData.SetHitokabuHaitou1(hitokabuHaitou);
                        setYosouAll();
                    }
                    else{

                        StockData.SetHitokabuHaitou1(0);
                        setYosouAll();
                    }

                    break;
                case R.id.editHitokabuHaitou2:
                    if( mEditHitokabuHaitou2.getText().toString() !=null && mEditHitokabuHaitou2.getText().toString().length() > 0 ) {
                        int hitokabuHaitou = Integer.parseInt(mEditHitokabuHaitou2.getText().toString());

                        if(hitokabuHaitou > 100){
                            hitokabuHaitou = 99;
                            mEditHitokabuHaitou2.setText(String.valueOf(hitokabuHaitou));
                            showAlert(getString(R.string.over_flow),getString(R.string.over_haitou2),mContext );
                            break;
                        }

                        // 取得株価のリアルタイム保存
                        StockData.SetHitokabuHaitou2(hitokabuHaitou);
                        setYosouAll();
                    }
                    else{

                        StockData.SetHitokabuHaitou2(0);
                        setYosouAll();
                    }

                    break;

            }
        }
    }
}
