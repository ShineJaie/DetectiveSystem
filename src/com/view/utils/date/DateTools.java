package com.view.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateTools {

	/**
	 * 
	 * @param date
	 * @return "yyyy-MM-dd HH:mm:ss", 如果为空则返回""
	 */
	public static String formateDate(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		if (date == null || date.equals("")) {
			return "";
		}
		return dateformat.format(date);
	}

}
