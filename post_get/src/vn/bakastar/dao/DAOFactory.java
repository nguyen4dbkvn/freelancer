package vn.bakastar.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.util.PropsKey;
import vn.bakastar.util.PropsUtil;
import vn.bakastar.util.PropsValue;

public class DAOFactory {

	private String url;
	private String userName;
	private String password;
	private String postTableName;
	private String getTableName;

	private DAOModel _postDAOModel;
	private DAOModel _getDAOModel;

	private DAOFactory(String url, String userName, String password, 
			String postTableName, String getTableName) {
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.postTableName = postTableName;
		this.getTableName = getTableName;
	}

	public static DAOFactory getInstance(String name, boolean original) 
			throws ConfigurationException {

		if (name == null) {
			throw new ConfigurationException("Database name cannot be null.");
		}

		String url = PropsUtil.get(name + "." + PropsKey.JDBC_URL);
		String driver = PropsUtil.get(name + "." + PropsKey.JDBC_DRIVER, PropsValue.MYSQL_JDBC_DRIVER);
		String userName = PropsUtil.get(name + "." + PropsKey.JDBC_USERNAME);
		String password = PropsUtil.get(name + "." + PropsKey.JDBC_PASSWORD);

		String postTableName = PropsValue.GET_TABLE_POST_NAME;
		String getTableName = PropsValue.POST_TABLE_GET_NAME;

		if (original) {
			postTableName = PropsValue.POST_TABLE_POST_NAME;
			getTableName = PropsValue.GET_TABLE_GET_NAME;
		}

		if (url == null || url.trim().length() == 0) {
			throw new ConfigurationException(
				String.format("Required url of database `%s` in properties file.", name));
		}

		try {
			Class.forName(driver);
		}
		catch (ClassNotFoundException cnfe) {
			throw new ConfigurationException("Driver class is missing in classpath.");
		}

		return new DAOFactory(url, userName, password, postTableName, getTableName);
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, userName, password);
	}

	public DAOModel getPostDAOModel() {
		if (_postDAOModel == null) {
			_postDAOModel = new DAOModelImpl(this, postTableName);
		}

		return _postDAOModel;
	}

	public DAOModel getGetDAOModel() {
		if (_getDAOModel == null) {
			_getDAOModel = new DAOModelImpl(this, getTableName);
		}

		return _getDAOModel;
	}
}
