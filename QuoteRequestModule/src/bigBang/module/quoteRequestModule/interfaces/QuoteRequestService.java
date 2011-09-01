package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.ClientInfoOrDocumentRequest;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestToManagerTransfer;
import bigBang.definitions.shared.RiskAnalisys;
import bigBang.definitions.shared.SecuredObject;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("QuoteRequestService")
public interface QuoteRequestService extends SearchService {
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

	public QuoteRequest getRequest(String requestId) throws SessionExpiredException, BigBangException;
	public QuoteRequest createRequest(QuoteRequest request) throws SessionExpiredException, BigBangException;
	public QuoteRequest editRequest(QuoteRequest request) throws SessionExpiredException, BigBangException;
	public void deleteRequest(String requestId) throws SessionExpiredException, BigBangException;
	public QuoteRequest closeRequest(String requestId) throws SessionExpiredException, BigBangException;
	
	public RiskAnalisys createRiskAnalisys(RiskAnalisys riskAnalisys) throws SessionExpiredException, BigBangException;
	public SecuredObject insertSecuredObject(SecuredObject object);
	
	public QuoteRequestToManagerTransfer[] transferToManager(String[] quoteRequestIds, String managerId) throws SessionExpiredException, BigBangException;
	public QuoteRequestToManagerTransfer acceptTransfer(String transferId) throws SessionExpiredException, BigBangException;
	public QuoteRequestToManagerTransfer cancelTransfer(String transferId) throws SessionExpiredException, BigBangException;
	
	public ClientInfoOrDocumentRequest createClientInfoOrDocumentRequest(ClientInfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ClientInfoOrDocumentRequest repeatClientInfoOrDocumentRequest(ClientInfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ClientInfoOrDocumentRequest receiveClientInfoOrDocumentRequestResponse(ClientInfoOrDocumentRequest.Response response) throws SessionExpiredException, BigBangException;
	public void cancelClientInfoOrDocumentRequest(ClientInfoOrDocumentRequest.Cancellation cancellation) throws SessionExpiredException, BigBangException;

}
