package vn.bakastar.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import vn.bakastar.exceptions.ConfigurationException;

public class ConfigFileUtil {

	private static final String CONFIG_FILE = "application.ini";
	private static final String SEQ_ID = ".seq_ID";

	public static void saveSeqID(Map<String, Long> dbSeqIdMap) throws ConfigurationException {
		Properties properties = new Properties();

		File configFile = new File(CONFIG_FILE);
		try {
			configFile.createNewFile();
		}
		catch (IOException e) {
			throw new ConfigurationException("Cannot create config file " 
				+ CONFIG_FILE, e);
		}

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(configFile);
			properties.load(is);
			is.close();

			for (Map.Entry<String, Long> dbSeqId : dbSeqIdMap.entrySet()) {
				String name = dbSeqId.getKey();
				Long pk = dbSeqId.getValue();

				properties.setProperty(name.concat(SEQ_ID), String.valueOf(pk));
			}

			os = new FileOutputStream(configFile);
			properties.store(os, null);
			os.close();
		}
		catch (IOException e) {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			}
			catch (IOException ioe) {
				_logger.warn(e.getMessage(), ioe);
			}

			throw new ConfigurationException("Cannot load config file " 
				+ CONFIG_FILE, e);
		}
	}

	public static void saveSeqID(String name, long seqID) throws ConfigurationException {
		Properties properties = new Properties();

		File configFile = new File(CONFIG_FILE);
		try {
			configFile.createNewFile();
		}
		catch (IOException e) {
			throw new ConfigurationException("Cannot create config file " 
				+ CONFIG_FILE, e);
		}

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(configFile);
			properties.load(is);
			is.close();

			properties.setProperty(name.concat(SEQ_ID), String.valueOf(seqID));

			os = new FileOutputStream(configFile);
			properties.store(os, null);
			os.close();
		}
		catch (IOException e) {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			}
			catch (IOException ioe) {
				_logger.warn(e.getMessage(), ioe);
			}

			throw new ConfigurationException("Cannot load config file " 
				+ CONFIG_FILE, e);
		}
	}

	public static long getSeqID(String name) throws ConfigurationException {
		Properties properties = new Properties();

		File configFile = new File(CONFIG_FILE);
		try {
			configFile.createNewFile();
		}
		catch (IOException e) {
			throw new ConfigurationException("Cannot create config file " 
				+ CONFIG_FILE, e);
		}

		try (InputStream is = new FileInputStream(configFile);) {
			properties.load(is);

			return Long.parseLong(properties.getProperty(
				name.concat(SEQ_ID), String.valueOf(0L)));
		}
		catch (IOException e) {
			throw new ConfigurationException("Cannot load config file " 
					+ CONFIG_FILE, e);
		}
	}

	private static Logger _logger = Logger.getLogger(ConfigFileUtil.class.getName());
}
