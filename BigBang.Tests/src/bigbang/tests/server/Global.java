package bigbang.tests.server;

import javax.servlet.*;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import bigBang.library.server.DocuShareServiceImpl;
import bigBang.library.server.EngineImplementor;

import Jewel.Engine.*;
import Jewel.Engine.SysObjects.*;

public class Global
	implements ServletContextListener, HttpSessionListener
{
	public void contextInitialized(ServletContextEvent e)
	{
//		try
//		{
//			HelloKDC.doKDC(null);
//		}
//		catch (Throwable e1)
//		{
//			return;
//		}

		try
        {
			Engine.InitEngine(new EngineImplementor(e.getServletContext()));
		}
        catch (JewelEngineException e1)
        {
			e1.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent e)
	{
	}

	public void sessionCreated(HttpSessionEvent arg0)
	{
	}

	public void sessionDestroyed(HttpSessionEvent arg0)
	{
		DocuShareServiceImpl.LogOff();
	}
}
