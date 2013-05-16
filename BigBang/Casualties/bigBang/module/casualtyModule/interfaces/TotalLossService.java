package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.TotalLossFile;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("TotalLossService")
public interface TotalLossService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static TotalLossServiceAsync instance;
		public static TotalLossServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(TotalLossService.class);
			}
			return instance;
		}
	}

	public TotalLossFile getTotalLossFile(String id) throws SessionExpiredException, BigBangException;

	public TotalLossFile editTotalLossFile(TotalLossFile file) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public TotalLossFile closeProcess(String id) throws SessionExpiredException, BigBangException;
}
