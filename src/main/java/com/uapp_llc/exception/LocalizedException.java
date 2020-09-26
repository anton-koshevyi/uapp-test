package com.uapp_llc.exception;

public abstract class LocalizedException extends RuntimeException {

  public LocalizedException(String message) {
    super(message);
  }

  public abstract int getStatusCode();

}
