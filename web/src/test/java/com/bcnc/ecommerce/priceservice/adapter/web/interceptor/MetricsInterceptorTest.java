package com.bcnc.ecommerce.priceservice.adapter.web.interceptor;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MetricsInterceptorTest {

    private static final String URI_APPLICABLE = "/prices/applicable";
    private static final String URI_NOT_FOUND = "/prices/notfound";
    private static final String URI_INVALID = "/prices/invalid";
    private static final String URI_ERROR = "/prices/error";
    private static final String URI_FAVICON = "/favicon.ico";
    private static final String URI_SWAGGER_UI = "/swagger-ui/index.html";
    private static final String URI_API_DOCS = "/v3/api-docs";
    private static final String METHOD_GET = "GET";
    private static final int STATUS_200 = 200;
    private static final int STATUS_400 = 400;
    private static final int STATUS_404 = 404;
    private static final int STATUS_500 = 500;
    private static final String METRIC_TOTAL = "http_requests_total";
    private static final String METRIC_GLOBAL = "http_requests_global_total";
    private static final String TAG_METHOD = "method";
    private static final String TAG_URI = "uri";
    private static final String TAG_STATUS = "status";

    private MeterRegistry meterRegistry;
    private MetricsInterceptor metricsInterceptor;

    @BeforeEach
    void setup()
    {
        meterRegistry = mock(MeterRegistry.class);
        metricsInterceptor = new MetricsInterceptor(meterRegistry);
    }

    @Test
    void shouldIncrementCounterForNonExcludedUri()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn(METHOD_GET);
        when(request.getRequestURI()).thenReturn(URI_APPLICABLE);
        when(response.getStatus()).thenReturn(STATUS_200);

        Counter mockCounter = mock(Counter.class);
        when(meterRegistry.counter(METRIC_TOTAL, TAG_METHOD, METHOD_GET, TAG_URI, URI_APPLICABLE, TAG_STATUS,
                String.valueOf(STATUS_200))).thenReturn(mockCounter);
        when(meterRegistry.counter(METRIC_GLOBAL)).thenReturn(mockCounter);

        metricsInterceptor.afterCompletion(request, response, null, null);

        verify(mockCounter, times(2)).increment(); // both counters
    }

    @ParameterizedTest
    @MethodSource("excludedUris")
    void shouldNotIncrementCounterForExcludedUris(String uri)
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn(uri);
        when(response.getStatus()).thenReturn(200);

        metricsInterceptor.afterCompletion(request, response, null, null);

        verifyNoInteractions(meterRegistry);
    }

    @ParameterizedTest
    @MethodSource("errorResponseParameters")
    void shouldIncrementCounterForErrorResponse(int statusCode, String uri)
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn(METHOD_GET);
        when(request.getRequestURI()).thenReturn(uri);
        when(response.getStatus()).thenReturn(statusCode);

        Counter mockCounter = mock(Counter.class);
        when(meterRegistry.counter(METRIC_TOTAL, TAG_METHOD, METHOD_GET, TAG_URI, uri, TAG_STATUS,
                String.valueOf(statusCode))).thenReturn(mockCounter);
        when(meterRegistry.counter(METRIC_GLOBAL)).thenReturn(mockCounter);

        metricsInterceptor.afterCompletion(request, response, null, null);

        verify(mockCounter, times(2)).increment(); // global + specific
    }

    private static Stream<Arguments> errorResponseParameters()
    {
        return Stream.of(
                Arguments.of(STATUS_400, URI_INVALID),
                Arguments.of(STATUS_404, URI_NOT_FOUND),
                Arguments.of(STATUS_500, URI_ERROR)
        );
    }

    static Stream<String> excludedUris() {
        return Stream.of(
                URI_FAVICON,
                URI_SWAGGER_UI,
                URI_API_DOCS
        );
    }
}
