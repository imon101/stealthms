package stealthms.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter {
	public String formatCurrentDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("EET"));
		cal.setTime(new Date());
		String result = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "."
				+ prefixWithZero(Integer.toString(cal.get(Calendar.MONTH) + 1))
				+ "." + Integer.toString(cal.get(Calendar.YEAR)) + " "
				+ Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + ":"
				+ prefixWithZero(Integer.toString(cal.get(Calendar.MINUTE)));
		return result;
	}

	public String prefixWithZero(String text) {
		if (text.length() < 2) {
			return "0" + text;
		}
		return text;
	}
}
