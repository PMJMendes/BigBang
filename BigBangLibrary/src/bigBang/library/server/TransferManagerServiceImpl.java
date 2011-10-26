package bigBang.library.server;

import java.util.UUID;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.interfaces.TransferManagerService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.AcceptXFer;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.CancelXFer;

public class TransferManagerServiceImpl
	extends EngineImplementor
	implements TransferManagerService
{
	private static final long serialVersionUID = 1L;

	public ManagerTransfer acceptTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		UUID lidProc;
		AcceptXFer lobjAX;
		ManagerTransfer lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
			lidProc = lobjXFer.GetProcessID();

			lobjAX = new AcceptXFer(lidProc);
			lobjAX.mbMassTransfer = false;
			lobjAX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new ManagerTransfer();
		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.managedProcessIds = new String[] {lobjAX.midParentProc.toString()};
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.processId = lidProc.toString();
		lobjResult.status = ManagerTransfer.Status.ACCEPTED;

		return lobjResult;
	}

	public ManagerTransfer cancelTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		UUID lidProc;
		CancelXFer lobjCX;
		ManagerTransfer lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
			lidProc = lobjXFer.GetProcessID();

			lobjCX = new CancelXFer(lidProc);
			lobjCX.mbMassTransfer = false;
			lobjCX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new ManagerTransfer();
		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.managedProcessIds = new String[] {lobjCX.midParentProc.toString()};
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.processId = lidProc.toString();
		lobjResult.status = ManagerTransfer.Status.CANCELED;

		return lobjResult;
	}

	public ManagerTransfer massAcceptTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		UUID lidProc;
		AcceptXFer lobjAX;
		ManagerTransfer lobjResult;
		UUID[] larrProcs;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
			lidProc = lobjXFer.GetProcessID();

			lobjAX = new AcceptXFer(lidProc);
			lobjAX.mbMassTransfer = true;
			lobjAX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrProcs = lobjXFer.GetProcessIDs();

		lobjResult = new ManagerTransfer();
		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.managedProcessIds = new String[larrProcs.length];
		for ( i = 0; i < larrProcs.length; i++ )
			lobjResult.managedProcessIds[i] = larrProcs[i].toString();
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.processId = lidProc.toString();
		lobjResult.status = ManagerTransfer.Status.ACCEPTED;

		return lobjResult;
	}

	public ManagerTransfer massCancelTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		UUID lidProc;
		CancelXFer lobjCX;
		ManagerTransfer lobjResult;
		UUID[] larrProcs;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
			lidProc = lobjXFer.GetProcessID();

			lobjCX = new CancelXFer(lidProc);
			lobjCX.mbMassTransfer = true;
			lobjCX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrProcs = new UUID[] {lobjCX.midParentProc};

		lobjResult = new ManagerTransfer();
		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.managedProcessIds = new String[larrProcs.length];
		for ( i = 0; i < larrProcs.length; i++ )
			lobjResult.managedProcessIds[i] = larrProcs[i].toString();
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.processId = lidProc.toString();
		lobjResult.status = ManagerTransfer.Status.ACCEPTED;

		return lobjResult;
	}
}
