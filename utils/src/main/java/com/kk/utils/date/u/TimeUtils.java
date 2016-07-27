package com.kk.utils.date.u;



public class TimeUtils {
    public static class Constant extends TimeConstant{}
    public static class Compare extends TimeCompare{}
    public static class Convert extends TimeConvertUtils{}
    public static class FetchInt  extends TimeFetchUtils.INT{}
    public static class FetchTime  extends TimeFetchUtils.TIMESTAMP{}
    public static class Format extends TimeFormatUtils{}
    public static class Floor extends TimeHandleUtils.FLOOR{}
    public static class Ceil extends TimeHandleUtils.CEIL{}
    public static class Operate extends TimeHandleUtils.OPERATE{ }
    public static class ParseDate extends TimeParseUtils.DATE{}
    public static class ParseTime extends TimeParseUtils.TIMESTAMP{}
}
