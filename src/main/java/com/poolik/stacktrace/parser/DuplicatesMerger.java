package com.poolik.stacktrace.parser;

import com.poolik.stacktrace.StackTrace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DuplicatesMerger {

  public static final int FIRST_FRAMES_LIMIT = 10;
  public static final int SIMILARITY_THRESHOLD = 5;

  /**
   * @param stackTraces - list of stackTraces to purge from duplicates
   * @return List of unique stackTraces
   */
  public static List<StackTrace> mergeDuplicates(List<StackTrace> stackTraces) {
    List<StackTrace> uniqueStackTraces = new ArrayList<>();
    stackTraces.forEach(trace -> {
      if (trace.stackFrames.size() >= SIMILARITY_THRESHOLD) {
        searchForDuplicate(trace, uniqueStackTraces);
      }
      addCurrentIfUnique(trace, uniqueStackTraces);
    });
    return uniqueStackTraces;
  }

  private static void searchForDuplicate(StackTrace current, List<StackTrace> uniqueStackTraces) {
    int i = 0;
    final int finalI1 = i;
    Optional<StackTrace> duplicate = uniqueStackTraces.stream().filter(trace -> trace.stackFrames.containsAll(
        current.stackFrames.subList(finalI1, SIMILARITY_THRESHOLD)))
        .findAny();
    while (!duplicate.isPresent() && i <= FIRST_FRAMES_LIMIT - SIMILARITY_THRESHOLD && current.stackFrames.size() > SIMILARITY_THRESHOLD + i) {
      final int finalI = i;
      duplicate = uniqueStackTraces.stream().filter(trace -> trace.stackFrames.containsAll(
          current.stackFrames.subList(finalI, SIMILARITY_THRESHOLD + finalI)))
          .findAny();
      i++;
    }

    if (duplicate.isPresent()) {
      duplicate.get().duplicateCounter += current.duplicateCounter + 1;
      current.markDuplicate();
    }
  }

  private static void addCurrentIfUnique(StackTrace current, List<StackTrace> uniqueStackTraces) {
    if (!current.isDuplicate()) {
      if (current.stackFrames.size() < SIMILARITY_THRESHOLD && noStackTraceWithExactSameFrames(current, uniqueStackTraces)) {
        uniqueStackTraces.add(current);
      } else if (current.stackFrames.size() >= SIMILARITY_THRESHOLD)
        uniqueStackTraces.add(current);
    }
  }

  private static boolean noStackTraceWithExactSameFrames(StackTrace current, List<StackTrace> uniqueStackTraces) {
    Optional<StackTrace> exactDuplicate = uniqueStackTraces.stream()
        .filter(t -> t.stackFrames.size() <= SIMILARITY_THRESHOLD)
        .filter(t -> t.stackFrames.equals(current.stackFrames)).findAny();
    if (exactDuplicate.isPresent()) {
      exactDuplicate.get().duplicateCounter += current.duplicateCounter + 1;
      current.markDuplicate();
      return false;
    }
    return true;
  }
}