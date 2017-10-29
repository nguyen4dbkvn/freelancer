package vn.bakastar.dao.model;

import java.util.List;

import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.PostEntry;

public interface PostModelDAO extends ModelDAO {

	public List<PostEntry> list() throws DAOException;

	public void create(PostEntry postEntry) throws DAOException;

	public void delete(PostEntry postEntry) throws DAOException;
}
