package com.kk.utils.date.u;

import java.sql.Timestamp;

public class TimeConstant {
    public static final int       SEC_MILLUS  = 1000;
    public static final int       MIN_SEC     = 60;
    public static final int       MIN_MILLUS  = MIN_SEC * SEC_MILLUS;
    public static final int       HOUR_MIN    = 60;
    public static final int       HOUR_SEC    = HOUR_MIN * MIN_SEC;
    public static final int       HOUR_MILLUS = HOUR_SEC * SEC_MILLUS;
    public static final int       DAY_HOUR    = 24;
    public static final int       DAY_MIN     = DAY_HOUR * HOUR_MIN;
    public static final int       DAY_SEC     = DAY_MIN * MIN_SEC;
    public static final int       DAY_MILLUS  = DAY_SEC * SEC_MILLUS;

    public static final Timestamp TIME_1970   = new Timestamp(1);
    public static final Timestamp TIME_2000   = TimeUtils.FetchTime.create(2000, 1, 1);
    public static final Timestamp TIME_2005   = TimeUtils.FetchTime.create(2005, 1, 1);
    public static final Timestamp TIME_2010   = TimeUtils.FetchTime.create(2010, 1, 1);

}
