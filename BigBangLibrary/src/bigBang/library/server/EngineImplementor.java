package bigBang.library.server;

import javax.servlet.ServletContext;

import Jewel.Engine.Interfaces.IEngineImpl;

public class EngineImplementor
	extends Jewel.Web.server.EngineImplementor
	implements IEngineImpl
{
	private static final long serialVersionUID = 1L;

    public EngineImplementor()
    {
    	super();
    }

    public EngineImplementor(ServletContext prefContext)
    {
    	super(prefContext);
    }
}
