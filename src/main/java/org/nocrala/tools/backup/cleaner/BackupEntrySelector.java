package org.nocrala.tools.backup.cleaner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackupEntrySelector {

  public static void markEntriesToKeep(List<BackupEntry> entries, Date date) {

    if (entries == null || entries.isEmpty()) {
      return;
    }

    // 1. Always keep the last entry

    entries.get(entries.size() - 1).markKeep();

    // 2. Keep all entries of last 7 days

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_MONTH, -7);
    Date sevenDaysAgo = cal.getTime();

    for (BackupEntry e : entries) {
      if (!e.getDate().before(sevenDaysAgo)) {
        e.markKeep();
      }
    }

    // 3. Keep week's last entry for the last 4 weeks.

    cal.setTime(date);
    cal.add(Calendar.DAY_OF_MONTH, -28);
    Date fourWeeksAgo = cal.getTime();

    Map<Long, BackupEntry> weekEntries = new HashMap<Long, BackupEntry>();
    for (BackupEntry e : entries) {
      if (!e.getDate().before(fourWeeksAgo)) {
        WeekTime w = new WeekTime(e.getDate());
        BackupEntry best = weekEntries.get(w.count());
        if (best == null || new WeekTime(best.getDate()).ticks() < w.ticks()) {
          weekEntries.put(w.count(), e);
        }
      }
    }

    for (BackupEntry e : weekEntries.values()) {
      e.markKeep();
    }

    // 4. Keep month's last entry for ever.

    Map<Long, BackupEntry> monthEntries = new HashMap<Long, BackupEntry>();
    for (BackupEntry e : entries) {
      MonthTime m = new MonthTime(e.getDate());
      BackupEntry best = monthEntries.get(m.count());
      if (best == null || new MonthTime(best.getDate()).ticks() < m.ticks()) {
        monthEntries.put(m.count(), e);
      }
    }

    for (BackupEntry e : monthEntries.values()) {
      e.markKeep();
    }

  }

}
