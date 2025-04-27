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

        if(header != null){
            String[] authElements = header.split(" ");

            if(authElements.length == 2 && "Bearer".equals(authElements[0])){
                try{
                    //in the HTTP filter i first check the HTTP verb
                    //if it's a GET, i continue as before
                    //otherwise, if it's a PUT
                    //or DELETE i use a stronger vaidation
                    if("GET".equals(request.getMethod())){
                        SecurityContextHolder.getContext().setAuthentication(userAuthProvider.validateToken(authElements[1]));
                    }else{
                        SecurityContextHolder.getContext().setAuthentication(userAuthProvider.validateTokenStrongly(authElements[1]));
                    }


                } catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }
        }
         //i first check if there is an authorization header
        //and the first part is a Bearer


        //at the end continue the filter chain
        filterChain.doFilter(request,response);
        //and now i use this filter in the security config
    }
}
