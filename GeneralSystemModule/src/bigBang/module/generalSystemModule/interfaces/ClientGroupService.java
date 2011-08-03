package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.client.types.ClientGroup;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ClientGroupService")
public interface ClientGroupService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util
	{
		private static ClientGroupServiceAsync instance;
		public static ClientGroupServiceAsync getInstance()
		{
			if (instance == null)
			{
				instance = GWT.create(ClientGroupService.class);
			}
			return instance;
		}
	}
	
	public ClientGroup[] getClientGroupList() throws SessionExpiredException, BigBangException;
	public ClientGroup createClientGroup(ClientGroup clientGroup) throws SessionExpiredException, BigBangException;
	public ClientGroup saveClientGroup(ClientGroup clientGroup) throws SessionExpiredException, BigBangException;
	public void deleteClientGroup(String id) throws SessionExpiredException, BigBangException;
}
