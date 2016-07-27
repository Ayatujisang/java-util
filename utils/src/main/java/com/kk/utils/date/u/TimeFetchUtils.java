package com.kk.utils.date.u;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class TimeFetchUtils {

    public static class INT // 返回整型的
    {

        public static long now() {
            return System.currentTimeMillis();
        }

        public static long nowNano() {
            return System.nanoTime();
        }

        public static long nowSeconds() {
            return now() / 1000;
        }

        public static int nowDays() {
            return days(new Date());
        }

        public static int nowHours() {
            return hours(new Date());
        }

        public static long nowMinutes() {
            return minutes(new Date());
        }

        /**
         * @return 经过的天数（相对值，非绝对值）。 unix时间戳对应的时间
         */
        public static int days(final Date date) {
            Calendar calendar = Calendar.getInstance();
            return (int) ((date.getTime()) / TimeConstant.DAY_MILLUS);
        }

        /**
         * @return 经过的小时数（相对值，非绝对值）。
         */
        public static int hours(Date date) {
            return (int) (date.getTime() / TimeConstant.HOUR_MILLUS);
        }

        /**
         * @return 经过的分钟数（相对值，非绝对值）。
         */
        public static long minutes(Date date) {
            return (date.getTime() / TimeConstant.MIN_MILLUS);
        }

        /**
         * @return 今天剩下的秒数
         */
        public static int lastSecondsOfDay(final Date date) {
            return TimeConstant.DAY_SEC - pastSecondsOfDay(date);
        }

        /**
         * @return 今天过去的秒数
         */
        @SuppressWarnings("deprecation")
        public static int pastSecondsOfDay(final Date date) {
            return date.getHours() * TimeConstant.HOUR_SEC + date.getMinutes() * TimeConstant.MIN_SEC + date.getSeconds();
        }

    }

    public static class DATE {

        public static Date now() {
            return new Date();
        }

        public static Date today() {
            final Date now = now();
            return TimeHandleUtils.FLOOR.floorToDate(now);
        }

        @SuppressWarnings("deprecation")
        public static Date yestoday() {
            final Date res = today();
            res.setDate(res.getDate() - 1);
            return res;
        }

        @SuppressWarnings("deprecation")
        public static Date tomorrow() {
            final Date res = today();
            res.setDate(res.getDate() + 1);
            return res;
        }

        public static Date thisMonth() {
            final Date res = now(); // 今天
            return TimeHandleUtils.FLOOR.floorToMonth(res);// 到月初
        }

        @SuppressWarnings("deprecation")
        public static Date lastMonth() {
            final Date res = thisMonth();
            res.setMonth(res.getMonth() - 1);// 上个月
            return res;
        }
    }

    public static class TIMESTAMP {

        public static Timestamp now() {
            return TimeConvertUtils.toTimestamp(DATE.now());
        }

        public static Timestamp today() {
            return TimeConvertUtils.toTimestamp(DATE.today());
        }

        public static Timestamp yestoday() {
            return TimeConvertUtils.toTimestamp(DATE.yestoday());
        }

        public static Timestamp tomorrow() {
            return TimeConvertUtils.toTimestamp(DATE.tomorrow());
        }

        public static Timestamp thisMonth() {
            return TimeConvertUtils.toTimestamp(DATE.thisMonth());
        }

        public static Timestamp lastMonth() {
            return TimeConvertUtils.toTimestamp(DATE.lastMonth());
        }

        /**
         * 创建时间对象。
         *
         * @param year   平凡的年。
         * @param month  平凡的月,1-12
         * @param date   平凡的天,1-31
         * @param hour   平凡的时
         * @param minute 平凡的分
         * @param second 平凡的秒
         * @param millis 平凡的毫秒
         */
        @SuppressWarnings("deprecation")
        public static Timestamp create(int year, int month, int date, int hour, int minute, int second, int millis) {
            return new Timestamp(year - 1900, month - 1, date, hour, minute, second, millis * 1000);
        }

        /**
         * 创建时间对象。
         *
         * @param year  平凡的年。
         * @param month 平凡的月,1-12
         * @param date  平凡的天,1-31
         */
        public static Timestamp create(int year, int month, int date) {
            return create(year, month, date, 0, 0, 0, 0);
        }
    }

}
