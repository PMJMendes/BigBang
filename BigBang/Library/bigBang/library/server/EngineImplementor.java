package bigBang.library.server;

import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEngineImpl;
import Jewel.Engine.SysObjects.JewelWorkerThread;

import com.google.gwt.user.server.rpc.RPCRequest;

public class EngineImplementor
	extends Jewel.Web.server.EngineImplementor
	implements IEngineImpl
{
	private static final Logger grefLogger = Logger.getLogger(EngineImplementor.class);
	private static final long serialVersionUID = 1L;

	protected static class DebugHolder
	{
		public ThreadLocal<UUID> theDebugID;

		public DebugHolder()
		{
			theDebugID = new ThreadLocal<UUID>()
			{
				protected UUID initialValue()
				{
					return null;
				}
			};
		}
	}
	protected static DebugHolder grefDebugID;

    protected static UUID getDebugID()
    {
    	return grefDebugID.theDebugID.get();
    }

    protected static void setDebugID(UUID value)
    {
    	grefDebugID.theDebugID.set(value);
    }

    protected static void clearDebugID()
    {
    	grefDebugID.theDebugID.set(null);
    }

    public EngineImplementor()
    {
    	super();

    	if ( grefDebugID == null )
    		grefDebugID = new DebugHolder();
    }

    public EngineImplementor(ServletContext prefContext)
    {
    	super(prefContext);

    	if ( grefDebugID == null )
    		grefDebugID = new DebugHolder();
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp)
    	throws ServletException, java.io.IOException
    {
    	setDebugID(UUID.randomUUID());
    	try
    	{
    		super.service(req, resp);
    	}
    	finally
    	{
    		clearDebugID();
    	}
    }

    protected void onAfterRequestDeserialized(RPCRequest rpcRequest)
    {
    	String lstrUser;
    	UUID lidUser;

    	lstrUser = "(sem user)";
    	lidUser = Engine.getCurrentUser();
    	if ( lidUser != null )
    	{
    		try
    		{
				lstrUser = User.GetInstance(Engine.getCurrentNameSpace(), lidUser).getDisplayName();
			}
    		catch (Throwable e)
    		{
    	    	lstrUser = "(erro a obter o user)";
			}
    	}
    	grefLogger.debug("in|" + getDebugID().toString() + "|" + lstrUser + "|" + rpcRequest.getMethod().toString());

    	super.onAfterRequestDeserialized(rpcRequest);
    }

    protected void onAfterResponseSerialized(String serializedResponse)
    {
    	String lstrUser;
    	UUID lidUser;
    	String lstrResp;

    	lstrUser = "(sem user)";
		try
		{
	    	lidUser = Engine.getCurrentUser();
	    	if ( lidUser != null )
				lstrUser = User.GetInstance(Engine.getCurrentNameSpace(), lidUser).getDisplayName();
    	}
		catch (Throwable e)
		{
	    	lstrUser = "(erro a obter o user)";
		}

    	lstrResp = serializedResponse;
    	if ( (lstrResp != null) && (lstrResp.length() > 50) )
    		lstrResp = lstrResp.substring(0, 50);
    	grefLogger.debug("out|" + getDebugID().toString() + "|" + lstrUser + "|" + lstrResp);

    	super.onAfterResponseSerialized(serializedResponse);
    }

	public JewelWorkerThread getThread(Runnable prefThread)
	{
		return new DebugThread(prefThread, getSession(), getDebugID());
	}
}
