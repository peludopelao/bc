package org.nocrala.tools.backup.cleaner;

import java.io.File;
import java.util.Date;

public class BackupEntry implements Comparable<BackupEntry> {

  private Date date;
  private File f;
  private boolean keep;

  public BackupEntry(Date date, File f, boolean keep) {
    this.date = date;
    this.f = f;
    this.keep = keep;
  }

  @Override
  public int compareTo(BackupEntry other) {
    return this.date.compareTo(other.date);
  }

  // Accessors

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public File getF() {
    return f;
  }

  public void setF(File f) {
    this.f = f;
  }

  public boolean isKeep() {
    return keep;
  }

  public void markKeep() {
    this.keep = true;
  }

}
