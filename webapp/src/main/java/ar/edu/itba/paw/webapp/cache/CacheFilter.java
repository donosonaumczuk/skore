package ar.edu.itba.paw.webapp.cache;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CacheFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.setHeader("Cache-Control", "public, max-age=" + Long.MAX_VALUE + ", immutable");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
