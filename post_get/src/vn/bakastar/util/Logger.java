package vn.bakastar.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	public static void enable(boolean enable) {
		_enable = enable;
	}

	public static boolean isEnable() {

		return _enable;
	}

	public static void debug(String className, String message) {

		if (isEnable()) {
			System.out.println(
				String.format("%s %s[%s] %s", 
					logSdf.format(new Date()),
					Thread.currentThread().getName(), className, message));
		}
	}

	public static void error(String className, Throwable cause) {

		System.out.println(
			String.format("%s %s[%s] %s", logSdf.format(new Date()), 
				Thread.currentThread().getName(), 
				className, cause.getMessage()));

		cause.printStackTrace();
	}

	public static void error(String className, String message, Throwable cause) {

		System.out.println(
			String.format("%s %s[%s] %s", logSdf.format(new Date()), 
				Thread.currentThread().getName(), 
				className, message));

		cause.printStackTrace();
	}

	private static boolean _enable = false;
	private static final SimpleDateFormat logSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
}
