/*
 * 文 件 名:  HealthUtils.java
 * 版    权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  shenxm
 * 修改时间:  2013-4-23
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import com.panthole.hibernate.constants.Constants.Hex;
import com.panthole.hibernate.constants.Constants.Num4Int;

/**
 * 常用工具类
 */
public abstract class HealthUtils {
	/**
	 * ASyncTask线程池
	 */
	private static ExecutorService executorService = Executors
			.newSingleThreadExecutor();

	private static int screenWidth;

	private static int screenHeight;

	private static long lastClickTime;

	/**
	 * 返回 screenWidth
	 * 
	 * @return screenWidth
	 */
	public static int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * 对screenWidth进行赋值
	 * 
	 * @param screenWidth
	 *            screenWidth
	 */
	public static void setScreenWidth(int screenWidth) {
		HealthUtils.screenWidth = screenWidth;
	}

	/**
	 * 返回 screenHeight
	 * 
	 * @return screenHeight
	 */
	public static int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * 对screenHeight进行赋值
	 * 
	 * @param screenHeight
	 *            screenHeight
	 */
	public static void setScreenHeight(int screenHeight) {
		HealthUtils.screenHeight = screenHeight;
	}

	/**
	 * 判断某个字符串是否存在于数组中
	 * 
	 * @param stringArray
	 *            原数组
	 * @param source
	 *            查找的字符串
	 * @return 是否找到
	 */
	public static boolean isContains(String[] stringArray, String source) {
		// 转换为list

		if (stringArray != null && stringArray.length != 0) {
			List<String> tempList = Arrays.asList(stringArray);
			if (HealthUtils.isNotEmpty(tempList)) {

				// 利用list的包含方法，进行判断
				if (tempList.contains(source)) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	/**
	 * 判断一个数组或集合是否为空
	 * 
	 * @param array
	 *            数组或集合
	 * @return 是否为空 true 为空 false 不为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object array) {
		if (null != array) {
			if (array instanceof List) {
				List list = (List) array;
				if (!list.isEmpty()) {
					return false;
				}
			} else if (array instanceof Map) {
				Map map = (Map) array;
				if (!map.isEmpty()) {
					return false;
				}
			} else if (array instanceof Object[]) {
				Object[] arr = (Object[]) array;
				if (arr.length > 0) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 将字符串进行MD5加密
	 * 
	 * @param s
	 *            字符串
	 * @return 加密后的字符串
	 */
	public static final String mD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * Num4Int.NUM_2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> Num4Int.NUM_4 & Hex.HEX_0XF];
				str[k++] = hexDigits[byte0 & Hex.HEX_0XF];
			}
			// 32位
			// return new String(str);

			// 16位
			return new String(str).substring(Num4Int.NUM_8, Num4Int.NUM_24);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断一个数组或集合是否不为空
	 * 
	 * @param array
	 *            数组或集合
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(Object array) {
		return !isEmpty(array);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            待判断的字符串
	 * @return 字符串是否为空
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return true;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;

		return true;
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param str
	 *            待判断的字符串
	 * @return 字符串是否不为空
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 获取任务线程池
	 * 
	 * @return 任务线程池
	 */
	public static ExecutorService getExecutor() {
		return executorService;
	}

	/**
	 * 判断当前是否联网
	 * 
	 * @param context
	 *            应用上下文
	 * @return 应用是否联网
	 */
	public static boolean isConnToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] infoArr = connectivity.getAllNetworkInfo();
			if (isNotEmpty(infoArr)) {
				for (NetworkInfo networkInfo : infoArr) {
					if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * 将参考值返回分隔符
	 * 
	 * @param minVal
	 *            ('-','--','%'...)maxVal
	 * @returnl('-','--','%'...)
	 */
	public static String getSeparator(String referenceValue) {
		if (referenceValue != null) {
			int beginIndex = 0;
			int endIndex = 0;
			for (int i = 0; i < referenceValue.length(); i++) {
				int c = referenceValue.charAt(i);

				if (c >= '0' && c <= '9') {
					if (endIndex == 0) {
						beginIndex++;
					}
				} else if (c == '.' || c == ' ') {
					if (endIndex == 0) {
						beginIndex++;
					}

				} else {
					endIndex++;
				}

			}
			String separator = referenceValue.substring(beginIndex, beginIndex
					+ endIndex);
			System.out.println(separator);
			if (separator.equals("")) {
				return "noSeparator";
			}

			else {
				return separator;
			}
		}

		else {

			return "noSeparator";
		}

	}

	/**
	 * 判断值是否在范围内
	 * 
	 * @param result
	 *            获得值
	 * @param referenceValue
	 *            参考范围
	 * @param status
	 *            * 0.偏低 1.正常 2.偏高
	 * @return 应用是否在范围内
	 */
	public static boolean isEhrWarm(String result, String referenceValue) {

		String separator = getSeparator(referenceValue);
		System.out.println(separator);
		if (result == null || referenceValue == null || result.equals("-")
				|| isChinese(result)) {
			return false;
		} else if (separator.equals("<")) {
			// 小于标准值
			String s[] = referenceValue.split(separator);
			Double maxVal = Double.parseDouble(s[1]);
			Double res = Double.parseDouble(result);
			if (res <= maxVal) {
				return false;
			}
			return true;
		} else if (separator.equals(">")) {
			// 大于标准值
			String s[] = referenceValue.split(separator);
			Double maxVal = Double.parseDouble(s[1]);
			Double res = Double.parseDouble(result);
			if (res >= maxVal) {
				return false;
			}
			return true;
		}
		// else if (separator.equals("＜"))
		// {
		// return false;
		// }
		else {
			if (isHave(referenceValue, separator)) {
				try {
					String s[] = referenceValue.split(separator);

					Double minVal = Double.parseDouble(s[0]);
					Double maxVal = Double.parseDouble(s[1]);
					Double res = Double.parseDouble(result);
					if (res <= maxVal && res >= minVal) {
						return false;
					} else {
						return true;
					}
				} catch (NumberFormatException e) {
					return false;
				} catch (RuntimeException e) {
					return false;
				}

			} else {
				return false;
			}
		}

	}

	/**
	 * 判断值是否在范围内
	 * 
	 * @param result
	 *            获得值
	 * @param referenceValue
	 *            参考范围
	 * @param status
	 *            * 0.偏低 1.正常 2.偏高
	 * @return 应用是否在范围内
	 */
	public static boolean isWarm(String result, String referenceValue,
			String status) {
		if (status.equals("")) {
			String separator = getSeparator(referenceValue);
			System.out.println(separator);
			if (result == null || referenceValue == null || result.equals("-")
					|| isChinese(result)) {
				return false;
			} else if (separator.equals("<")) {
				// 小于标准值
				String s[] = referenceValue.split(separator);
				Double maxVal = Double.parseDouble(s[1]);
				Double res = Double.parseDouble(result);
				if (res <= maxVal) {
					return false;
				}
				return true;
			} else if (separator.equals(">")) {
				// 大于标准值
				String s[] = referenceValue.split(separator);
				Double maxVal = Double.parseDouble(s[1]);
				Double res = Double.parseDouble(result);
				if (res >= maxVal) {
					return false;
				}
				return true;
			}
			// else if (separator.equals("＜"))
			// {
			// return false;
			// }
			else {
				if (isHave(referenceValue, separator)) {
					try {
						String s[] = referenceValue.split(separator);

						Double minVal = Double.parseDouble(s[0]);
						Double maxVal = Double.parseDouble(s[1]);
						Double res = Double.parseDouble(result);
						if (res <= maxVal && res >= minVal) {
							return false;
						} else {
							return true;
						}
					} catch (NumberFormatException e) {
						return false;
					} catch (RuntimeException e) {
						return false;
					}

				} else {
					return false;
				}
			}
		} else {

			if (status.equals("1")) {
				return false;
			} else {
				return true;
			}

		}
	}

	/**
	 * 判断值在范围内偏高还是偏低
	 * 
	 * @param result
	 *            获得值
	 * @param referenceValue
	 *            参考范围
	 * @return 应用是否在范围内
	 */
	public static boolean isHigh(String result, String referenceValue) {

		if (referenceValue != null && result != null && !isChinese(result)) {
			String separator = getSeparator(referenceValue);

			if (separator.equals("noSeparator")) {
				return false;
			} else {
				try {
					String s[] = referenceValue.split(separator);
					Double maxVal = Double.parseDouble(s[1]);
					Double res = Double.parseDouble(result);
					if (res > maxVal) {
						return true;
					} else {
						return false;
					}
				} catch (NumberFormatException e) {
					return false;
				} catch (RuntimeException e) {
					return false;
				}

			}
		} else {
			return false;
		}
	}

	public static boolean isChinese(String strName) {

		char[] ch = strName.toCharArray();

		for (int i = 0; i < ch.length; i++) {

			char c = ch[i];

			if (isChinese(c) == true) {

				return isChinese(c);

			} else {

				return isChinese(c);

			}
		}
		return false;
	}

	// GENERAL_PUNCTUATION 判断中文的“号

	// CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号

	// HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号

	public static boolean isChinese(char c) {
		// || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

		// || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

		|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		} else

			return false;

	}

	// 判断 是否为中文字符
	public static boolean isChineseByREG(String str) {
		boolean isChinese = true;
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
			} else {
				isChinese = false;
				break;
			}
		}
		return isChinese;
	}

	/**
	 * 判断循环查找字符串数组中的每个字符串中是否包含所有查找的内容
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static boolean isHave(String str, String s) {
		if (s.equals("noSeparator")) {
			return false;
		} else {
			if (str.indexOf(s) != -1) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 判断email格式是否正确
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^[a-zA-Z0-9_-]+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;

		// 无非就是计算圆形区域
		if (width <= height) {
			roundPx = width / 2;
			float clip = (height - width) / 2;
			top = clip;
			bottom = width + clip;
			left = 0;
			right = width;

			height = width;

			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;

			left = clip;
			right = height + clip;
			top = 0;
			bottom = height;

			width = height;

			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		paint.setColor(0xFFFFFFFF);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.rgb(186, 186, 186));
		paint.setStrokeWidth(10);
		paint.setAntiAlias(true);
		canvas.drawCircle(roundPx, roundPx, 320, paint);
		return output;
	}

	/**
	 * 转换图片成小图
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */

	/**
	 * 获取组件高度（宽度）
	 * 
	 * @param view
	 * @return
	 */
	public static int getHeight(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredHeight();
	}

	/**
	 * 获取字体高度
	 * 
	 * @param view
	 * @return
	 */
	public int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent);
	}

	/**
	 * 获取当地年月日和星期几
	 * 
	 * @param view
	 * @return
	 */

	public static String getDate() {
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		// 月、日前加0

		int mm = Integer.parseInt(mMonth);
		if (mm < 10) {
			mMonth = "0" + mm;
		}
		if (Integer.parseInt(mDay) < 10) {
			mDay = "0" + mDay;
		}
		// 星期几
		if ("1".equals(mWay)) {
			mWay = "日";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		return mYear + "年" + mMonth + "月" + mDay + "日" + "  星期" + mWay;
	}

	/**
	 * 获取web服务地址
	 * 
	 * @param view
	 * @return
	 */

	private static String getIp() {
		String TAG = "HealthGrid";
		String urlString = "http://sharpgrid.vicp.net:8888/health-bizserver/getIP.jsp";// http地址
		String resultData = "";// 获得的数据
		URL url = null;
		try {
			url = new URL(urlString);// 构造一个url对象
		} catch (MalformedURLException e) {
			Log.e(TAG, "MalformedURLException");
			e.printStackTrace();
		}
		if (url != null) {
			try {
				HttpURLConnection urlConnection;// 使用HttpURLConnection打开连接
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream stream = urlConnection.getInputStream();// 得到读取的内容
				InputStreamReader in = new InputStreamReader(stream);
				BufferedReader buffere = new BufferedReader(in); // 为输出创建BufferedReader
				String line = null;
				while ((line = buffere.readLine()) != null) {// 使用while循环来取得获取的数据
					resultData += line + "\n";// 我们要在每一行的后面加上一个反斜杠来换行
				}
				if (resultData.equals("")) {
					return "";
				} else {
					return resultData;
				}
			} catch (IOException e) {
				Log.e(TAG, "IOException");
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, "url null");
		}
		return resultData;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @param String
	 *            format
	 * @return
	 */
	public static String getSystemTime(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String currentTime = formatter.format(curDate);
		return currentTime;
	}



	/**
	 * 获取字符串的长度，中文占一个字符,英文数字占半个字符
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static double length(String value) {
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < value.length(); i++) {
			// 获取一个字符
			String temp = value.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				valueLength += 1;
			} else {
				// 其他字符长度为0.5
				valueLength += 0.5;
			}
		}
		// 进位取整
		return Math.ceil(valueLength);
	}

	/**
	 * 用户姓名只显示姓**
	 * 
	 * @param String
	 *            format
	 * @return
	 */
	public static String getLastName(String patientName) {
		if (HealthUtils.isBlank(patientName)) {
			return "--";
		} else {
			int length = (int) length(patientName) - 1;
			patientName = patientName.substring(0, 1);
			for (int i = 0; i < length; i++) {
				patientName = patientName + "*";
			}

			return patientName;
		}
	}

	/**
	 * 创建目录
	 */
	public static void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * 防止按钮连续点击
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

}