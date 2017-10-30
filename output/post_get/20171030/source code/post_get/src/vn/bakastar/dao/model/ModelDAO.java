package vn.bakastar.dao.model;

import java.util.List;

import vn.bakastar.exceptions.DAOException;

public interface ModelDAO {

	public int count();

	public List<?> list(int limit) throws DAOException;

	public void delete(long primaryKey) throws DAOException;
}
