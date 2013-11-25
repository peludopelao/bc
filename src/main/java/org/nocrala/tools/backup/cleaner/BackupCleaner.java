package org.nocrala.tools.backup.cleaner;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackupCleaner {

  private static final SimpleDateFormat DF = new SimpleDateFormat("yyyyMMdd");
  private static final SimpleDateFormat DTF = new SimpleDateFormat(
      "yyyyMMdd-HHmmss");

  public static void main(String[] args) {

    // 1. Get parameters

    File workDir = new File(args[0]);
    final String prefix = args[1];
    final String suffix = args[2];

    // 2. Get list of backup entries

    List<BackupEntry> entries = new ArrayList<BackupEntry>();
    for (File f : workDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.matches(prefix + "[0-9]{8}(\\-[0-9]{6})?" + suffix);
      }
    })) {
      String sd = f.getName().substring(prefix.length(),
          f.getName().length() - suffix.length());
      Date d;
      try {
        d = sd.length() == 8 ? DF.parse(sd) : DTF.parse(sd);
        entries.add(new BackupEntry(d, f, false));
      } catch (ParseException e) {
        System.out.println("Malformed name of file '" + f.getAbsolutePath()
            + "' -- skipped.");
      }
    }

    // 3. Mark the entries to keep

    markEntriesToKeep(entries);

    // 4. Remove the entries we won't keep

    for (BackupEntry e : entries) {
      if (!e.isKeep()) {
        if (!e.getF().delete()) {
          System.out.println("Could not delete '" + e.getF().getAbsolutePath()
              + "'.");
        }
      }
    }

  }

  private static void markEntriesToKeep(List<BackupEntry> entries) {

    if (entries == null || entries.isEmpty()) {
      return;
    }

    // 1. Always keep the last entry

    entries.get(entries.size() - 1).markKeep();

    // 2. Keep all entries of last 7 days

    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DAY_OF_MONTH, -7);
    Date sevenDaysAgo = cal.getTime();

    for (BackupEntry e : entries) {
      if (!e.getDate().before(sevenDaysAgo)) {
        e.markKeep();
      }
    }

    // 3. Keep week's last entry for the last 4 weeks.

    cal.setTime(new Date());
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
