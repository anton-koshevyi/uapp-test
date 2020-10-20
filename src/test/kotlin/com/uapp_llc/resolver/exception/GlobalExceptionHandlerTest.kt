package com.uapp_llc.resolver.exception

import com.uapp_llc.exception.LocalizedException
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Controller
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletResponse

class GlobalExceptionHandlerTest {

  @BeforeEach
  fun setUp() {
    RestAssuredMockMvc.mockMvc(MockMvcBuilders
        .standaloneSetup(TestController())
        .setHandlerExceptionResolvers(GlobalExceptionHandler())
        .alwaysDo<StandaloneMockMvcBuilder>(MockMvcResultHandlers.log())
        .build())
  }

  @Test
  fun whenLocalizedExceptionType_thenExceptionBasedResponse() {
    RestAssuredMockMvc
        .given()
        .header("Accept-Language", "en")
        .`when`()
        .get("/localized")
        .then()
        .statusCode(HttpServletResponse.SC_BAD_REQUEST)
        .expect(ResultMatcher.matchAll(
            ResultMatcher {
              Assertions
                  .assertThat(it.resolvedException)
                  .isInstanceOf(LocalizedException::class.java)
                  .hasMessage("Localized error")
            },
            ResultMatcher {
              Assertions
                  .assertThat(it.response)
                  .hasFieldOrPropertyWithValue("getErrorMessage", "Localized error")
            }
        ))
  }

  @Test
  fun whenNotHandledType_thenInternalErrorResponse() {
    RestAssuredMockMvc
        .given()
        .header("Accept-Language", "en")
        .`when`()
        .get("/global")
        .then()
        .statusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
        .expect {
          Assertions
              .assertThat(it.resolvedException)
              .isInstanceOf(RuntimeException::class.java)
              .hasMessage("Global error")
        }
  }


  @Controller
  private class TestController {

    @GetMapping("/localized")
    private fun localized(): Nothing = throw object : LocalizedException("Localized error") {
      override fun getStatusCode(): Int = HttpServletResponse.SC_BAD_REQUEST
    }


    @GetMapping("/global")
    private fun global(): Nothing = throw RuntimeException("Global error")

  }

}
