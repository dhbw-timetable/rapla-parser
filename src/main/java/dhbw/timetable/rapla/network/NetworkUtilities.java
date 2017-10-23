package dhbw.timetable.rapla.network;

import java.net.URL;

/**
 * Created by Hendrik Ulbrich (C) 2017
 */
public final class NetworkUtilities {

	private final static String PROTOCOL_REGEX = "(HTTPS|https)://";

	private NetworkUtilities() {}

    /**
     * Try to establish an URL internet connection.
     *
     * @param url The url to check
     * @return true if the connection is open, false if an error occured.
     */
	public static boolean TestConnection(String url) {
		try {
			new URL(url).openConnection();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

    /**
     * Checks if a given url matches a specific pattern of a valid rapla url
     *
     * @param url The url to validate
     * @return true if the pattern matched, false if not
     */
	public static boolean URLIsValid(String url) {
		return url.matches(PROTOCOL_REGEX + "rapla\\.dhbw-" + BaseURL.getRegex() + "\\.de/rapla?(.+=.+)(&.+=.+)*");
	}

}
