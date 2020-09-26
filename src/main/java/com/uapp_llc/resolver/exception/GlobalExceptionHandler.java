package com.uapp_llc.resolver.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.uapp_llc.exception.LocalizedException;

@Component
public class GlobalExceptionHandler extends DefaultHandlerExceptionResolver {

  /**
   * Must be ordered lower than HandlerExceptionResolver, implemented by
   * {@link org.springframework.boot.web.servlet.error.DefaultErrorAttributes},
   * otherwise {@code javax.servlet.error.exception} can be read incorrectly.
   */
  public GlobalExceptionHandler() {
    super.setOrder(HIGHEST_PRECEDENCE + 1);
  }

  @Override
  public final ModelAndView doResolveException(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    ModelAndView superResult = super.doResolveException(request, response, handler, ex);

    if (superResult != null) {
      return superResult;
    }

    try {
      if (ex instanceof LocalizedException) {
        return handleLocalized((LocalizedException) ex, response);
      }

      super.sendServerError(ex, request, response);
    } catch (Exception e) {
      logger.error("Failed to handle exception", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    logger.error("Global exception", ex);
    return new ModelAndView();
  }

  private ModelAndView handleLocalized(
      LocalizedException ex, HttpServletResponse response) throws IOException {
    response.sendError(ex.getStatusCode(), ex.getMessage());
    return new ModelAndView();
  }

}
