package vn.bakastar.dao.model;

import java.util.List;

import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.GetEntry;

public interface GetModelDAO extends ModelDAO {

	public List<GetEntry> list(int limit) throws DAOException;

	public void create(GetEntry getEntry) throws DAOException;

	public void delete(GetEntry getEntry) throws DAOException;
}
