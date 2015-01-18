package com.poolik.stacktrace;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StackTrace {
  public final List<String> stackFrames = new ArrayList<>();
  public final StringBuilder fullString = new StringBuilder();
  public final Path originatingFile;
  private boolean duplicate;
  public int duplicateCounter = 0;

  public StackTrace(String last, Path originatingFile) {
    this.originatingFile = originatingFile;
    fullString.append(last).append("\n");
  }

  public boolean isDuplicate() {
    return duplicate;
  }

  public void markDuplicate() {
    this.duplicate = true;
  }

  @Override
  public String toString() {
    return fullString.toString();
  }
}
