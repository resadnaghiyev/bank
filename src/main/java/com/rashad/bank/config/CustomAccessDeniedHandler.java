package com.rashad.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rashad.bank.api.dto.response.CustomResponse;
import com.rashad.bank.exception.ErrorCode;
import com.rashad.bank.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final MessageSource messageSource;

    public CustomAccessDeniedHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        final ObjectMapper mapper = new ObjectMapper();
        String message = messageSource.getMessage("access-denied", null, request.getLocale());

        String responseMsg = mapper.writeValueAsString(CustomResponse.builder().error(
                new ErrorResponse(ErrorCode.ACCESS_DENIED, message)).build());
        response.getWriter().write(responseMsg);
    }
}
