package test;

import com.poolik.stacktrace.StackTrace;

import java.io.File;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestUtil {
  public static void assertASMError(StackTrace stackTrace) {
    assertThat(stackTrace.stackFrames.size(), is(12));
    assertTrue(stackTrace.fullString.toString().startsWith("2015-01-16 00:07:58 JRebel-SDK-CBP: ERROR Class 'org.apache.cxf.common.util.ASMHelper'"));
    assertTrue(stackTrace.fullString.toString().endsWith("URLClassLoader$1.run(URLClassLoader.java:197):1\n"));
  }

  public static void assertSimpleStackTrace(StackTrace stackTrace) {
    assertThat(stackTrace.stackFrames.size(), is(18));
    assertTrue(stackTrace.fullString.toString().startsWith("2015-01-16 00:34:07 JRebel: ERROR"));
    assertTrue(stackTrace.fullString.toString().endsWith(".run(QueuedThreadPool.java:522)\n"));
  }

  public static Path getTestFile(String name) {
    return new File(SimpleParserTests.class.getResource("/"+name).getFile()).toPath();
  }
}
