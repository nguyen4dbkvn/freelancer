package vn.bakastar.dao.model;

import java.text.ParseException;
import java.util.List;

import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.ImeiEntry;

public interface ImeiModelDAO extends ModelDAO {

	public List<ImeiEntry> list() throws DAOException;

	public void create(ImeiEntry imeiEntry) throws DAOException, ParseException;
}
