package vn.bakastar.exceptions;

public class GeoCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GeoCodeException() {
		super();
	}

	public GeoCodeException(String message) {
		super(message);
	}

	public GeoCodeException(Throwable cause) {
		super(cause);
	}

	public GeoCodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
