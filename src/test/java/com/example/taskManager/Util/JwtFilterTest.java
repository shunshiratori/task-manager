package com.example.taskManager.Util;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

class JwtFilterTest {
    private final JwtUtil jwtUtil = new JwtUtil("test-only-secret-at-least-32-bytes-long");
    private final JwtFilter filter = new JwtFilter(jwtUtil);

    @Test
    void rejectsMissingMalformedAndInvalidTokens() throws Exception {
        for (String header : new String[]{null, "Basic credentials", "Bearer ", "Bearer invalid"}) {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/projects");
            if (header != null) {
                request.addHeader("Authorization", header);
            }
            MockHttpServletResponse response = new MockHttpServletResponse();
            AtomicBoolean called = new AtomicBoolean();
            filter.doFilter(request, response, (req, res) -> called.set(true));
            assertEquals(401, response.getStatus());
            assertFalse(called.get());
        }
    }

    @Test
    void allowsPublicPostEndpointsWithoutToken() throws Exception {
        for (String path : new String[]{"/login", "/users"}) {
            MockHttpServletRequest request = new MockHttpServletRequest("POST", "/app" + path);
            request.setContextPath("/app");
            MockHttpServletResponse response = new MockHttpServletResponse();
            AtomicBoolean called = new AtomicBoolean();
            filter.doFilter(request, response, (req, res) -> called.set(true));
            assertTrue(called.get());
            assertEquals(200, response.getStatus());
        }
    }

    @Test
    void otherMethodsOnPublicPathsStillRequireToken() throws Exception {
        for (String path : new String[]{"/login", "/users"}) {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, (req, res) -> fail("Must not continue"));
            assertEquals(401, response.getStatus());
        }
    }

    @Test
    void validTokenPassesUserIdToNextHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/projects");
        request.addHeader("Authorization", "Bearer " + jwtUtil.generationToken(1L));
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean called = new AtomicBoolean();
        filter.doFilter(request, response, (req, res) -> {
            called.set(true);
            assertEquals(1L, req.getAttribute("userId"));
        });
        assertTrue(called.get());
        assertEquals(200, response.getStatus());
    }
}
