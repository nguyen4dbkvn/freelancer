package vn.bakastar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import vn.bakastar.dao.model.GetModelDAO;
import vn.bakastar.dao.model.PostModelDAO;
import vn.bakastar.db.model.impl.GetModelDAOImpl;
import vn.bakastar.db.model.impl.PostModelDAOImpl;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.GetEntry;
import vn.bakastar.model.PostEntry;
import vn.bakastar.util.PropsKey;
import vn.bakastar.util.PropsUtil;
import vn.bakastar.util.PropsValue;

public class DBConnection {

	private DBConnection(String name, String url, String userName, String password, 
			String postTableName, String getTableName) {
		this.name = name;
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.postTableName = postTableName;
		this.getTableName = getTableName;
	}

	public static DBConnection getInstance(String name, boolean original) 
			throws ConfigurationException {

		if (name == null) {
			throw new ConfigurationException("Database name cannot be null.");
		}

		String url = PropsUtil.get(name + "." + PropsKey.JDBC_URL);
		String driver = PropsUtil.get(name + "." + PropsKey.JDBC_DRIVER, PropsValue.MYSQL_JDBC_DRIVER);
		String userName = PropsUtil.get(name + "." + PropsKey.JDBC_USERNAME);
		String password = PropsUtil.get(name + "." + PropsKey.JDBC_PASSWORD);

		String postTableName = PropsValue.GET_SOURCE_TABLE_NAME;
		String getTableName = PropsValue.POST_DESTINATION_TABLE_NAME;

		if (original) {
			postTableName = PropsValue.POST_SOURCE_TABLE_NAME;
			getTableName = PropsValue.GET_DESTINATION_TABLE_NAME;
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

		return new DBConnection(name, url, userName, password, postTableName, getTableName);
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, userName, password);
	}

	public String getName() {
		return name;
	}

	public PostModelDAO getPostDAOModel() {
		if (_postModelDAO == null) {
			_postModelDAO = new PostModelDAOImpl(this, postTableName);
		}

		return _postModelDAO;
	}

	public int countPost() throws DAOException {

		return getPostDAOModel().count();
	}

	public List<PostEntry> listPost() throws DAOException {

		return getPostDAOModel().list();
	}

	public void create(PostEntry postEntry) throws DAOException {

		getPostDAOModel().create(postEntry);
	}

	public void delete(PostEntry postEntry) throws DAOException {

		getPostDAOModel().delete(postEntry);
	}

	public void deletePost(long postEntryId) {

		getPostDAOModel().delete(postEntryId);
	}

	public GetModelDAO getGetDAOModel() {
		if (_getModelDAO == null) {
			_getModelDAO = new GetModelDAOImpl(this, getTableName);
		}

		return _getModelDAO;
	}

	public int countGet() throws DAOException {

		return getGetDAOModel().count();
	}

	public List<GetEntry> listGet() throws DAOException {

		return getGetDAOModel().list();
	}

	public void create(GetEntry getEntry) throws DAOException {

		getGetDAOModel().create(getEntry);
	}

	public void delete(GetEntry getEntry) throws DAOException {

		getGetDAOModel().delete(getEntry);
	}

	public void deleteGet(long gettEntryId) {

		getGetDAOModel().delete(gettEntryId);
	}

	private String name;
	private String url;
	private String userName;
	private String password;
	private String postTableName;
	private String getTableName;

	private PostModelDAO _postModelDAO;
	private GetModelDAO _getModelDAO;
}
