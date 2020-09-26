package com.uapp_llc.exception;

import javax.servlet.http.HttpServletResponse;

public class NotFoundException extends LocalizedException {

  public NotFoundException(String message) {
    super(message);
  }

  @Override
  public int getStatusCode() {
    return HttpServletResponse.SC_NOT_FOUND;
  }

}
