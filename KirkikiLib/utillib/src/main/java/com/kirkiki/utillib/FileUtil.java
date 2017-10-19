package com.kirkiki.utillib;

import java.io.File;

public final class FileUtil {

  private FileUtil() {
    throw new RuntimeException("illegal access my friend!!");
  }

  public static boolean isFileExists(File file) {
    return file.exists();
  }

  public static String stackTraceToString(Throwable e) {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
      sb.append(element.toString());
      sb.append("\n");
    }
    return sb.toString();
  }
}
