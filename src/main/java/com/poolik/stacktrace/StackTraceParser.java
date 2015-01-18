package com.poolik.stacktrace;

import com.poolik.stacktrace.parser.FileParser;
import com.poolik.stacktrace.util.DirUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static com.poolik.stacktrace.parser.DuplicatesMerger.mergeDuplicates;

public class StackTraceParser {
  public static final String LOG_FILE_SUFFIX = ".log";

  public List<StackTrace> parse(Path path) throws IOException {
    if (Files.isDirectory(path)) return parseDirectory(path);
    else return parseFile(path);
  }

  private List<StackTrace> parseFile(Path path) {
    return mergeDuplicates(new FileParser(path).parse());
  }

  private List<StackTrace> parseDirectory(Path path) throws IOException {
    return mergeDuplicates(DirUtils.findWithSuffix(path, LOG_FILE_SUFFIX)
        .parallelStream()
        .flatMap(p -> this.parseFile(p).stream())
        .collect(Collectors.toList()));
  }
}