package com.xerago.rsa.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortalDateConversion {

	private static final Logger LOGGER = LogManager.getRootLogger();

	private PortalDateConversion() {

	}

	public static List<Integer> calcAgeWithInceptionDate(Date to, Date today) {
		List<Integer> integers = new ArrayList<Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			if (to != null && today != null) {
				LOGGER.info("to::: " + sdf.format(to) + " ::: today::: " + sdf.format(today));
				LocalDate localDateTo = LocalDateTime.ofInstant(to.toInstant(), ZoneId.systemDefault()).toLocalDate();
				LocalDate localDatetoday = LocalDateTime.ofInstant(today.toInstant(), ZoneId.systemDefault())
						.toLocalDate();
				Period p = Period.between(localDateTo, localDatetoday);
				LOGGER.info("yearsBetween ::: " + p.getYears());
				integers.add(p.getYears());
				integers.add(p.getMonths());
				integers.add(p.getDays());
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return integers;
	}

	public static Integer daysBetween(Date from, Date to) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			LOGGER.info("from::: " + sdf.format(from) + " ::: to::: " + sdf.format(to));
			LocalDate localDateFrom = LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault()).toLocalDate();
			LocalDate localDateTo = LocalDateTime.ofInstant(to.toInstant(), ZoneId.systemDefault()).toLocalDate();
			if (localDateFrom.isBefore(localDateTo) || localDateFrom.equals(localDateTo)) {
				long overalldays = ChronoUnit.DAYS.between(localDateFrom, localDateTo) + 1L;
				LOGGER.info("daysBetween ::: " + overalldays);
				return (int) (overalldays);
			} else {
				throw new IllegalArgumentException("The from date is before of to date");
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
	}

	public static Integer daysBetweenAge(Date from, Date to) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			LOGGER.info("from::: " + sdf.format(from) + " ::: to::: " + sdf.format(to));
			LocalDate localDateFrom = LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault()).toLocalDate();
			LocalDate localDateTo = LocalDateTime.ofInstant(to.toInstant(), ZoneId.systemDefault()).toLocalDate();
			if (localDateFrom.isBefore(localDateTo) || localDateFrom.equals(localDateTo)) {
				long overalldays = ChronoUnit.DAYS.between(localDateFrom, localDateTo);
				LOGGER.info("daysBetweenAge ::: " + overalldays);
				return (int) (overalldays);
			} else {
				throw new IllegalArgumentException("The from date is before of to date");
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
	}

	public static Integer yearsBetweenAge(Date from, Date to) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			LOGGER.info("from::: " + sdf.format(from) + " ::: to::: " + sdf.format(to));
			LocalDate localDateFrom = LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault()).toLocalDate();
			LocalDate localDateTo = LocalDateTime.ofInstant(to.toInstant(), ZoneId.systemDefault()).toLocalDate();
			if (localDateFrom.isBefore(localDateTo) || localDateFrom.equals(localDateTo)) {
				long overalldays = ChronoUnit.YEARS.between(localDateFrom, localDateTo);
				LOGGER.info("yearsBetweenAge ::: " + overalldays);
				return (int) (overalldays);
			} else {
				throw new IllegalArgumentException("The from date is before of to date");
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
	}

	public static List<Integer> calcAgeWithInceptionDate(LocalDate localDate1, LocalDate today) {
		List<Integer> integers = new ArrayList<Integer>();
		try {
			Period p = Period.between(localDate1, today);
			integers.add(p.getYears());
			integers.add(p.getMonths());
			integers.add(p.getDays());
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return integers;
	}

	public static String getXmlCalObject(Date date) {
		SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			return objSDF.format(date);
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
	}

	public static long diffDates(String start, String end) {

		LOGGER.info("start:: " + start);
		LOGGER.info("end::: " + end);

		long lnDiffInDays = 0;
		String dtEnd = "";
		try {
			if (end != null) {
				dtEnd = StringUtils.trim(end);
			}
			if (!Constants.DATE_DEFAULT.equalsIgnoreCase(start) && !Constants.DATE_DEFAULT.equalsIgnoreCase(dtEnd)) {
				DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date d1 = dateFormat.parse(start);
				Date d2 = dateFormat.parse(dtEnd);

				Calendar calStart = Calendar.getInstance();
				calStart.setTime(d1);
				Calendar calEnd = Calendar.getInstance();
				calEnd.setTime(d2);
				Calendar d = Calendar.getInstance();
				d.setTimeInMillis(calEnd.getTimeInMillis() - calStart.getTimeInMillis());
				lnDiffInDays = d.getTime().getTime() / 86400000;
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		LOGGER.info("lnDiffInDays::: " + lnDiffInDays);
		return lnDiffInDays;
	}

	public static Calendar getXMLCalObj(String input) {
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			Date inputDate = (Date) sdf.parse(input);
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			return cal;
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
	}

	public static Calendar getXMLCalObj(Date input) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(input);
			return cal;
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
	}

}
