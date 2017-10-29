package vn.bakastar.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import vn.bakastar.dao.model.GetModelDAO;
import vn.bakastar.dao.model.ImeiModelDAO;
import vn.bakastar.dao.model.PostModelDAO;
import vn.bakastar.dao.model.impl.GetModelDAOImpl;
import vn.bakastar.dao.model.impl.ImeiModelDAOImpl;
import vn.bakastar.dao.model.impl.PostModelDAOImpl;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.GetEntry;
import vn.bakastar.model.ImeiEntry;
import vn.bakastar.model.PostEntry;
import vn.bakastar.util.PropsKey;
import vn.bakastar.util.PropsUtil;
import vn.bakastar.util.PropsValue;

public class DBConnection {

	protected static final String SQL_CHECK_EXIST_TABLE = 
		"SELECT TABLE_NAME FROM information_schema.tables WHERE (TABLE_SCHEMA = '[$SCHEMA_NAME$]') AND (TABLE_NAME = '[$TABLE_NAME$]');";

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

		String postTableName = PropsValue.POST_DESTINATION_TABLE_NAME;
		String getTableName = PropsValue.GET_SOURCE_TABLE_NAME;

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

	public Connection getConnection() throws DAOException {
		try {
		
			return DriverManager.getConnection(url, userName, password);
		}
		catch (SQLException e) {
			throw new DAOException("Connecting fail database " + getName(), e);
		}
		
	}

	public String getName() {
		return name;
	}

	public String getSchema() {
		String name = getName();

		return name.substring(0, name.indexOf("@"));
	}

	public void callStoreProcedure(String storeName) throws DAOException {
		StringBuilder sb = new StringBuilder(3);

		sb.append("CALL ");
		sb.append(storeName);
		sb.append("();");

		String call = sb.toString();

		try(CallableStatement statement = getConnection().prepareCall(call)) {

			statement.execute();
		}
		catch (SQLException e) {
			throw new DAOException(String.format(
				"Calling fail store `%s` on database[%s]", storeName, getName()), e);
		}
	}

	public boolean isExistTable(String tableName) throws DAOException {

		String query = DAOUtil.replace(SQL_CHECK_EXIST_TABLE, 
			new String[] {SQLConstants.SCHEMA_NAME, SQLConstants.TABLE_NAME}, 
			new String[] {getSchema(), tableName});

		try (
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet resultSet = ps.executeQuery();
		) {
			if (resultSet.next()) {
				return true;
			}

			return false;
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public PostModelDAO getPostModelDAO() {
		if (_postModelDAO == null) {
			_postModelDAO = new PostModelDAOImpl(this, postTableName);
		}

		return _postModelDAO;
	}

	public int countPost() throws DAOException {

		return getPostModelDAO().count();
	}

	public List<PostEntry> listPost() throws DAOException {

		return getPostModelDAO().list();
	}

	public void create(PostEntry postEntry) throws DAOException {

		getPostModelDAO().create(postEntry);
	}

	public void delete(PostEntry postEntry) throws DAOException {

		getPostModelDAO().delete(postEntry);
	}

	public void deletePost(long postEntryId) {

		getPostModelDAO().delete(postEntryId);
	}

	public GetModelDAO getGetModelDAO() {
		if (_getModelDAO == null) {
			_getModelDAO = new GetModelDAOImpl(this, getTableName);
		}

		return _getModelDAO;
	}

	public int countGet() throws DAOException {

		return getGetModelDAO().count();
	}

	public List<GetEntry> listGet() throws DAOException {

		return getGetModelDAO().list();
	}

	public void create(GetEntry getEntry) throws DAOException {

		getGetModelDAO().create(getEntry);
	}

	public void delete(GetEntry getEntry) throws DAOException {

		getGetModelDAO().delete(getEntry);
	}

	public void deleteGet(long gettEntryId) {

		getGetModelDAO().delete(gettEntryId);
	}

	public ImeiModelDAO getImeiModelDao(String tableName) throws DAOException {
		ImeiModelDAO imeiModelDAO = _imeiModelDAOMap.get(tableName);

		if (imeiModelDAO == null && isExistTable(tableName)) {
			imeiModelDAO = new ImeiModelDAOImpl(this, tableName);

			_imeiModelDAOMap.put(tableName, imeiModelDAO);
		}

		return imeiModelDAO;
	}

	public void create(ImeiEntry imeiEntry, String imei) throws DAOException, ParseException {
		String prefix = PropsValue.GET_DESTINATION_TABLE_IMEI_PREFIX;
		String tableName = prefix.concat(imei);

		ImeiModelDAO imeiModelDAO = getImeiModelDao(tableName);

		if (imeiModelDAO != null) {

			imeiModelDAO.create(imeiEntry);
		}
		else if (_logger.isDebugEnabled()) {
			_logger.debug(String.format("Skipped inserting `%s`.`%s` because not found table.", 
				getName(), tableName));
		}
	}

	private String name;
	private String url;
	private String userName;
	private String password;
	private String postTableName;
	private String getTableName;

	private PostModelDAO _postModelDAO;
	private GetModelDAO _getModelDAO;
	private Map<String, ImeiModelDAO> _imeiModelDAOMap = new ConcurrentHashMap<String, ImeiModelDAO>();

	private static final Logger _logger = Logger.getLogger(DBConnection.class.getName());
}
