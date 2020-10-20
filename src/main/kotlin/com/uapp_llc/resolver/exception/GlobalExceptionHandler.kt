package com.uapp_llc.resolver.exception

import com.uapp_llc.exception.LocalizedException
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class GlobalExceptionHandler : DefaultHandlerExceptionResolver() {

  /**
   * Must be ordered lower than HandlerExceptionResolver, implemented by
   * {@link org.springframework.boot.web.servlet.error.DefaultErrorAttributes},
   * otherwise {@code javax.servlet.error.exception} can be read incorrectly.
   */
  init {
    super.setOrder(HIGHEST_PRECEDENCE + 1)
  }

  override fun doResolveException(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  handler: Any?,
                                  ex: Exception): ModelAndView {
    val superResult: ModelAndView? = super.doResolveException(request, response, handler, ex)

    if (superResult != null) {
      return superResult
    }

    try {
      if (ex is LocalizedException) {
        return handleLocalized(ex, response)
      }

      super.sendServerError(ex, request, response)
    } catch (e: java.lang.Exception) {
      logger.error("Failed to handle exception", e)
      response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
    }

    logger.error("Global exception", ex)
    return ModelAndView()
  }

  private fun handleLocalized(ex: LocalizedException,
                              response: HttpServletResponse): ModelAndView {
    response.sendError(ex.getStatusCode(), ex.message)
    return ModelAndView()
  }

}
