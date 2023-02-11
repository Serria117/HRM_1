package com.hrm.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component @Slf4j
public class AppRequestFilter extends OncePerRequestFilter
{
    @Autowired JWTProvider JWTProvider;
    @Autowired UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        var header = request.getHeader("Authorization");
        try {
            if ( header != null && header.startsWith("Bearer ") ) {
                var token = header.substring(7);
//            log.info("Intercepted token: " + token);
                var username = JWTProvider.getUsernameFromToken(token);
                if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
                    var userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(username);
                    if ( JWTProvider.validateToken(token, userDetails) ) {
                        log.info("Token is valid");
                        var usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
                        log.info("Security context has been set");
                    }
                    else {
                        log.error("Token is not valid");
                    }
                }
            }
        }
        catch ( Exception e ) {
            log.warn(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
