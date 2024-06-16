package com.example.quily.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CommonUtil {
	public static String getCurrentDateTimeInFormat() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma 'on' dd MMMM yyyy", Locale.ENGLISH);
		return LocalDateTime.now().format(formatter);
	}

	public static final String emailPatternRegex = "^[\\w.%+-]+@[A-Za-z0-9.-]+\\.com$";

}
