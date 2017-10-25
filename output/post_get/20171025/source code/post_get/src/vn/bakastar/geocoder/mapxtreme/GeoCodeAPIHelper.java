package vn.bakastar.geocoder.mapxtreme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.lang.StringEscapeUtils;

import vn.bakastar.exceptions.GeoCodeException;
import vn.bakastar.util.PropsValue;

public class GeoCodeAPIHelper {

	public static String getAddress(double latitude, double longitude) 
		throws GeoCodeException {

		String address = null;
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			//connect to reverse geocode server
			socket = new Socket(PropsValue.GEOCODE_API_HOST, PropsValue.GEOCODE_API_PORT);
			//socket.setKeepAlive(false);
			socket.setSoLinger(true, 0);
			socket.setReuseAddress(true);
			//socket.setTcpNoDelay(true);
			socket.setSoTimeout(1000);

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			//send and receive data
			out.println(longitude + " " + latitude);
			out.flush();

			String result = in.readLine();
			address = StringEscapeUtils.unescapeHtml(result);
		} 
		catch (IOException e) {
			throw new GeoCodeException(e);
		}
		finally {
			try {
				if (out != null) 
					out.close();

				if (in != null)
					in.close();

				if (socket != null) 
					socket.close();
			} 
			catch (IOException e) {
				throw new GeoCodeException(e);
			}
		}

		return address;
	}
}
