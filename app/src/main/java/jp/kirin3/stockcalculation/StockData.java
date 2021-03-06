package jp.kirin3.stockcalculation;

/**
 * Created by etisu on 2018/04/22.
 */

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

/****
 * ユーザーの保存データを管理
 * 利用時はInitStockData()を呼び出す必要がある
 *
 * データは基本全てリアルタイムでプリファランスに保存されていく
 */
public class StockData {


    // 編集中銘柄
    public static String sMeigara;

    // 編集中取得株価
    public static Integer sShutokuKabuKa;

    // 編集中取得株数
    public static Integer sShutokuKabuSuu;

    // 編集中予想株価
    public static Integer sYosouKabuKa;

    // 編集中一株配当 整数部分
    public static Integer sHitokabuHaitou1;

    // 編集中一株配当 小数点部分
    public static Integer sHitokabuHaitou2;

    // 編集中予想年数
    public static Integer sYosouNenSuu;

    // 表示中取得金額
    public static Long sShutokuKingaku;

    // 最大銘柄登録数
    final static int SAVE_MAX_NUM = 20;

    // 税率
    final static double ZEIRITSU = 20.315;

    // ユーザーの登録銘柄数
    public static Integer sSaveNum = 0;
    // 編集中銘柄番号
    private static Integer sUsingNo = 0;

    private static Context mContext;


    // ・プリファランス名
    // 銘柄 + i
    final static String PRE_MEIGARA = "PRE_MEIGARA_";
    // 取得株価 + i
    final static String PRE_SHUTOKU_KABU_KA = "PRE_SHUTOKU_KABU_KA_";
    // 取得株数 + i
    final static String PRE_SHUTOKU_KABU_SUU = "PRE_SHUTOKU_KABU_SUU_";
    // 予想株価 + i
    final static String PRE_YOSOU_KABU_KA = "PRE_YOSOU_KABU_KA_";

    // 一株配当 整数部分 + i
    final static String PRE_HITOKABU_HAITOU1 = "PRE_HITOKABU_HAITOU1_";
    // 一株配当 小数点部分 + i
    final static String PRE_HITOKABU_HAITOU2 = "PRE_HITOKABU_HAITOU2_";
    // 予想年数 + i
    final static String PRE_YOSOU_NEN_SUU = "PRE_YOSOU_NEN_SUU_";

    // ユーザーの銘柄登録数
    final static String PRE_SAVE_NUM = "PRE_SAVE_NUM";
    // ユーザーの編集中銘柄番号
    final static String PRE_USING_NO = "PRE_USING_NO";




    public static void InitStockData(Context context, EditText EditMeigara,EditText EditShutokuKabuKa,EditText EditShutokuKabuSuu,EditText EditHitokabuHaitou1,EditText EditHitokabuHaitou2){
        mContext = context;

        sSaveNum = GetPreUsingNum(); //　登録数
        sUsingNo = GetPreSaveNo();

        // 未登録なら最初に一人分を表示
        if( sSaveNum == 0 ) {
            AddPreSaveNum();
            String meigara = "銘柄1";
            SetPreMeigara(0, meigara);
            EditMeigara.setText(meigara);
        }

        sMeigara = StockData.GetPreMeigara(sUsingNo);
        sShutokuKabuKa= StockData.GetPreShutokuKabuKa(sUsingNo);
        sShutokuKabuSuu = StockData.GetPreShutokuKabuSuu(sUsingNo);
        Integer buf_sYosouKabuKa = StockData.GetPreYosouKabuKa(sUsingNo);
        sShutokuKingaku = (long)sShutokuKabuKa * (long)sShutokuKabuSuu;

        sHitokabuHaitou1 = StockData.GetPreHitokabuHaitou1(sUsingNo);
        sHitokabuHaitou2 = StockData.GetPreHitokabuHaitou2(sUsingNo);
        sYosouNenSuu = StockData.GetPreYosouNenSuu(sUsingNo);

        EditMeigara.setText(sMeigara);
        if(sShutokuKabuKa != 0) EditShutokuKabuKa.setText(sShutokuKabuKa.toString()); // ◇2初回登録、変更時に予想株価、ピッカーも変更されてしまう。
        else EditShutokuKabuKa.setText("");
        if(sShutokuKabuSuu != 0) EditShutokuKabuSuu.setText(sShutokuKabuSuu.toString());
        else EditShutokuKabuSuu.setText("");
        if(sHitokabuHaitou1 != 0) EditHitokabuHaitou1.setText(sHitokabuHaitou1.toString());
        else EditHitokabuHaitou1.setText("");
        if(sHitokabuHaitou2 != 0) EditHitokabuHaitou2.setText(sHitokabuHaitou2.toString());
        else EditHitokabuHaitou2.setText("");

        // ◇2そのため保存しておいた予想損益で上書き
        if(buf_sYosouKabuKa != 0) {
            SetYosouKabuKa(buf_sYosouKabuKa);
            MainActivity.setNumPickerKabuKa(buf_sYosouKabuKa);
        }

        if( sYosouNenSuu != 0 ){
            MainActivity.setNumPickerNenSuu(sYosouNenSuu);
        }
    }


    /*****
     * ユーザーの登録銘柄数を取得
     */
    public static int GetPreUsingNum(){
        int ret = CommonMng.getPrefInt(mContext, PRE_SAVE_NUM);
        return ret;
    }

    /*****
     * ユーザーの編集中銘柄番号を取得
     */
    public static int GetPreSaveNo(){
        int ret = CommonMng.getPrefInt(mContext, PRE_USING_NO);
        return ret;
    }

    /*****
     * 番号を指定して銘柄を取得
     * @param no 銘柄番号(0～)
     */
    public static String GetPreMeigara(int no){
        String key = PRE_MEIGARA + no;
        String meigara = CommonMng.getPrefData(mContext,key);
        return meigara;
    }

    /*****
     * 番号を指定して取得株価を取得
     * @param no 銘柄番号(0～)
     */
    public static Integer GetPreShutokuKabuKa(int no){
        String key = PRE_SHUTOKU_KABU_KA + no;
        Integer kabuka = CommonMng.getPrefInt(mContext,key);
        return kabuka;
    }

    /*****
     * 番号を指定して取得株数を取得
     * @param no 銘柄番号(0～)
     */
    public static Integer GetPreShutokuKabuSuu(int no){
        String key = PRE_SHUTOKU_KABU_SUU + no;
        Integer kabusuu = CommonMng.getPrefInt(mContext,key);
        return kabusuu;
    }

    /*****
     * 番号を指定して予想株価を取得
     * @param no 銘柄番号(0～)
     */
    public static Integer GetPreYosouKabuKa(int no){
        String key = PRE_YOSOU_KABU_KA + no;
        Integer kabuka = CommonMng.getPrefInt(mContext,key);
        return kabuka;
    }

    /*****
     * 番号を指定して一株配当 整数部分を取得
     * @param no 銘柄番号(0～)
     */
    public static Integer GetPreHitokabuHaitou1(int no){
        String key = PRE_HITOKABU_HAITOU1 + no;
        Integer haitou = CommonMng.getPrefInt(mContext,key);
        return haitou;
    }

    /*****
     * 番号を指定して一株配当 小数点部分を取得
     * @param no 銘柄番号(0～)
     */
    public static Integer GetPreHitokabuHaitou2(int no){
        String key = PRE_HITOKABU_HAITOU2 + no;
        Integer haitou = CommonMng.getPrefInt(mContext,key);
        return haitou;
    }

    /*****
     * 番号を指定して予想年数を取得
     * @param no 銘柄番号(0～)
     */
    public static Integer GetPreYosouNenSuu(int no){
        String key = PRE_YOSOU_NEN_SUU + no;
        Integer nensuu = CommonMng.getPrefInt(mContext,key);
        return nensuu;
    }


    /*****
     * ユーザーの登録銘柄数を設定
     */
    public static void SetPreSaveNum(int no){
        sSaveNum = no;
        CommonMng.setPrefInt(mContext, PRE_SAVE_NUM,no);
    }

    /*****
     * ユーザーの編集中銘柄番号を設定
     */
    public static void SetUsingNo(int no){
        sUsingNo = no;
        CommonMng.setPrefInt(mContext, PRE_USING_NO,no);
    }

    /*****
     * ユーザーの登録銘柄数を一つ追加
     */
    public static void AddPreSaveNum(){
        sSaveNum++;
        CommonMng.setPrefInt(mContext, PRE_SAVE_NUM, sSaveNum);
    }



    /*****
     * 番号を指定して銘柄を登録
     * @param no 銘柄番号(0～)
     * @param data 銘柄名
     */
    public static void SetPreMeigara(int no,String data){
        String key = PRE_MEIGARA + no;
        CommonMng.setPrefString(mContext,key,data);
    }

    /*****
     * 番号を指定して取得株価を登録
     * @param no 銘柄番号(0～)
     * @param price 価格
     */
    public static void SetPreShutokuKabuKa(int no,int price){
        String key = PRE_SHUTOKU_KABU_KA + no;
        CommonMng.setPrefInt(mContext,key,price);
    }

    /*****
     * 番号を指定して取得株数を登録
     * @param no 銘柄番号(0～)
     * @param num 枚数
     */
    public static void SetPreShutokuKabuSuu(int no,int num){
        String key = PRE_SHUTOKU_KABU_SUU + no;
        CommonMng.setPrefInt(mContext,key,num);
    }


    /*****
     * 番号を指定して予想株価を登録
     * @param no 銘柄番号(0～)
     * @param num 枚数
     */
    public static void SetPreYosouKabuKa(int no,int num){
        String key = PRE_YOSOU_KABU_KA + no;
        CommonMng.setPrefInt(mContext,key,num);
    }

    /*****
     * 編集中の銘柄名を変数とプリファランスに保存
     * @param data 銘柄名
     */
    public static void SetMeigara(String data){
        String key = PRE_MEIGARA + sUsingNo;
        CommonMng.setPrefString(mContext,key,data);
        sMeigara = data;

    }

    /*****
     * 編集中の取得株価を変数とプリファランスに保存
     * @param price 株価
     */
    public static void SetShutokuKabuKa(int price){
        String key = PRE_SHUTOKU_KABU_KA + sUsingNo;
        CommonMng.setPrefInt(mContext,key,price);

        sShutokuKabuKa = price;
    }

    /*****
     * 編集中の取得株数を変数とプリファランスに保存
     * @param num 株数
     */
    public static void SetShutokuKabuSuu(int num){
        String key = PRE_SHUTOKU_KABU_SUU + sUsingNo;
        CommonMng.setPrefInt(mContext,key,num);

        sShutokuKabuSuu = num;
    }

    /*****
     * 編集中の予想株価を変数とプリファランスに保存
     * @param price 株価
     */
    public static void SetYosouKabuKa(int price){
        String key = PRE_YOSOU_KABU_KA + sUsingNo;
        CommonMng.setPrefInt(mContext,key,price);

        sYosouKabuKa = price;
    }

    /*****
     * 編集中の一株配当を変数とプリファランスに保存
     * @param haitou 配当
     */
    public static void SetHitokabuHaitou1(int haitou){
        String key = PRE_HITOKABU_HAITOU1 + sUsingNo;
        CommonMng.setPrefInt(mContext,key,haitou);

        sHitokabuHaitou1 = haitou;
    }

    /*****
     * 編集中の一株配当を変数とプリファランスに保存
     * @param haitou 配当
     */
    public static void SetHitokabuHaitou2(int haitou){
        String key = PRE_HITOKABU_HAITOU2 + sUsingNo;
        CommonMng.setPrefInt(mContext,key,haitou);

        sHitokabuHaitou2 = haitou;
    }

    /*****
     * 編集中の予想年数を変数とプリファランスに保存
     * @param num 年数
     */
    public static void SetYosouNenSuu(int num){
        String key = PRE_YOSOU_NEN_SUU + sUsingNo;
        CommonMng.setPrefInt(mContext,key,num);

        sYosouNenSuu = num;
    }
}
