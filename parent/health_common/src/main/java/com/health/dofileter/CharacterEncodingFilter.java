package com.health.dofileter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CharacterEncodingFilter",urlPatterns = {"/*"},initParams = @WebInitParam(name = "charset",value = "UTF-8"))
public class CharacterEncodingFilter implements Filter {
    private String charset;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request=(HttpServletRequest)req;
        HttpServletResponse response=(HttpServletResponse) resp;
        request.setCharacterEncoding(charset);
        response.setCharacterEncoding(charset);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        charset=config.getInitParameter("charset");
    }

}
