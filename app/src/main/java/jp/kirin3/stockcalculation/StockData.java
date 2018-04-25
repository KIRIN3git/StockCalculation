package jp.kirin3.stockcalculation;

/**
 * Created by etisu on 2018/04/22.
 */

import android.content.Context;
import android.util.Log;

/****
 * ユーザーの保存データを管理
 * 利用時はInitStockData()を呼び出す必要がある
 */
public class StockData {


    // 編集中取得銘柄
    public static String sShutokuMeigara;

    // 編集中取得株価
    public static Integer sShutokuKabuKa;

    // 編集中取得株数
    public static Integer sShutokuKabuSuu;

    // 編集中予想株価
    public static Integer sYosouKabuSuu;

    // 最大銘柄登録数
    final static int MEIGARA_MAX_NUM = 5;
    // ユーザーの銘柄数
    public static Integer sUserMeigaraNum = 0;
    // 編集中銘柄番号
    private static Integer sNowMeigaraNo = 0;

    private static Context mContext;


    // ・プリファランス名
    // 銘柄 + i
    final static String PRE_MEIGARA = "PRE_MEIGARA_";
    // 株価 + i
    final static String PRE_KABU_KA = "PRE_KABU_KA_";
    // 株数 + i
    final static String PRE_KABU_SUU = "PRE_KABU_SUU_";
    // ユーザーの銘柄登録数
    final static String PRE_USER_MEIGARA_NUM = "PRE_USER_MEIGARA_NUM";
    // ユーザーの編集中銘柄番号
    final static String PRE_NOW_MEIGARA_NO = "PRE_NOW_MEIGARA_NO";



    public static void InitStockData(Context context){
        mContext = context;

        sUserMeigaraNum = GetPreUserMeigaraNum();
        sNowMeigaraNo = GetPreNowMeigaraNo();
    }


    /*****
     * ユーザーの登録銘柄数を取得
     */
    public static int GetPreUserMeigaraNum(){
        int ret = CommonMng.getPrefInt(mContext,PRE_USER_MEIGARA_NUM);
        return ret;
    }

    /*****
     * ユーザーの編集中銘柄番号を取得
     */
    public static int GetPreNowMeigaraNo(){
        int ret = CommonMng.getPrefInt(mContext,PRE_NOW_MEIGARA_NO);
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
     * 番号を指定して株価を取得
     * @param no 銘柄番号(0～)
     */
    public static Integer GetPreKabuKa(int no){
        String key = PRE_KABU_KA + no;
        Integer kabuka = CommonMng.getPrefInt(mContext,key);
        return kabuka;
    }

    /*****
     * 番号を指定して株数を取得
     * @param no 銘柄番号(0～)
     */
    public static Integer GetPreKabuSuu(int no){
        String key = PRE_KABU_SUU + no;
        Integer kabusuu = CommonMng.getPrefInt(mContext,key);
        return kabusuu;
    }




    /*****
     * ユーザーの登録銘柄数を設定
     */
    public static void SetPreUserMeigaraNum(int no){
        sUserMeigaraNum = no;
        CommonMng.setPrefInt(mContext,PRE_USER_MEIGARA_NUM,no);
    }

    /*****
     * ユーザーの編集中銘柄番号を設定
     */
    public static void SetPreNowMeigaraNo(int no){
        sNowMeigaraNo = no;
        CommonMng.setPrefInt(mContext,PRE_NOW_MEIGARA_NO,no);
    }

    /*****
     * ユーザーの登録銘柄数を一つ追加
     */
    public static void AddPreUserMeigaraNum(){
        sUserMeigaraNum++;
        CommonMng.setPrefInt(mContext,PRE_USER_MEIGARA_NUM,sUserMeigaraNum);
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
     * 番号を指定して株価を登録
     * @param no 銘柄番号(0～)
     * @param price 価格
     */
    public static void SetPreKabuKa(int no,int price){
        String key = PRE_KABU_KA + no;
        CommonMng.setPrefInt(mContext,key,price);
    }

    /*****
     * 番号を指定して株数を登録
     * @param no 銘柄番号(0～)
     * @param num 枚数
     */
    public static void SetPreKabuSuu(int no,int num){
        String key = PRE_KABU_SUU + no;
        CommonMng.setPrefInt(mContext,PRE_USER_MEIGARA_NUM,num);
    }

    /*****
     * 編集中の銘柄名を変数とプリファランスに保存
     * @param data 銘柄名
     */
    public static void SetMeigara(String data){
        String key = PRE_MEIGARA + sNowMeigaraNo;
        CommonMng.setPrefString(mContext,key,data);

        Log.w( "DEBUG_DATA", "key " + key);
        Log.w( "DEBUG_DATA", "data " + data);
    }

    /*****
     * 編集中の株価を変数とプリファランスに保存
     * @param price 株価
     */
    public static void SetKabuKa(int price){
        String key = PRE_KABU_KA + sNowMeigaraNo;
        CommonMng.setPrefInt(mContext,key,price);

        Log.w( "DEBUG_DATA", "key " + key);
        Log.w( "DEBUG_DATA", "data " + price);
    }

    /*****
     * 編集中の株数を変数とプリファランスに保存
     * @param num 株数
     */
    public static void SetKabuSuu(int num){
        String key = PRE_KABU_SUU + sNowMeigaraNo;
        CommonMng.setPrefInt(mContext,key,num);

        Log.w( "DEBUG_DATA", "key " + key);
        Log.w( "DEBUG_DATA", "data " + num);
    }


}
