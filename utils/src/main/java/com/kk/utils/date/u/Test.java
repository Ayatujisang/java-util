package com.kk.utils.date.u;

import com.kk.utils.date.DateUtil;
import com.kk.utils.log.ConsoleLogger;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Test {
    static ConsoleLogger logger = new ConsoleLogger();

    public static void main(String[] args) {
        Date date = new Date();
        Date yes = new Date(date.getTime() - TimeConstant.DAY_MILLUS);

        // unix时间 对应的天数
        logger.info(TimeFetchUtils.INT.days(date));
        // unix时间 对应的小时数
        logger.info(TimeFetchUtils.INT.hours(date));

        // 两个时间相差的天数
        logger.info(TimeHandleUtils.OPERATE.diffDays(yes, date)); // yes-date
        logger.info(DateUtil.daysBetween(yes, date)); // date-yes

        // 两个时间相差的小时
        logger.info(TimeHandleUtils.OPERATE.diffHours(yes, new Date()));

        // 时间相加天数
        logger.info(TimeHandleUtils.OPERATE.addDate(new Date(), 1).toLocaleString());
        logger.info(DateUtil.addDay(new Date(), 1).toLocaleString());
        logger.info(DateUtils.addDays(new Date(), 1).toLocaleString());

        // 时间相加小时
        logger.info(TimeHandleUtils.OPERATE.addHour(new Date(), 1).toLocaleString());

        // 时间相减天数
        logger.info(TimeHandleUtils.OPERATE.addDate(new Date(), -100).toLocaleString());
        logger.info(TimeHandleUtils.OPERATE.subDate(new Date(), 100).toLocaleString());

        // 获取当前天， 小时等归零
        logger.info(TimeHandleUtils.FLOOR.floorToDate(new Date()).toLocaleString());

        // 获取当前 天、小时， 分钟等归零
        logger.info(TimeHandleUtils.FLOOR.floorToHour(new Date()).toLocaleString());

        // 获取当前 年， 月 日等归零
        logger.info(TimeHandleUtils.FLOOR.floorToYear(new Date()).toLocaleString());

        // 获取当前天+1， 小时等归零
        logger.info(TimeHandleUtils.CEIL.ceilToDate(new Date()).toLocaleString());

        // 获取当前 天、小时+1， 分钟等归零
        logger.info(TimeHandleUtils.CEIL.ceilToHour(new Date()).toLocaleString());

        // 获取昨天 ， 小时等归零
        logger.info(TimeFetchUtils.DATE.yestoday().toLocaleString());
        // 获取明天 ， 小时等归零
        logger.info(TimeFetchUtils.DATE.tomorrow().toLocaleString());
        // 获取月初
        logger.info(TimeFetchUtils.DATE.thisMonth().toLocaleString());

        //判断 日期 是否是同一天
        logger.info(TimeCompare.eqYearMonthDate(yes, new Date()));
        //判断 日期 是否是同一年同一月
        logger.info(TimeCompare.eqYearMonth(yes, new Date()));

        // 时间格式化
        logger.info(TimeFormatUtils.dateTime(new Date()));
        logger.info(DateUtil.defaultTime(new Date()));
    }
}
