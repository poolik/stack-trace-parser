package com.poolik.stacktrace.parser;

import java.util.Objects;
import java.util.function.Consumer;

public interface FilteredConsumer<T> {

  boolean filter(T t);

  default Consumer<T> andThen(Consumer<? super T> after) {
    Objects.requireNonNull(after);
    return (T t) -> { if (filter(t)) after.accept(t); };
  }
}