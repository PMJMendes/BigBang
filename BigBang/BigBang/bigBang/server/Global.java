package bigBang.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import Jewel.Engine.Engine;
import bigBang.library.server.DocuShareServiceImpl;
import bigBang.library.server.EngineImplementor;

public class Global
	implements ServletContextListener, HttpSessionListener
{
	private static final Logger grefLogger = Logger.getLogger(Global.class);

	public void contextInitialized(ServletContextEvent e)
	{
		grefLogger.debug("App started");
        try
        {
			Engine.InitEngine(new EngineImplementor(e.getServletContext()));
		}
        catch (Throwable e1)
        {
        	grefLogger.fatal("Error in App Init", e1);
		}
	}

	public void contextDestroyed(ServletContextEvent e)
	{
		grefLogger.debug("App killed");
	}

	public void sessionCreated(HttpSessionEvent arg0)
	{
	}

	public void sessionDestroyed(HttpSessionEvent arg0)
	{
		DocuShareServiceImpl.LogOff();
	}
}
