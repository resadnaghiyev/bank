package com.rashad.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rashad.bank.api.dto.response.CustomResponse;
import com.rashad.bank.exception.ErrorCode;
import com.rashad.bank.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageSource messageSource;

    public JwtAuthenticationEntryPoint(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (authException instanceof InsufficientAuthenticationException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            String message = messageSource.getMessage("unauthorized", null, request.getLocale());
            String responseMsg = mapper.writeValueAsString(CustomResponse.builder().error(
                    new ErrorResponse(ErrorCode.UNAUTHORIZED, message)).build());
            response.getWriter().write(responseMsg);
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            System.out.println("Exception thrown from class: " + authException.getClass().getName());
            ObjectMapper mapper = new ObjectMapper();
            String message = messageSource.getMessage("bad-request", null, request.getLocale());
            String responseMsg = mapper.writeValueAsString(CustomResponse.builder().error(
                    new ErrorResponse(ErrorCode.BAD_REQUEST, message)).build());
            response.getWriter().write(responseMsg);
        }
    }
}
