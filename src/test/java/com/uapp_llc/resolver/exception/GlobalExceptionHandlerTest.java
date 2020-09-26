package com.uapp_llc.resolver.exception;

import javax.servlet.http.HttpServletResponse;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import com.uapp_llc.exception.LocalizedException;

public class GlobalExceptionHandlerTest {

  @BeforeEach
  public void setUp() {
    RestAssuredMockMvc.mockMvc(MockMvcBuilders
        .standaloneSetup(new TestController())
        .setHandlerExceptionResolvers(new GlobalExceptionHandler())
        .alwaysDo(MockMvcResultHandlers.log())
        .build());
  }

  @Test
  public void whenLocalizedExceptionType_thenExceptionBasedResponse() {
    RestAssuredMockMvc
        .given()
        .header("Accept-Language", "en")
        .when()
        .get("/localized")
        .then()
        .statusCode(HttpServletResponse.SC_BAD_REQUEST)
        .expect(ResultMatcher.matchAll(
            result -> Assertions
                .assertThat(result.getResolvedException())
                .isInstanceOf(LocalizedException.class)
                .hasMessage("Localized error"),
            result -> Assertions
                .assertThat(result.getResponse())
                .hasFieldOrPropertyWithValue("getErrorMessage", "Localized error")
        ));
  }

  @Test
  public void whenNotHandledType_thenInternalErrorResponse() {
    RestAssuredMockMvc
        .given()
        .header("Accept-Language", "en")
        .when()
        .get("/global")
        .then()
        .statusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
        .expect(ResultMatcher.matchAll(
            result -> Assertions
                .assertThat(result.getResolvedException())
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("Global error")
        ));
  }


  @Controller
  private static class TestController {

    @GetMapping("/localized")
    private void localized() {
      throw new LocalizedException("Localized error") {
        @Override
        public int getStatusCode() {
          return HttpServletResponse.SC_BAD_REQUEST;
        }
      };
    }

    @GetMapping("/global")
    private void global() {
      throw new RuntimeException("Global error");
    }

  }

}
