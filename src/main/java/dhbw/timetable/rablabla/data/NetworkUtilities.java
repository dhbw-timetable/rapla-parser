package dhbw.timetable.rablabla.data;

import java.net.URL;

/**
 * Created by Hendrik Ulbrich (C) 2017
 */
public final class NetworkUtilities {

	private final static String PROTOCOL_REGEX = "(HTTPS|https)://";

	private NetworkUtilities() {}
	
	public static boolean TestConnection(String url) {
		try {
			new URL(url).openConnection();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean URLIsValid(String url) {
		return url.matches(PROTOCOL_REGEX + "rapla\\.dhbw-" + BaseURL.getRegex() + "\\.de/rapla?(.+=.+)(&.+=.+)*");
	}

}
