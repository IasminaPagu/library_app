package com.library.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.http.HttpHeaders;


@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthProvider;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            String[] authElements = header.split(" ");
            if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                String rawToken = authElements[1];

                String token = rawToken.trim().replaceAll("^\"|\"$", "");

                try {
                    if ("GET".equals(request.getMethod())) {
                        SecurityContextHolder.getContext()
                                .setAuthentication(userAuthProvider.validateToken(token));
                    } else {
                        SecurityContextHolder.getContext()
                                .setAuthentication(userAuthProvider.validateTokenStrongly(token));
                    }
                } catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    //throw e;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
