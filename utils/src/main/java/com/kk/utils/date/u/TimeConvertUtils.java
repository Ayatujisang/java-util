package com.kk.utils.date.u;

import java.sql.Timestamp;
import java.util.Date;


public class TimeConvertUtils {
    public static Date toUtilDate(java.sql.Date sqlDate) {
        return sqlDate == null ? null : new Date(sqlDate.getTime());
    }

    public static java.sql.Date toSqlDate(Date utilDate) {
        return utilDate == null ? null : new java.sql.Date(utilDate.getTime());
    }

    public static Timestamp toTimestamp(Date utilDate) {
        return new Timestamp(utilDate.getTime());
    }
}
