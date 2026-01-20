package com.backend.estudiantes.filters;

import java.io.IOException;
import java.rmi.ServerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.estudiantes.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(

        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain

    ) throws ServletException, IOException {

        log.debug(" Procesando solicitud: {} {}",
            request.getMethod(), request.getRequestURI());

            final String authHeader = request.getHeader("Authorization");

            //bearer
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                log.debug("No se encontro token");
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);

            try {
                final String userEmail = jwtService.extractUsername(jwt);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    
                    log.debug("extrayendo usuario del token: {}", userEmail);

                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                    if (jwtService.isTokenValid(jwt, userDetails)) {

                        log.debug("token valido para: {}", userEmail);

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        log.info("User logeado: {} - Ruta: {}", userEmail, request.getRequestURI());


                    } else {
                        log.warn("error en filtro: {}", userEmail);
                    }
                }

            } catch (Exception e) {
                log.error("Error en el filtro: {}", e.getMessage());
            }

        filterChain.doFilter(request, response);

    }

}