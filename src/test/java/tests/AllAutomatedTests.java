package tests;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import backupcleaner.BackupEntrySelectorTests;

public class AllAutomatedTests {

  public static Test suite() {
    TestSuite ts = new TestSuite();
    ts.addTestSuite(BackupEntrySelectorTests.class);
    return ts;
  }

  public static void main(final String[] args) {
    Test ts = suite();
    TestResult tr = junit.textui.TestRunner.run(ts);
    if (!tr.wasSuccessful()) {
      System.exit(1);
    }
  }

}
