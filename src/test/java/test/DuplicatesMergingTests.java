package test;

import com.poolik.stacktrace.StackTrace;
import com.poolik.stacktrace.StackTraceParser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static test.TestUtil.assertSimpleStackTrace;
import static test.TestUtil.getTestFile;

public class DuplicatesMergingTests {
  private StackTraceParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new StackTraceParser();
  }

  @Test
  public void simpleDuplicate() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("duplicate.txt"));
    assertThat(stackTraces.size(), is(1));
    StackTrace stackTrace = stackTraces.get(0);
    assertSimpleStackTrace(stackTrace);
    assertThat(stackTrace.duplicateCounter, is(1));
  }

  @Test
  public void similarNonDuplicate() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("similarNonDuplicate.txt"));
    assertThat(stackTraces.size(), is(2));
    StackTrace stackTrace = stackTraces.get(0);
    assertSimpleStackTrace(stackTrace);
    assertThat(stackTrace.duplicateCounter, is(0));
  }

  @Test
  public void similarDuplicate() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("similarDuplicates.txt"));
    assertThat(stackTraces.size(), is(1));
    StackTrace stackTrace = stackTraces.get(0);
    assertStruts(stackTrace);
    assertThat(stackTrace.duplicateCounter, is(1));
  }

  @Test
  public void shortStackTraceDuplicates() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("shortStackTrace.txt"));
    assertThat(stackTraces.size(), is(1));
    StackTrace stackTrace = stackTraces.get(0);
    assertShortStackTrace(stackTrace);
    assertThat(stackTrace.duplicateCounter, is(2));
  }

  @Test
  public void parseDirWithDuplicates() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("multiple"));
    assertThat(stackTraces.size(), is(2));
    assertSimpleStackTrace(stackTraces.get(0));
    assertThat(stackTraces.get(0).duplicateCounter, is(3));
    assertStruts(stackTraces.get(1));
    assertThat(stackTraces.get(1).duplicateCounter, is(5));
  }

  private void assertStruts(StackTrace stackTrace) {
    assertThat(stackTrace.stackFrames.size(), is(31));
    assertTrue(stackTrace.fullString.toString().startsWith("There is no Action mapped for namespace"));
    assertTrue(stackTrace.fullString.toString().endsWith("ThreadPool$Worker.run(ThreadPool.java:1498)\n"));
  }

  private void assertShortStackTrace(StackTrace stackTrace) {
    assertThat(stackTrace.stackFrames.size(), is(2));
    assertTrue(stackTrace.fullString.toString().startsWith("2015-01-16 00:34:13 JRebel-Spring: Adding bean 'beanAdded'"));
    assertTrue(stackTrace.fullString.toString().endsWith("java:1147) ~[catalina.jar:8.0.15]:1\n"));
  }
}