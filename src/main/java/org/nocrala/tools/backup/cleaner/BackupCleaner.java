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

    BackupEntrySelector.markEntriesToKeep(entries, new Date());

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

}
