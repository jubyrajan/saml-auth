package org.jr.samlauth.sp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * Utility methods 
 * @author Juby Rajan
 */
public class Utils {
	/**
	 * Checks if a given string has length greater than zero
	 * @param str The string to be checked
	 * @return true of the string has a length greater than zero, false otherwise
	 */
	public static boolean hasLength(String str) {
		return str != null && str.length() > 0;
	}

	/**
	 * Checks if a given string has any text ignoring whitespace
	 * @param str The string to be checked
	 * @return true of the string has any text ignoring whitespace, false otherwise
	 */
	public static boolean hasText(String str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Converts and returns Throwable object's stack trace to string
	 * @param t the Throwable object
	 * @return Throwable object's stack trace to string
	 */
	public static String throwableTostackTraceString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	/**
	 * Logs to console
	 * @param clazz The class from which the log is triggered, optional
	 * @param log The log statement
	 * @param t Throwable object if any
	 */
	public static void consoleLog(Class<?> clazz, String log, Throwable t) {
		StringBuilder sb = new StringBuilder(new Date().toString());
		if (clazz != null) {
			sb.append(" ").append(clazz);
		}
		sb.append(": ").append(log);
		if (t != null) {
			sb.append(" ").append(throwableTostackTraceString(t));
		}
		System.out.println(sb.toString());
	}

	/**
	 * Logs to console
	 * @param log The log statement
	 */
	public static void consoleLog(String log) {
		consoleLog(null, log, null);
	}

	/**
	 * Logs to console
	 * @param log The log statement
	 * @param t Throwable object if any
	 */
	public static void consoleLog(String log, Throwable t) {
		consoleLog(null, log, t);
	}
}
