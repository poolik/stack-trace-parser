package com.poolik.stacktrace.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;

public class SuffixFileVisitor extends SimpleFileVisitor<Path> {
  private final String suffix;
  private final Collection<Path> files = new ArrayList<>();

  public SuffixFileVisitor(String suffix) {
    this.suffix = suffix;
  }

  public Collection<Path> getFiles() {
    return files;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (file.getFileName().toString().endsWith(suffix)) files.add(file);
    return FileVisitResult.CONTINUE;
  }
}