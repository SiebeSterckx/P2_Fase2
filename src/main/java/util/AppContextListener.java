package util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Opens connection when application is starting
 */
@WebListener
public class AppContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String dbUrl = servletContextEvent.getServletContext().getInitParameter("dburl");
        String schemaDm = servletContextEvent.getServletContext().getInitParameter("schemaDm");
        String schema = servletContextEvent.getServletContext().getInitParameter("schema");
        DbConnectionService.connect(dbUrl, schemaDm, schema);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        DbConnectionService.disconnect();
    }
}