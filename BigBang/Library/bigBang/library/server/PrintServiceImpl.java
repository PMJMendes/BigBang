package bigBang.library.server;

import java.awt.print.PrinterJob;

import Jewel.Engine.Engine;
import bigBang.library.interfaces.PrintService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

public class PrintServiceImpl
	extends EngineImplementor
	implements PrintService
{
	private static final long serialVersionUID = 1L;

	public String[] getAvailablePrinterNames()
		throws SessionExpiredException, BigBangException
	{
		javax.print.PrintService[] larrServices;
		String[] larrResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try 
		{
			larrServices = PrinterJob.lookupPrintServices();
			larrServices = PrinterJob.lookupPrintServices(); //JMMM: Chamada duplicada para ver se funciona à segunda

			larrResult = new String[larrServices.length];

			for(int i = 0; i < larrServices.length; i++)
			{
				larrResult[i] = larrServices[i].getName();
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult;
	}
}
