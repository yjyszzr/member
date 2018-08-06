package com.dl.member.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtilNew {
	public static final DateTimeFormatter date_sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
	public static final DateTimeFormatter date_sdf_ch = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
	public static final DateTimeFormatter DTF_YYYYMMDDHH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
	public static final DateTimeFormatter time_sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	public static final DateTimeFormatter ymd_sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	public static final DateTimeFormatter yyyymmddhhmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public static final DateTimeFormatter short_time_sdf = DateTimeFormatter.ofPattern("HH:mm:ss");
	public static final DateTimeFormatter datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final String[] weekDays = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };

	public static String getYMD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		return sdf.format(date);
	}

	/**
	 * 时间是否在一周之内
	 * 
	 * @param addtime
	 * @param now
	 * @return
	 */
	public static boolean isLatestWeek(Date addtime, Date now) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(now);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -7); // 设置为7天前
		Date before7days = calendar.getTime(); // 得到7天前的时间
		if (before7days.getTime() < addtime.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取到截止时间为止的秒数，1970-01-01开始
	 *
	 * @param date
	 * @param hour
	 * @param min
	 * @param sec
	 * @return
	 */
	public static long getTimeLong(LocalDate date, int hour, int min, int sec) {
		LocalDateTime time = LocalDateTime.of(date, LocalTime.of(hour, min, sec));
		Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
		return instant.getEpochSecond();
	}

	/**
	 * 获取到截止时间为止的秒数，1970-01-01开始
	 *
	 * @return
	 */
	public static Integer getCurrentTimeLong() {
		LocalDateTime time = LocalDateTime.now();
		Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
		return Math.toIntExact(instant.getEpochSecond());
	}

	public static Integer getCurrentTimeLong(Long lepochSecond) {
		LocalDateTime time = LocalDateTime.ofEpochSecond(lepochSecond, 0, ZoneOffset.of("+08:00"));
		Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
		return Math.toIntExact(instant.getEpochSecond());
	}

	/**
	 * 根据格式获取，秒数，获取日期
	 *
	 * @param lepochSecond
	 * @param formatter
	 * @return
	 */
	public static String getCurrentTimeString(Long lepochSecond, DateTimeFormatter formatter) {
		LocalDateTime time = LocalDateTime.ofEpochSecond(lepochSecond, 0, ZoneOffset.of("+08:00"));
		return time.format(formatter);
	}

	/**
	 * 根据格式获取，秒数，获取日期
	 *
	 * @param lepochSecond
	 * @param formatter
	 * @return
	 */
	public static String getTimeString(Integer lepochSecond, DateTimeFormatter formatter) {
		LocalDateTime time = LocalDateTime.ofEpochSecond(lepochSecond, 0, ZoneOffset.of("+08:00"));
		return time.format(formatter);
	}

	/**
	 * 获取当前时间
	 *
	 * @param pattern
	 * @return
	 */
	public static String getCurrentTime(DateTimeFormatter pattern) {
		return getTime(LocalTime.now(), pattern);
	}

	/**
	 * @param now
	 * @param pattern
	 * @return
	 */
	public static String getTime(LocalTime now, DateTimeFormatter pattern) {
		return now.format(pattern);
	}

	/**
	 * 默认时间格式的当前时间 yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getCurrentDateTime() {
		return getCurrentDateTime(LocalDateTime.now(), datetimeFormat);
	}

	/**
	 * 获取年月日 yyyy-MM-dd
	 *
	 * @return
	 */
	public static String getCurrentYearMonthDay() {
		return getCurrentDateTime(LocalDateTime.now(), date_sdf);
	}

	/**
	 * 获取年月日 yyyyMMdd
	 *
	 * @return
	 */
	public static String getCurrentyyyyMMdd() {
		return getCurrentDateTime(LocalDateTime.now(), yyyyMMdd);
	}

	public static String getCurrentDateTime(LocalDateTime now, DateTimeFormatter pattern) {
		return now.format(pattern);
	}

	public static String getCurrentDate(DateTimeFormatter pattern) {
		return getDate(LocalDate.now(), pattern);
	}

	public static String getDate(LocalDate localDate, DateTimeFormatter pattern) {
		return localDate.format(pattern);
	}

	public static String getDate(LocalDate localDate, String pattern) {
		return getDate(localDate, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 计算日期时间差,使用默认格式"yyyy-MM-dd HH:mm:ss"
	 *
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return 时间差，格式为 X.xx/X小时h分
	 * @throws Exception
	 */
	public static String dateSubtractionHours(String start, String end) {
		return dateSubtractionHours(start, end, datetimeFormat);
	}

	/**
	 * 计算日期时间差
	 *
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @param format
	 *            日期格式
	 * @return
	 * @throws Exception
	 */
	public static String dateSubtractionHours(String start, String end, DateTimeFormatter format) {
		LocalDateTime start_time = LocalDateTime.parse(start, format);
		LocalDateTime end_time = LocalDateTime.parse(end, format);
		return dateSubtractionHours(start_time, end_time);
	}

	/**
	 * 计算日期时间差
	 *
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 * @throws Exception
	 */
	public static String dateSubtractionHours(LocalDateTime start, LocalDateTime end) {
		Duration d = Duration.between(start, end);
		StringBuilder subTime = new StringBuilder();
		subTime.append(Float.valueOf(d.toMinutes()) / 60).append("/");
		subTime.append(d.toHours() + "小时" + (d.toMinutes() % 60) + "分");
		return subTime.toString();
	}

	/**
	 * 计算与当前日期的时间差 单位天
	 * 
	 * @param lepochSecond
	 * @return
	 */
	public static long diffBetweenCurrentDay(long lepochSecond) {
		return diffBetween(LocalDateTime.now(), lepochSecond).toDays();
	}

	/**
	 * 计算与当前日期的时间差 单位分
	 * 
	 * @param lepochSecond
	 * @return
	 */
	public static long diffBetweenCurrentMin(long lepochSecond) {
		return diffBetween(LocalDateTime.now(), lepochSecond).toMinutes();
	}

	/**
	 * 计算与当前日期的时间差 单位小时
	 * 
	 * @param lepochSecond
	 * @return
	 */
	public static long diffBetweenCurrentHours(long lepochSecond) {
		return diffBetween(LocalDateTime.now(), lepochSecond).toHours();
	}

	/**
	 * 计算与当前日期的时间差
	 * 
	 * @param lepochSecond
	 * @return Duration
	 */
	public static Duration diffBetween(LocalDateTime dateTime, long lepochSecond) {
		LocalDateTime start = LocalDateTime.ofEpochSecond(lepochSecond, 0, ZoneOffset.of("+08:00"));
		return Duration.between(start, dateTime);
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @see
	 * @param dt
	 * @return 当前日期是星期几
	 */
	@Deprecated
	public static String getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 获取日期是星期几<br>
	 * 
	 * @param dateTime
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(LocalDateTime dateTime) {
		int weekDay = dateTime.getDayOfWeek().getValue();
		return weekDays[weekDay - 1];
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @return 当前日期是星期几
	 */
	public static String getCurrentWeekOfDate() {
		return getWeekOfDate(LocalDateTime.now());
	}

	public static final long getMilliSecondsByStr(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(dateStr).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0l;
	}

}
