package vn.bakasta.geocoder.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import vn.bakasta.geocoder.dao.model.GeoCoderModelDAO;
import vn.bakasta.geocoder.dao.model.GeoCoderModelDAOImpl;
import vn.bakasta.geocoder.exceptions.ConfigurationException;
import vn.bakasta.geocoder.exceptions.DAOException;
import vn.bakasta.geocoder.model.GeoCoder;
import vn.bakasta.geocoder.util.PropsValue;

public class DBConnection {

	public DBConnection(String url, String userName, String password) throws ConfigurationException {
		this.url = url;
		this.userName = userName;
		this.password = password;

		String driver = PropsValue.DB_JDBC_DRIVER;

		if (url == null || url.trim().length() == 0) {
			throw new ConfigurationException("Required url of database in properties file.");
		}

		try {
			Class.forName(driver);
		}
		catch (ClassNotFoundException cnfe) {
			throw new ConfigurationException("Driver class is missing in classpath.");
		}
	}

	public Connection getConnection() throws DAOException {
		try {
		
			return DriverManager.getConnection(url, userName, password);
		}
		catch (SQLException e) {
			throw new DAOException("Connecting fail database", e);
		}
		
	}

	public GeoCoderModelDAO getModelDAO() {
		if (_modelDAO == null) {
			_modelDAO = new GeoCoderModelDAOImpl(this, PropsValue.DB_TABLE_NAME);
		}

		return _modelDAO;
	}

	public int count() throws DAOException {

		return getModelDAO().count();
	}

	public List<GeoCoder> list(int limit) throws DAOException {

		return getModelDAO().list(limit);
	}

	public void update(long seqId, String address) throws DAOException {

		getModelDAO().update(seqId, address);
	}

	protected String url;
	protected String userName;
	protected String password;

	protected GeoCoderModelDAO _modelDAO;
}
