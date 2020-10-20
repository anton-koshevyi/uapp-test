package com.uapp_llc.exception;

import javax.servlet.http.HttpServletResponse;

class IllegalActionException constructor(

    message: String

) : LocalizedException(message) {

  override fun getStatusCode(): Int = HttpServletResponse.SC_FORBIDDEN

}
