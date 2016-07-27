package com.kk.utils.date.u;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

public class TimeParseUtils {

    public static final String[] SIMPLE_TIME_PATTERNS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS" ,"yyyy-MM-dd HH:mm:ss SSS", "yyyy-MM-dd HH",
                                                      "yyyy-MM-dd H:m:s" };

    public static class DATE {
        public static Date parse(final String str, final String... parsePatterns) {
            String dateStr = StringUtils.trimToNull(str);
            if (dateStr == null || parsePatterns == null) {
                return null;
            }
            try {
                return DateUtils.parseDate(str, parsePatterns);
            } catch (final ParseException e) {
                return null;
            }
        }

        public static Date parseWithExceptions(final String str, final String... parsePatterns) throws ParseException {
            String dateStr = StringUtils.trimToNull(str);
            if (dateStr == null || parsePatterns == null) {
                return null;
            }
            return DateUtils.parseDate(str, parsePatterns);
        }

        public static Date parse(final String str) {
            return parse(str, SIMPLE_TIME_PATTERNS);
        }

        public static Date parseWithExceptions(final String str) throws ParseException {
            return parseWithExceptions(str, SIMPLE_TIME_PATTERNS);
        }
    }

    public static class TIMESTAMP {
        public static Timestamp parse(final String str, final String[] parsePatterns) {
            final Date date = DATE.parse(str, parsePatterns);
            if (date == null) {
                return null;
            }
            return TimeConvertUtils.toTimestamp(date);
        }

        public static Date parseWithExceptions(final String str, final String[] parsePatterns) throws ParseException {
            final Date date = DATE.parseWithExceptions(str, parsePatterns);
            if (date == null) {
                return null;
            }
            return TimeConvertUtils.toTimestamp(date);
        }

        public static Timestamp parse(final String str) {
            return parse(str, SIMPLE_TIME_PATTERNS);
        }

        public static Date parseWithExceptions(final String str) throws ParseException {
            return parseWithExceptions(str, SIMPLE_TIME_PATTERNS);
        }
    }
}
