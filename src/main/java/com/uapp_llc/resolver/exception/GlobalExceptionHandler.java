package com.uapp_llc.resolver.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.uapp_llc.exception.LocalizedException;

@Component
public class GlobalExceptionHandler extends DefaultHandlerExceptionResolver {

  private final MessageSource messageSource;

  /**
   * Must be ordered lower than HandlerExceptionResolver, implemented by
   * {@link org.springframework.boot.web.servlet.error.DefaultErrorAttributes},
   * otherwise {@code javax.servlet.error.exception} can be read incorrectly.
   */
  @Autowired
  public GlobalExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
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
        return handleLocalized((LocalizedException) ex, request, response);
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
      LocalizedException ex, HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String message = messageSource.getMessage(ex, request.getLocale());
    response.sendError(ex.getStatusCode(), message);
    return new ModelAndView();
  }

}
