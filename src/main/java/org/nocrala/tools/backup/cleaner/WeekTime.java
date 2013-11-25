package org.nocrala.tools.backup.cleaner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WeekTime {

  private static final long WEEK_DURATION = 7L * 24 * 60 * 60 * 1000;

  private static final Date A_MONDAY = new GregorianCalendar(2013,
      Calendar.NOVEMBER, 25).getTime();

  private Date date;

  private long count;
  private long ticks;

  public WeekTime(Date date) {
    this.date = date;
    this.count = (date.getTime() - A_MONDAY.getTime()) / WEEK_DURATION;
    this.ticks = (date.getTime() - A_MONDAY.getTime()) % WEEK_DURATION;
  }

  public Date getDate() {
    return date;
  }

  public long count() {
    return count;
  }

  public long ticks() {
    return ticks;
  }

}
