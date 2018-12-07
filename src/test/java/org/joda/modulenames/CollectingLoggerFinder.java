package org.joda.modulenames;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class CollectingLoggerFinder extends System.LoggerFinder {

  static final class Log {
    final System.Logger.Level level;
    final String message;
    final Throwable throwable;

    Log(System.Logger.Level level, String message, Throwable throwable) {
      this.level = level;
      this.message = message;
      this.throwable = throwable;
    }

    public System.Logger.Level getLevel() {
      return level;
    }

    public String getMessage() {
      return message;
    }

    public Optional<Throwable> getThrowable() {
      return Optional.ofNullable(throwable);
    }

    @Override
    public String toString() {
      return String.format("[%s] %s", level, message);
    }
  }

  static class Logger implements System.Logger {

    private final String name;
    private final List<Log> logs;

    Logger(String name) {
      this.name = name;
      this.logs = new ArrayList<>();
    }

    public List<Log> getLogs() {
      return logs;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean isLoggable(Level level) {
      return true;
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
      logs.add(new Log(level, msg, thrown));
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
      logs.add(new Log(level, MessageFormat.format(format, params), null));
    }
  }

  private final Map<String, Logger> loggers = new ConcurrentHashMap<>();

  @Override
  public System.Logger getLogger(String name, Module module) {
    var key = name + '@' + module;
    return loggers.computeIfAbsent(key, k -> new Logger(name));
  }
}
