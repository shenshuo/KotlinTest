package zpcaifu.kotlintest.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class Regular {
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][34587]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}
	@SuppressWarnings("unused")
	public static boolean getPass(String pass) {
		
		if(Pattern.matches("^[0-9a-zA-Z_]{6,16}", pass)){
			return true;
		}else{
			
			return false;
		}
				
		}
	public static String getDecimalFormatZero(Double dou){
		 DecimalFormat df = new DecimalFormat("###");
		 if(dou.equals("")){
			 return "";
		 }
		return df.format(dou);
	}
	public static SpannableString setSpannableString(String text ,int index,int last){
		SpannableString titleString = new SpannableString(text);
		titleString.setSpan(new AbsoluteSizeSpan(10, true), index, last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return titleString;
		
	}
	public static SpannableString setSpannablecolor(String text ,String color,int index,int last){
		SpannableString titleString = new SpannableString(text);
		titleString.setSpan(new ForegroundColorSpan(Color.parseColor(color)), index, last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return titleString;
		
	}
	public static String getDecimalFormatOne(Double dou){
		 DecimalFormat df = new DecimalFormat("###0.0");
		 if(dou.equals("")){
			 return "";
		 }
		return df.format(dou);
	}
	public static String getDecimalFormatTwo(Double dou){

		 DecimalFormat df = new DecimalFormat("###0.00");
		 if(dou.equals("")){
			 return "";
		 }
		return df.format(dou);
	}

	public static String getDecimalFormatTwoString(String dou){

		DecimalFormat df = new DecimalFormat("###0.00");
		if(dou.equals("")){
			return "";
		}
		return df.format(Double.parseDouble(dou));
	}
	public static String getMilliToDate(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	public static boolean hasSpecialCharacter(String str) {
		String regEx = "[~!@#$%^&*<>]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return false;
		}
		return true;
	}

	public static String getMilliToDateZhe(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat("dd日");
		return formatter.format(date);
	}

	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
	public static Bitmap changeColor(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int[] colorArray = new int[w * h];
		int n = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int color = getMixtureWhite(bitmap.getPixel(j, i));
				colorArray[n++] = color;
			}
		}
		return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
	}

	/**
	 * 获取和白色混合颜色
	 *
	 * @return
	 */
	private static int getMixtureWhite(int color) {
		int alpha = Color.alpha(color);
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite

						(green, alpha),
				getSingleMixtureWhite(blue, alpha));
	}

	/**
	 * 获取单色的混合值
	 *
	 * @param color
	 * @param alpha
	 * @return
	 */
	private static int getSingleMixtureWhite(int color, int alpha) {
		int newColor = color * alpha / 255 + 255 - alpha;
		return newColor > 255 ? 255 : newColor;
	}
	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	public static String getMilliToDate2(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		return formatter.format(date);
	}

	public static String getMilliToDate3(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		return formatter.format(date);
	}
	public static String getMilliToDate4(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	public static String getMilliTotime(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(date);
	}

	public static String getMilliTotime2(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日HH时mm分ss秒");
		return formatter.format(date);
	}

	public static String getMilliTotime3(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static String getMilliTotime4(String time) {
		if (time == null) {
			return " ";
		}
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formatter.format(date);
	}

	public static String data(String time) {
		if (time == null) {
			return " ";
		}
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
				Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return times;
	}

	public static String ondata() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

}
