/* 
 * Confidential - Oracle restricted
 */

package backupcleaner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.nocrala.tools.backup.cleaner.BackupEntry;
import org.nocrala.tools.backup.cleaner.BackupEntrySelector;

public class BackupEntrySelectorTests extends TestCase {

  public BackupEntrySelectorTests(final String txt) {
    super(txt);
  }

  public void testSimpleCase() throws ParseException {

    List<BackupEntry> entries = new ArrayList<BackupEntry>();
    entries.add(new BackupEntry(date("20131203"), null, false));
    entries.add(new BackupEntry(date("20131124"), null, false));
    BackupEntrySelector.markEntriesToKeep(entries, date("20131201"));
    checkMarks("tt", entries);
  }

  public void testTrickySelection() throws ParseException {

    List<BackupEntry> entries = new ArrayList<BackupEntry>();
    entries.add(new BackupEntry(date("20131203"), null, false));
    entries.add(new BackupEntry(date("20131201"), null, false));
    entries.add(new BackupEntry(date("20131202"), null, false));
    entries.add(new BackupEntry(date("20131130"), null, false));
    entries.add(new BackupEntry(date("20131129"), null, false));
    entries.add(new BackupEntry(date("20131128"), null, false));
    entries.add(new BackupEntry(date("20131127"), null, false));
    entries.add(new BackupEntry(date("20131126"), null, false));
    entries.add(new BackupEntry(date("20131125"), null, false));

    entries.add(new BackupEntry(date("20131124"), null, false));
    entries.add(new BackupEntry(date("20131123"), null, false));
    entries.add(new BackupEntry(date("20131122"), null, false));
    entries.add(new BackupEntry(date("20131119"), null, false));

    entries.add(new BackupEntry(date("20131118"), null, false));
    entries.add(new BackupEntry(date("20131117"), null, false));
    entries.add(new BackupEntry(date("20131116"), null, false));

    entries.add(new BackupEntry(date("20131111"), null, false));
    entries.add(new BackupEntry(date("20131110"), null, false));
    entries.add(new BackupEntry(date("20131109"), null, false));

    entries.add(new BackupEntry(date("20131102"), null, false));
    entries.add(new BackupEntry(date("20131101"), null, false));
    entries.add(new BackupEntry(date("20131031"), null, false));
    entries.add(new BackupEntry(date("20131030"), null, false));

    entries.add(new BackupEntry(date("20131002"), null, false));
    entries.add(new BackupEntry(date("20131001"), null, false));
    entries.add(new BackupEntry(date("20130930"), null, false));
    entries.add(new BackupEntry(date("20130929"), null, false));

    BackupEntrySelector.markEntriesToKeep(entries, date("20131201"));

    checkMarks("ttttttttt" + "tfff" + "ftf" + "ftf" + "ftff" + "ftff", entries);
  }

  private void checkMarks(String expected, List<BackupEntry> entries) {
    assertEquals(expected.length(), entries.size());
    for (int i = 0; i < expected.length(); i++) {
      BackupEntry e = entries.get(i);
      if ((expected.charAt(i) == 't') != e.isKeep()) {
        System.out.println("failure[" + i + "]: expected=" + expected.charAt(i)
            + " - " + e.getDate() + " keep=" + e.isKeep());
      }
      assertEquals(expected.charAt(i) == 't', e.isKeep());
    }
  }

  private static final SimpleDateFormat DF = new SimpleDateFormat("yyyyMMdd");
  private static final SimpleDateFormat DTF = new SimpleDateFormat(
      "yyyyMMdd-HHmmss");

  private Date date(String d) throws ParseException {
    return d.length() == 8 ? DF.parse(d) : DTF.parse(d);
  }

}
