package com.poolik.stacktrace.parser;

import com.poolik.stacktrace.StackTrace;
import com.poolik.stacktrace.util.LineHistory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileParser {
  private static final Pattern STACK_FRAME_IDENTIFIER = Pattern
      .compile("^.*?at\\s([a-zA-Z_$][a-zA-Z\\d_$]*\\.[a-zA-Z_$][a-zA-Z\\d_$]*.*\\(([^:]*):?([\\d]*)\\)).*$"); //black magic
  private Optional<StackTrace> current = Optional.empty();
  private final Path fileToParse;

  public FileParser(Path fileToParse) {
    this.fileToParse = fileToParse;
  }

  public List<StackTrace> parse() {
    List<StackTrace> stackTraces = new ArrayList<>();
    try (Stream<String> lines = Files.lines(fileToParse, StandardCharsets.ISO_8859_1)){
      lines.forEach(applyFilters().andThen(parseLine(stackTraces)));
      addCurrentIfUnique(stackTraces);
    } catch (Exception e) {
      System.out.println("FAILED TO PARSE: " + fileToParse);
      e.printStackTrace();
    }
    return stackTraces;
  }

  private FilteredConsumer<String> applyFilters() {
    final LineHistory lineHistory = new LineHistory();
    return line -> {
      if (lineHistory.getLast().contains("Caused by:") && STACK_FRAME_IDENTIFIER.matcher(line).matches()) {
        current = Optional.empty();
        return false;
      }
      lineHistory.add(line);
      return true;
    };
  }

  private Consumer<String> parseLine(Collection<StackTrace> stackTraces) {
    final LineHistory lineHistory = new LineHistory();
    return line -> {
      Matcher frameMatcher = STACK_FRAME_IDENTIFIER.matcher(line);
      if (frameMatcher.matches()) {
        if (!current.isPresent()) current = Optional.of(new StackTrace(lineHistory.getLast(), fileToParse));
        String stackFrame = frameMatcher.group(1);
        current.get().stackFrames.add(stackFrame);
        current.get().fullString.append(line).append("\n");
      } else if (!frameMatcher.matches()) {
        addCurrentIfUnique(stackTraces);
      }
      lineHistory.add(line);
    };
  }


  private void addCurrentIfUnique(Collection<StackTrace> stackTraces) {
    if (current.isPresent()) stackTraces.add(current.get());
    current = Optional.empty();
  }
}