package bigBang.library.interfaces;

import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("TransferManagerService")
public interface TransferManagerService
	extends RemoteService
{
	public static class Util
	{
		private static TransferManagerServiceAsync instance;

		public static TransferManagerServiceAsync getInstance()
		{
			if (instance == null)
				instance = GWT.create(TransferManagerService.class);

			return instance;
		}
	}

	public ManagerTransfer getTransfer(String transferId) throws SessionExpiredException, BigBangException;

	public ManagerTransfer acceptTransfer(String transferId) throws SessionExpiredException, BigBangException;
	public ManagerTransfer cancelTransfer(String transferId) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massAcceptTransfer(String transferId) throws SessionExpiredException, BigBangException;
	public ManagerTransfer massCancelTransfer(String transferId) throws SessionExpiredException, BigBangException;
}
