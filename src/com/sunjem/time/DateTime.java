package com.sunjem.time;

public class DateTime {
	long timestamp;
	
	protected static int START_YEAR = 1970;
	protected static byte START_MONTH = 1;
	protected static byte START_DAY = 1;
	protected static Byte [] DAYS_IN_MONTHS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	protected byte seconds;
	protected byte minutes;
	protected byte hours;
	protected byte days;
	protected byte months;
	protected int years;
	
	public DateTime(int years, byte months, byte days) {
		this.years = years - START_YEAR;
		this.months = months;
		this.days = days;
		generateTimestamp();
	}
	
	public DateTime(int years, byte months, byte days, byte hours, byte minutes, byte seconds) {
		this.years = years - START_YEAR;
		this.months = months;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		generateTimestamp();
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		parseTimestamp();
	}

	public byte getSeconds() {
		return seconds;
	}

	public void setSeconds(byte seconds) {
		this.seconds = seconds;
		generateTimestamp();
	}

	public byte getMinutes() {
		return minutes;
	}

	public void setMinutes(byte minutes) {
		this.minutes = minutes;
		generateTimestamp();
	}

	public byte getHours() {
		return hours;
	}

	public void setHours(byte hours) {
		this.hours = hours;
		generateTimestamp();
	}

	public byte getDays() {
		return days;
	}

	public void setDays(byte days) {
		this.days = days;
		generateTimestamp();
	}

	public byte getMonths() {
		return months;
	}

	public void setMonths(byte months) {
		this.months = months;
		generateTimestamp();
	}

	public DateTime(long timestamp) {
		this.timestamp = timestamp;
		parseTimestamp();
	}
	
	@Override
	public String toString() {
		return "DateTime [timestamp=" + timestamp + ", seconds=" + seconds + ", minutes=" + minutes + ", hours=" + hours
				+ ", days=" + days + ", months=" + months + ", years=" + this.getYears() + "]";
	}
	
	public static boolean isBissextile(int realYear) {
		if(realYear % 4 != 0) {
			return false;
		}
		if((realYear%100 == 0) && (realYear%400 != 0)) {
			return false;
		}
		return true;
	}
	
	public int getYears() {
		return getRealYears(years);
	}
	protected int getRealYears(int year) {
		return year + START_YEAR;
	}
	public void setYears(int years) {
		this.years = years - START_YEAR;
		generateTimestamp();
	}
	
	protected void generateTimestamp() {
		int d = days;
		for(int y = 0; y < years; y++) {
			if(isBissextile(getRealYears(y))) {
				d += 1;
			}
			d += 365;
		}
		for(byte m = 0; m < months - START_MONTH; m++) {
			if(isBissextile(getRealYears(years)) && m == 1) {
				d += 1;
			}
			d += DAYS_IN_MONTHS[m];
		}
		d -= START_DAY;
		timestamp = ((d*24 + hours)*60 + minutes)*60 + seconds;
		parseTimestamp();
	}
	
	protected void parseTimestamp() {
		double minutes = Math.floor(timestamp/60);
		this.seconds = (byte) (timestamp - minutes*60);
	
		double hours = Math.floor(minutes/60);
		this.minutes = (byte) (minutes - hours*60);
		
		double days = Math.floor(hours/24);
		this.hours = (byte) (hours - days*24);
		
		int daysYearCounter = 0;
		int daysYearCounterPrev = 0;
		int years = 0;
		while(true) {
			daysYearCounterPrev = daysYearCounter;

			if (DateTime.isBissextile(getRealYears(years))) {
				daysYearCounter += 1;
			}
			
			daysYearCounter += 365;
			if(daysYearCounter >= days) {
				daysYearCounter = daysYearCounterPrev;
				break;
			}
			years++;
		}
		this.years = years;
		int dayOfYear = (int) (days - daysYearCounter);
		
		int daysMonthCounter = 0;
		int	daysMonthCounterPrev = 0;
		byte months = 0;
		while(true) {
			daysMonthCounterPrev = daysMonthCounter;
			if (DateTime.isBissextile(getRealYears(years)) && months == 1) {
				daysMonthCounter += 1;
			}
			daysMonthCounter += DAYS_IN_MONTHS[months];
			if(daysMonthCounter >= dayOfYear) {
				daysMonthCounter = daysMonthCounterPrev;
				break;
			}
			months++;
		}
		this.months = (byte) (months + START_MONTH);
		this.days = (byte) (dayOfYear - daysMonthCounter + START_DAY);
	}
}
