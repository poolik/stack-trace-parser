package test;

import com.poolik.stacktrace.StackTrace;
import com.poolik.stacktrace.StackTraceParser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static test.TestUtil.assertASMError;
import static test.TestUtil.assertSimpleStackTrace;
import static test.TestUtil.getTestFile;

public class SimpleParserTests {

  private StackTraceParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new StackTraceParser();
  }

  @Test
  public void simpleStacktrace() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("simpleNoTrailing.txt"));
    assertThat(stackTraces.size(), is(1));
    assertSimpleStackTrace(stackTraces.get(0));
  }

  @Test
  public void simpleStacktraceNoTrailing() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("simpleNoTrailing.txt"));
    assertThat(stackTraces.size(), is(1));
    assertSimpleStackTrace(stackTraces.get(0));
  }

  @Test
  public void ignoresCausedBy() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("causedBySimple.txt"));
    assertThat(stackTraces.size(), is(1));
    assertSimpleStackTrace(stackTraces.get(0));
  }

  @Test
  public void parsesMultiple() throws Exception {
    List<StackTrace> stackTraces = parser.parse(getTestFile("multiple.txt"));
    assertThat(stackTraces.size(), is(2));
    assertSimpleStackTrace(stackTraces.get(0));
    assertASMError(stackTraces.get(1));
  }

}