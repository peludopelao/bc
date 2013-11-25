package org.nocrala.tools.backup.cleaner;

import java.util.Calendar;
import java.util.Date;

public class MonthTime {

  private Date date;

  private long count;
  private long ticks;

  public MonthTime(Date date) {
    this.date = date;
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    Calendar monthBegin = Calendar.getInstance();
    monthBegin.clear();
    monthBegin.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);

    this.count = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
    this.ticks = cal.getTimeInMillis() - monthBegin.getTimeInMillis();
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
