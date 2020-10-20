package com.uapp_llc.exception;

import javax.servlet.http.HttpServletResponse;

class NotFoundException constructor(

    message: String

) : LocalizedException(message) {

  override fun getStatusCode(): Int = HttpServletResponse.SC_NOT_FOUND

}
