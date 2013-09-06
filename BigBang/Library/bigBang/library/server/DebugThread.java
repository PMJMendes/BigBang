package bigBang.library.server;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import Jewel.Web.server.WebServerThread;

public class DebugThread
	extends WebServerThread
{
	private static final Logger grefLogger = Logger.getLogger(DebugThread.class);
	private UUID mrefID;

	protected DebugThread(Runnable prefTarget, HttpSession prefSession, UUID prefID)
	{
		super(prefTarget, prefSession);

		mrefID = prefID;
	}

	public void run()
	{
    	grefLogger.debug("start|" + mrefID.toString() + "|" + mrefTarget.toString());
		EngineImplementor.setDebugID(mrefID);

		try
		{
			super.run();
		}
		finally
		{
			EngineImplementor.clearDebugID();
	    	grefLogger.debug("stop|" + mrefID.toString() + "|Thread done");
		}
	}
}
