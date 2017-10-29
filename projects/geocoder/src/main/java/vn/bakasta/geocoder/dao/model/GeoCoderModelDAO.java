package vn.bakasta.geocoder.dao.model;

import java.util.List;

import vn.bakasta.geocoder.exceptions.DAOException;
import vn.bakasta.geocoder.model.GeoCoder;

public interface GeoCoderModelDAO {

	public int count() throws DAOException;

	public List<GeoCoder> list(int limit) throws DAOException;

	public void update(long seqId, String address) throws DAOException;
}
