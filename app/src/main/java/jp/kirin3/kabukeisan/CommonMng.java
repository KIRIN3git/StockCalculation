package jp.kirin3.kabukeisan;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * Created by shinji on 2017/06/15.
 */

public class CommonMng {


    public static Point real = null;

	/**
	 * Default Preferenceへの書き込み・読み込み
	 */
	public static final String DATASAVE = "DataSave";
	
	public static float DpToPx(float dp,float density){
		float px = dp * density;

		return px;
	}

    public static int DpToPx2(int dp,float density){
        int px = (int)(dp * density);

        return px;
    }

	public static int PxToDp2(int px,float density){
		int dp = (int)(px / density);

		return dp;
	}


	// XY座標のパーセントをピクセルで返す
	public static float[] PsToPx(float psx,float psy){

		float ret[] = {0.0f,0.0f};
        if( real == null ) return ret;

        ret[0] = real.x * (psx / 100.0f);
        ret[1] = real.y * (psy / 100.0f);

		return ret;
	}

	public static String GetDateString(int option ){

		String date = null;

		Calendar calendar = Calendar.getInstance();

		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH) + 1;
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
		final int second = calendar.get(Calendar.SECOND);
		if( option == 1 ) date = String.format( "%04d-%02d-%02d %02d:%02d:%02d",year,month,day,hour,minute,second );

		return date;
	}


	/**
	 * 金額表示
	 *
	 * @param cost 1円単位の金額
	 * @return 整形後の金額
	 */
	public static String costString(long cost) {
		DecimalFormat df1 = new DecimalFormat("###,###");
		return "¥" + df1.format(cost);
	}

	/**
	 * ダイアログ表示
	 */
	public static void showAlert(String title,String message,Context context){
		AlertDialog.Builder alertDlg = new AlertDialog.Builder(context);
		alertDlg.setTitle(title);
		alertDlg.setMessage(message);
		alertDlg.setPositiveButton(
				"OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// OK ボタンクリック処理
					}
				});

		// 表示
		alertDlg.create().show();
	}

	public static String getPrefData(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		return sharedPreferences.getString(key, "");
	}

	public static String getPrefData(Context context, String key, String _ini) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		return sharedPreferences.getString(key, _ini);
	}

	// Boolean型の読み書き
	public static void setPrefBoolean(Context context, String key, boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * Boolean型の書き込み（commitではなくapplyを使用する）。applyはバックグラウンドで書き込みを行うため、即時書き込み完了の保障は無い。瞬時に書き読みを行う場合は「setPrefBoolean」を使用すること
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefBoolean2(Context context, String key, boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(key, value).apply();
	}

	public static boolean getPrefBoolean(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, false);
	}

	public static boolean getPrefBoolean(Context context, String key,
										 boolean defaultParam) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defaultParam);
	}

	// int型の読み書き
	public static void setPrefInt(Context context, String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		sharedPreferences.edit().putInt(key, value).commit();
	}

	/**
	 * int型の書き込み（commitではなくapplyを使用する）。applyはバックグラウンドで書き込みを行うため、即時書き込み完了の保障は無い。瞬時に書き読みを行う場合は「setPrefInt」を使用すること
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setPrefInt2(Context context, String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		sharedPreferences.edit().putInt(key, value).apply();
	}

	/**
	 * 初期値を指定して値を取得
	 *
	 * @param context
	 * @param key
	 * @param _ini    初期値
	 * @return
	 */
	public static int getPrefInt(Context context, String key, int _ini) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		return sharedPreferences.getInt(key, _ini);

	}

	public static int getPrefInt(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		return sharedPreferences.getInt(key, 0);

	}

	//long型の読み書き
	public static void setPrefLong(Context context, String key, long value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		sharedPreferences.edit().putLong(key, value).commit();
	}

	public static long getPrefLong(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		return sharedPreferences.getLong(key, 0);
	}

	/**
	 * 指定したキーの項目を削除
	 *
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean delPref(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				DATASAVE, Activity.MODE_PRIVATE);
		return sharedPreferences.edit().remove(key).commit();
	}

}
