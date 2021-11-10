package com.example.Taupyk.lt.Security;

import com.example.Taupyk.lt.Models.CustomUser;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtToken jwtToken;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if(authorization != null && authorization.startsWith("Bearer "))
        {
            jwt = authorization.substring(7);
            boolean isValid = false;
            try {
                isValid = jwtToken.validateToken(jwt);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (SignatureException exception) {
                exception.printStackTrace();
            }
            if(isValid)
            username = jwtToken.extractUsername(jwt);
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            CustomUser customUser = (CustomUser)userService.loadUserByUsername(username);

            try
            {
                if(jwtToken.validateToken(jwt, customUser))
                {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtToken.getAuthenticationToken(jwt, SecurityContextHolder.getContext().getAuthentication(), customUser);
                    usernamePasswordAuthenticationToken.getAuthorities().forEach(auth -> System.out.println(auth));
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            catch(MalformedJwtException exception)
            {

            }
        }
        filterChain.doFilter(request, response);
    }
}
