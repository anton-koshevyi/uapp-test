package com.uapp_llc.exception;

import javax.servlet.http.HttpServletResponse;

public class IllegalActionException extends LocalizedException {

  public IllegalActionException(String message) {
    super(message);
  }

  @Override
  public int getStatusCode() {
    return HttpServletResponse.SC_FORBIDDEN;
  }

}
