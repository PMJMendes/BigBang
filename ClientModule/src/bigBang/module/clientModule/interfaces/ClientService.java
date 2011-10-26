package bigBang.module.clientModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ClientService")
public interface ClientService
	extends SearchService 
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ClientServiceAsync instance;
		public static ClientServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ClientService.class);
			}
			return instance;
		}
	}

	public Client getClient(String clientId) throws SessionExpiredException, BigBangException;

	public Client createClient(Client client) throws SessionExpiredException, BigBangException;

	public Client editClient(Client client) throws SessionExpiredException, BigBangException;

	public Client mergeWithClient(String clientId, String receptorId) throws SessionExpiredException, BigBangException; //Returns the altered receptor client

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	public QuoteRequest createQuoteRequest(String clientId, QuoteRequest request) throws SessionExpiredException, BigBangException;
	public InsurancePolicy createPolicy(String clientId, InsurancePolicy policy) throws SessionExpiredException, BigBangException;
	public RiskAnalysis createRiskAnalisys(String clientId, RiskAnalysis riskAnalisys) throws SessionExpiredException, BigBangException;
	public Casualty createCasualty(String clientId, Casualty casualty) throws SessionExpiredException, BigBangException;

	public void deleteClient(String clientId, String processId) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;
}
