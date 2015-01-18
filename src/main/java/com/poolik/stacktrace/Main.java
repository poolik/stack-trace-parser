package com.poolik.stacktrace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.reverse;
import static java.util.Comparator.comparingInt;

public class Main {
  public static void main(String[] args) throws IOException {
    long start = System.currentTimeMillis();
    List<StackTrace> stackTraces = new StackTraceParser().parse(Paths.get(args[0]));
    long stop = System.currentTimeMillis();
    long time = stop-start;
    String duration = String.format("%d min, %d sec",
        TimeUnit.MILLISECONDS.toMinutes(time),
        TimeUnit.MILLISECONDS.toSeconds(time) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
    );
    System.out.println("TOOK: " + duration);
    writeResult(args[1], stackTraces);
  }

  private static void writeResult(String filePath, List<StackTrace> stackTraces) throws IOException {
    stackTraces.sort(comparingInt(s -> s.duplicateCounter));
    reverse(stackTraces); //more duplicates up front
    Files.write(
        Paths.get(filePath),
        stackTraces.stream().map(trace -> {
          trace.fullString.insert(0, "FROM: " + trace.originatingFile.toString() + "\n");
          trace.fullString.insert(0, "#### " + trace.duplicateCounter + " DUPLICATES ####\n");
          return trace.fullString.toString();
        }).collect(Collectors.joining()).getBytes());
  }
}