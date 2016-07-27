package com.kk.utils.date.u;

import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class TimeHandleUtils {

    public static class OPERATE {
        public static int diffDays(Date dateLeft, Date dateRight) {
            return TimeFetchUtils.INT.days(dateLeft) - TimeFetchUtils.INT.days(dateRight);
        }

        public static int diffHours(Date dateLeft, Date dateRight) {
            return TimeFetchUtils.INT.hours(dateLeft) - TimeFetchUtils.INT.hours(dateRight);
        }

        public static long diffMinutes(Date dateLeft, Date dateRight) {
            return TimeFetchUtils.INT.minutes(dateLeft) - TimeFetchUtils.INT.minutes(dateRight);
        }

        @SuppressWarnings("deprecation")
        public static <T extends Date> T addYear(T date, int num) {
            date.setYear(date.getYear() + num);
            return date;
        }

        @SuppressWarnings("deprecation")
        public static <T extends Date> T addMonth(T date, int num) {
            date.setMonth(date.getMonth() + num);
            return date;
        }

        @SuppressWarnings("deprecation")
        public static <T extends Date> T addDate(T date, int num) {
            date.setDate(date.getDate() + num);
            return date;
        }

        @SuppressWarnings("deprecation")
        public static <T extends Date> T addHour(T date, int num) {
            date.setHours(date.getHours() + num);
            return date;
        }

        @SuppressWarnings("deprecation")
        public static <T extends Date> T subHour(T date, int num) {
            date.setHours(date.getHours() - num);
            return date;
        }

        @SuppressWarnings("deprecation")
        public static <T extends Date> T subMinute(T date, int num) {
            date.setMinutes(date.getMinutes() - num);
            return date;
        }

        @SuppressWarnings("deprecation")
        public static <T extends Date> T subDate(T date, int num) {
            date.setDate(date.getDate() - num);
            return date;
        }
    }

    public static class FLOOR {
        @SuppressWarnings({"deprecation", "unchecked"})
        public static <T extends Date> T floorTo(final T date, final int field) {
            if (date == null) {
                return null;
            }
            Class<?> clazz = date.getClass();
            T res = null;
            if (clazz == Date.class) {
                res = (T) new Date(date.getTime());
            } else if (clazz == Timestamp.class) {
                res = (T) new Timestamp(date.getTime());
            } else {
                try {
                    Constructor<?> ctor;
                    try {
                        ctor = clazz.getConstructor();
                        res = (T) clazz.newInstance();
                        res.setTime(date.getTime());
                    } catch (Exception e) {
                        try {
                            ctor = clazz.getConstructor(long.class);
                            ctor.newInstance(date.getTime());
                        } catch (Exception e1) {
                        }
                    }
                } catch (Exception e) {
                }
            }
            if (res == null) {
                res = date;
            }
            switch (field) {
                case Calendar.YEAR:
                    res.setMonth(0);
                    //$FALL-THROUGH$
                case Calendar.MONTH:
                    res.setDate(1);
                    //$FALL-THROUGH$
                case Calendar.DATE:
                    res.setHours(0);
                    //$FALL-THROUGH$
                case Calendar.HOUR:
                    res.setMinutes(0);
                    //$FALL-THROUGH$
                case Calendar.MINUTE:
                    res.setSeconds(0);
                    //$FALL-THROUGH$
                case Calendar.SECOND:
                case Calendar.MILLISECOND:
                    res.setTime((res.getTime() / 1000) * 1000);
                    break;
            }
            return res;
        }

        public static <T extends Date> T floorToYear(final T date) {
            return floorTo(date, Calendar.YEAR);
        }

        public static <T extends Date> T floorToMonth(final T date) {
            return floorTo(date, Calendar.MONTH);
        }

        public static <T extends Date> T floorToDate(final T date) {
            return floorTo(date, Calendar.DATE);
        }

        public static <T extends Date> T floorToHour(final T date) {
            return floorTo(date, Calendar.HOUR);
        }

        public static <T extends Date> T floorToMin(final T date) {
            return floorTo(date, Calendar.MINUTE);
        }

        public static <T extends Date> T floorToSec(final T date) {
            return floorTo(date, Calendar.SECOND);
        }
    }

    public static class CEIL {

        @SuppressWarnings({"deprecation", "unchecked"})
        public static <T extends Date> T ceilTo(final T date, final int field) {
            if (date == null) {
                return null;
            }
            Class<?> clazz = date.getClass();
            T res = null;
            if (clazz == Date.class) {
                res = (T) new Date(date.getTime());
            } else if (clazz == Timestamp.class) {
                res = (T) new Timestamp(date.getTime());
            } else {
                try {
                    Constructor<?> ctor;
                    try {
                        ctor = clazz.getConstructor();
                        res = (T) clazz.newInstance();
                        res.setTime(date.getTime());
                    } catch (Exception e) {
                        try {
                            ctor = clazz.getConstructor(long.class);
                            ctor.newInstance(date.getTime());
                        } catch (Exception e1) {
                        }
                    }
                } catch (Exception e) {
                }
            }
            if (res == null) {
                res = date;
            }
            switch (field) {
                case Calendar.YEAR:
                    res.setMonth(0);
                    //$FALL-THROUGH$
                case Calendar.MONTH:
                    res.setDate(1);
                    //$FALL-THROUGH$
                case Calendar.DATE:
                    res.setHours(0);
                    //$FALL-THROUGH$
                case Calendar.HOUR:
                    res.setMinutes(0);
                    //$FALL-THROUGH$
                case Calendar.MINUTE:
                    res.setSeconds(0);
                    //$FALL-THROUGH$
                case Calendar.SECOND:
                case Calendar.MILLISECOND:
                    res.setTime((res.getTime() / 1000) * 1000);
                    break;
            }
            switch (field) {
                case Calendar.YEAR:
                    res.setYear(res.getYear() + 1);
                    break;
                case Calendar.MONTH:
                    res.setMonth(res.getMonth() + 1);
                    break;
                case Calendar.DATE:
                    res.setDate(res.getDate() + 1);
                    break;
                case Calendar.HOUR:
                    res.setHours(res.getHours() + 1);
                    break;
                case Calendar.MINUTE:
                    res.setMinutes(res.getMinutes() + 1);
                    break;
                case Calendar.SECOND:
                    res.setSeconds(res.getSeconds() + 1);
                    break;
                case Calendar.MILLISECOND:
                    break;
            }
            return res;
        }

        public static <T extends Date> T ceilToYear(final T date) {
            return ceilTo(date, Calendar.YEAR);
        }

        public static <T extends Date> T ceilToMonth(final T date) {
            return ceilTo(date, Calendar.MONTH);
        }

        public static <T extends Date> T ceilToDate(final T date) {
            return ceilTo(date, Calendar.DATE);
        }

        public static <T extends Date> T ceilToHour(final T date) {
            return ceilTo(date, Calendar.HOUR);
        }

        public static <T extends Date> T ceilToMin(final T date) {
            return ceilTo(date, Calendar.MINUTE);
        }

        public static <T extends Date> T ceilToSec(final T date) {
            return ceilTo(date, Calendar.SECOND);
        }

    }

    public static void main(final String[] args) {
        final Timestamp timestamp = Timestamp.valueOf("2000-02-29 10:11:12.2");
        System.out.println(FLOOR.floorToDate(timestamp));
        System.out.println(FLOOR.floorToHour(timestamp));
        System.out.println(FLOOR.floorToMin(timestamp));
        System.out.println(FLOOR.floorToSec(timestamp));
        System.out.println("------");
        System.out.println(CEIL.ceilToDate(timestamp));
        System.out.println(CEIL.ceilToHour(timestamp));
        System.out.println(CEIL.ceilToMin(timestamp));
        System.out.println(CEIL.ceilToSec(timestamp));
    }
}
