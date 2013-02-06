package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.interfaces.DependentItemSubService;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("QuoteRequestService")
public interface QuoteRequestService
	extends SearchService, DependentItemSubService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static QuoteRequestServiceAsync instance;
		public static QuoteRequestServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(QuoteRequestService.class);
			}
			return instance;
		}
	}

	public QuoteRequest getEmptyRequest(String clientId) throws SessionExpiredException, BigBangException;

	public QuoteRequest getRequest(String requestId) throws SessionExpiredException, BigBangException;

	public QuoteRequest editRequest(QuoteRequest request) throws SessionExpiredException, BigBangException;

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public QuoteRequest closeProcess(String requestId, String notes) throws SessionExpiredException, BigBangException;

	public void deleteRequest(String requestId, String reason) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;
}
