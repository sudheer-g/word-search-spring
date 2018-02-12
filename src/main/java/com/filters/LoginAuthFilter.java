package com.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginAuthFilter implements Filter {
    private Logger logger = LogManager.getLogger();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/services/login";

        boolean loggedIn = session != null && session.getAttribute("userName") != null;
        boolean loginRequest = request.getRequestURI().equals(loginURI);
        logger.info("Logged In: {}, Login Request: {}", loggedIn, loginRequest);
        if(loggedIn && loginRequest){
            response.sendRedirect("wordSearch");
            return;
        }
        if (loggedIn || loginRequest) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(loginURI);
        }
    }


    @Override
    public void destroy() {

    }
}
