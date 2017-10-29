package vn.bakasta.geocoder.exceptions;

public class GeoCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GeoCodeException() {
		super();
	}

	public GeoCodeException(String message) {
		super(message);
	}

	public GeoCodeException(String message, int errorCode) {
		super(String.format("HTTP error: %d \n Message: %s", errorCode, message));
	}

	public GeoCodeException(Throwable cause) {
		super(cause);
	}

	public GeoCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeoCodeException(String message, int errorCode, Throwable cause) {
		super(String.format("HTTP error: %d \n Message: %s", errorCode, message), cause);
	}
}
