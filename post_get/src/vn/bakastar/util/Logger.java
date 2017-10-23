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

	public static void log(String className, String message) {

		if (isEnable()) {
			System.out.print(
				String.format("%s [%s][%s] %s", 
					Thread.currentThread().getName(), className, 
					logSdf.format(new Date()), message));
		}
	}

	public static void log(String className, Throwable cause) {

		if (isEnable()) {
			System.out.print(
				String.format("%s [%s][%s] %s", Thread.currentThread().getName(), 
					className, logSdf.format(new Date()), cause.getMessage()));

			cause.printStackTrace();
		}
	}

	public static void log(String className, String message, Throwable cause) {

		if (isEnable()) {
			System.out.print(
				String.format("%s [%s][%s] %s", Thread.currentThread().getName(), 
					className, logSdf.format(new Date()), message));

			cause.printStackTrace();
		}
	}

	private static boolean _enable = false;
	private static final SimpleDateFormat logSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
}
