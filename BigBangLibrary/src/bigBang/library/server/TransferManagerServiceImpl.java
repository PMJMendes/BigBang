package bigBang.library.server;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.interfaces.TransferManagerService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

public class TransferManagerServiceImpl
	extends EngineImplementor
	implements TransferManagerService
{
	private static final long serialVersionUID = 1L;

	public ManagerTransfer acceptTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public ManagerTransfer cancelTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}
}
