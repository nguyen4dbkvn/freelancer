package vn.bakastar.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import vn.bakastar.exceptions.ConfigurationException;

public class PropsUtil {

	private static final String PROPERTIES_FILE = "application.properties";

	private static final Properties PROPERTIES = new Properties();

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES_FILE);

		if (propertiesFile == null) {
			throw new ConfigurationException("Not found properties file " + PROPERTIES_FILE);
		}

		try {
			PROPERTIES.load(propertiesFile);
		}
		catch(IOException e) {
			throw new ConfigurationException("Cannot load properties file " + PROPERTIES_FILE, e);
		}
	}

	public static String get(String key) {

		return PROPERTIES.getProperty(key);
	}

	public static String get(String key, String defaultValue) {

		return PROPERTIES.getProperty(key, defaultValue);
	}

	public static String[] get(String key, String[] defaultValues) {

		String value = get(key);
		String[] results = null;

		if (value != null && value.trim().length() != 0) {
			results = value.split(",");
		}

		if (results != null && results.length != 0) {
			return results;
		}
		else {
			return defaultValues;
		}
	}

	public static long get(String key, long defaultValue) {

		return Long.parseLong(get(key, String.valueOf(defaultValue)));
	}

	public static long get(long value, long max, long min) {

		if (value < min) {

			return min;
		}

		if (value > max) {

			return max;
		}

		return value;
	}

	public static int get(String key, int defaultValue) {

		return Integer.parseInt(get(key, String.valueOf(defaultValue)));
	}

	public static int get(int value, int max, int min) {

		if (value < min) {

			return min;
		}

		if (value > max) {

			return max;
		}

		return value;
	}
}
