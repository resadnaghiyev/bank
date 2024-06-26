package com.rashad.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rashad.bank.api.service.JwtService;
import com.rashad.bank.api.service.UserDetailsServiceImpl;
import com.rashad.bank.api.dto.response.CustomResponse;
import com.rashad.bank.exception.ErrorCode;
import com.rashad.bank.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl detailsService;
    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            for (String path : whiteList) {
                if (request.getRequestURI().contains(path)){
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = detailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            ObjectMapper mapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            if (exception instanceof SignatureException) {
                String message = messageSource.getMessage("jwt-token-not-valid", null, request.getLocale());
                String responseMsg = mapper.writeValueAsString(CustomResponse.builder().error(
                        new ErrorResponse(ErrorCode.UNAUTHORIZED, message)).build());
                response.getWriter().write(responseMsg);
            }
            else if (exception instanceof ExpiredJwtException) {
                String message = messageSource.getMessage("jwt-token-expired", null, request.getLocale());
                String responseMsg = mapper.writeValueAsString(CustomResponse.builder().error(
                        new ErrorResponse(ErrorCode.UNAUTHORIZED, message)).build());
                response.getWriter().write(responseMsg);
            }
            else {
                System.out.println("** exception msg: " + exception.getMessage());
                System.out.println("** exception cause: " + exception.getCause());
                System.out.println("** exception clas: " + exception.getClass().getName());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String message = messageSource.getMessage("bad-request", null, request.getLocale());
                String responseMsg = mapper.writeValueAsString(CustomResponse.builder().error(
                        new ErrorResponse(ErrorCode.BAD_REQUEST, message)).build());
                response.getWriter().write(responseMsg);
            }
        }
    }

    private final List<String> whiteList = List.of(
            "/user/login",
            "/user/register",
            "/user/public/test"
    );
}
