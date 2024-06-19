package com.ispan.theater.filter;

import java.io.IOException;

import com.ispan.theater.service.AdminService;
import com.ispan.theater.service.CustomUserDetailsService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.ispan.theater.exception.OrderException;
import com.ispan.theater.util.JsonWebTokenUtility;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AdminService adminService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            //從request獲取JWT token
            String token = getTokenFromRequest(request);
            System.out.println("filter1:" + token);
            //從token獲取username
            //校验token
            if (token != null) {
                String temp = jsonWebTokenUtility.validateToken(token);
                if (StringUtils.hasText(token) && temp != null) {
                    String username = temp.substring(temp.indexOf("\"email\":\"") + 9, temp.indexOf("\"", temp.indexOf("\"email\":\"") + 9));
                    System.out.println("filter2:" + username);
                    // 加载與　token 關聯的用户

                    UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token,username);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println(SecurityContextHolder.getContext().getAuthentication());
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            resolver.resolveException(request, response, null, new ExpiredJwtException(null, null, "token驗證未過！"));
            filterChain.doFilter(request, response);
        }

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("filter3:" + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/user/pass")
                || request.getRequestURI().startsWith("/order/movie/findMovie")
                || request.getRequestURI().startsWith("/order/movie/findAllCinema")
                || request.getRequestURI().startsWith("/order/movie/dates")
                || request.getRequestURI().startsWith("/order/movie/times")
                || request.getRequestURI().startsWith("/order-redirect")
                || request.getRequestURI().startsWith("/order/movie/linePayConfirm") || request.getRequestURI().startsWith("/order/movie/ecPayConfirm")
                || request.getRequestURI().startsWith("/user/finduserphoto") || request.getRequestURI().startsWith("/order/movie/tickets")
                || request.getRequestURI().startsWith("/backstage/movie") || request.getRequestURI().startsWith("/comment")
                || request.getRequestURI().startsWith("/moviePicture")||request.getRequestURI().startsWith("/order/movie/findCinemaData")
                || request.getRequestURI().startsWith("/back/custService");
    }
    
    public UsernamePasswordAuthenticationToken getAuthentication(String token,String username) {
        String role = jsonWebTokenUtility.extractRole(token);
        UserDetails userDetails;
        System.out.println("ROLE"+role);
        if ("ROLE_ADMIN".equals(role)) {
            userDetails = adminService.loadUserByUsername(username);
        } else {
            userDetails = customUserDetailsService.loadUserByUsername(username);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }



}
