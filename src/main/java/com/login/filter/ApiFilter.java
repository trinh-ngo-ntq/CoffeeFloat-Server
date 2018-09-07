package com.login.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

public class ApiFilter implements Filter {
    private static final String CLIENT_HEADER_NAME = "X-From-Fss";
    private static final String TOKEN_HEADER_NAME = "Authorization";
    private FilterConfig filterConfig; // For init value

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // Check if the request come from app
        // ANd had valid access token
        System.out.println("ApiFilter");
        final String isMobile = req.getHeader(CLIENT_HEADER_NAME);
        final String token = req.getHeader(TOKEN_HEADER_NAME);
        
        // TODO: Exclude login path here
        if (StringUtils.isBlank(isMobile) || StringUtils.isBlank(token)) {
            System.err.println("Empty token");
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        } else {
            // OK
        }
        
        HttpSession session = req.getSession();
        session.setAttribute("member", RandomStringUtils.random(10, false, true));
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {

    }
}
