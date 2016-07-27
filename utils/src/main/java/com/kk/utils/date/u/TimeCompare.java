package com.kk.utils.date.u;

import java.util.Date;


public class TimeCompare {
	public static boolean eqYear(Date left, Date right) {
		return left == right || left.getYear() == right.getYear();
	}

	public static boolean eqMonth(Date left, Date right) {
		return left == right || left.getMonth() == right.getMonth();
	}

	public static boolean eqYearMonth(Date left, Date right) {
		return left == right || eqYear(left, right) && eqMonth(left, right);
	}

	public static boolean eqYearMonthDate(Date left, Date right) {
		return left == right || TimeFetchUtils.INT.days(left) == TimeFetchUtils.INT.days(right);
	}

	public static boolean isToday(Date date) {
		return TimeFetchUtils.INT.days(date) == TimeFetchUtils.INT.nowDays();
	}

	public static boolean isSameDay(Date left, Date right) {
		return TimeFetchUtils.INT.days(left) == TimeFetchUtils.INT.days(right);
	}
}
