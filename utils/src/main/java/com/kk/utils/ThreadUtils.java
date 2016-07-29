package com.kk.utils;


public class ThreadUtils {
	
	public static void sleep(final long millis) {
		if (millis <= 0) {
			return;
		}
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {}
	}
}
