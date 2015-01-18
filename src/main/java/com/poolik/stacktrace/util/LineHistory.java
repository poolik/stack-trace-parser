package com.poolik.stacktrace.util;

public class LineHistory {
  private String last = "";
  private String nextToLast = "";

  public void add(String line) {
    nextToLast = last;
    last = line;
  }

  public String getLast() {
    return last;
  }
}