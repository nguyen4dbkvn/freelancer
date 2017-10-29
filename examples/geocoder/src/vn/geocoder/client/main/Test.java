package vn.geocoder.client.main;

import vn.geocoder.client.GeoCodeAPIHelper;
import vn.geocoder.exception.GeoCodeException;

public class Test {

	public static void main(String[] args) {

		double[][] points = {
			{10.837519070738601, 107.23634719848633},
		    {21.117851491216644, 106.39501333236694},
		    {21.0042788, 105.8437013},
		    {10.7792614, 106.6952926},
		    {21.03158, 105.8012636},
		    {11.5193284, 107.8102863},
		    {9.6852982, 104.336686},
		    {20.7955886, 106.9379506},
		    {23.3581158, 105.2791171},
		    {22.8555748, 106.714023},
		    {17.4544544, 106.5359014}
		};

		try {
			for (int i=0; i<11; i++) {
				double[] point = points[i];

				GeoCodeAPIHelper.getAddress(point[0], point[1]);
			}
		}
		catch (GeoCodeException e) {
			e.printStackTrace();
		}
		finally {
			GeoCodeAPIHelper.release();
		}
	}

}
