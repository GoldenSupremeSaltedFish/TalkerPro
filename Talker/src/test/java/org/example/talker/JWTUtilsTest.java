package org.example.talker;

import org.example.talker.Exception.UnauthorizedException;
import org.example.talker.util.Impl.JWTUtils;
import org.example.talker.annotation.JwtToken;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JWTUtilsTest {

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private JwtToken jwtToken;

    private JWTUtils jwtUtils;

    @BeforeEach
    public void setup() {
        jwtUtils = new JWTUtils();
    }

    @Test
    public void testValidateTokenforUser() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer validToken");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

        jwtUtils.validateTokenforUser(joinPoint, jwtToken);

        // Add your assertions here
    }

    @Test
    public void testValidateTokenforUserWithInvalidToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalidToken");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

        assertThrows(UnauthorizedException.class, () -> {
            jwtUtils.validateTokenforUser(joinPoint, jwtToken);
        });
    }
}
