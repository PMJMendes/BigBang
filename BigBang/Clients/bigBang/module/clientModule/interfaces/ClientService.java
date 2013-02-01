package bigBang.module.clientModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.ManagerTransfer;
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
	public Client setInternational(String clientId) throws SessionExpiredException, BigBangException;

	public Client mergeWithClient(String clientId, String receptorId) throws SessionExpiredException, BigBangException; //Returns the altered receptor client

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public InsurancePolicy createPolicy(InsurancePolicy policy) throws SessionExpiredException, BigBangException;
	public RiskAnalysis createRiskAnalisys(RiskAnalysis riskAnalisys) throws SessionExpiredException, BigBangException;
	public Casualty createCasualty(Casualty casualty) throws SessionExpiredException, BigBangException;

	public void deleteClient(String clientId, String reason) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;
}
