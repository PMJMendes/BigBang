package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalisys;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("QuoteRequestService")
public interface QuoteRequestService
	extends SearchService
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

	public QuoteRequest getRequest(String requestId) throws SessionExpiredException, BigBangException;

	public QuoteRequest editRequest(QuoteRequest request) throws SessionExpiredException, BigBangException;
	public InsuredObject insertInsuredObject(InsuredObject object);
	public QuoteRequest closeRequest(String requestId) throws SessionExpiredException, BigBangException;
	
	public Negotiation createNegotiation(Negotiation negotiation) throws SessionExpiredException, BigBangException;
	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ManagerTransfer[] createManagerTransfer(String[] quoteRequestIds, String managerId) throws SessionExpiredException, BigBangException;

	public RiskAnalisys createRiskAnalisys(RiskAnalisys riskAnalisys) throws SessionExpiredException, BigBangException;

	public void deleteRequest(String requestId) throws SessionExpiredException, BigBangException;
}
