package ar.edu.itba.paw.webapp.Context;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;

public class ContextServlet {
    @Autowired
    public ServletContext servletContext;
}
