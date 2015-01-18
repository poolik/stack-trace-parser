package com.poolik.stacktrace.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public class DirUtils {
  private DirUtils() {}

  public static Collection<Path> findWithSuffix(Path from, String suffix) throws IOException {
    validate(from);
    SuffixFileVisitor visitor = new SuffixFileVisitor(suffix);
    Files.walkFileTree(from, visitor);
    return visitor.getFiles();
  }

  private static void validate(Path... paths) {
    for (Path path : paths) {
      if (!Files.isDirectory(Objects.requireNonNull(path))) {
        throw new IllegalArgumentException(String.format("%s is not a directory", path.toString()));
      }
    }
  }
}