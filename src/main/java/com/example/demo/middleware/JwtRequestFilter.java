package com.example.demo.middleware;

import com.example.demo.utility.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);

                String username = jwtUtil.extractUsername(token);

                String roles = jwtUtil.extractRoles(token);

                if (username != null && jwtUtil.isTokenValid(username, token)) {
                    var authorities = Arrays.stream(roles.split(","))
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());

                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null, authorities)
                    );
                } else {
                    throw new SecurityException("Invalid token");
                }
            }

            filterChain.doFilter(request, response);
        } catch (SecurityException | SignatureException | ExpiredJwtException ex) {
            throw new SecurityException("Authentication Failed: " + ex.getMessage());
        }
    }
}
