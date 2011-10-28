package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Receipt;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsurancePolicyService")
public interface InsurancePolicyService extends SearchService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static InsurancePolicyServiceAsync instance;
		public static InsurancePolicyServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(InsurancePolicyService.class);
			}
			return instance;
		}
	}
	
	public InsurancePolicy getPolicy(String policyId) throws SessionExpiredException, BigBangException;
	public InsurancePolicy voidPolicy(String policyId) throws SessionExpiredException, BigBangException;
	public void deletePolicy(String policyId) throws SessionExpiredException, BigBangException;
	public InsurancePolicy editPolicy(InsurancePolicy policy) throws SessionExpiredException, BigBangException;
	
	public Receipt createReceipt(String policyId, Receipt receipt) throws SessionExpiredException, BigBangException;

	
	
	//public RiskAnalisys createRiskAnalisys(String policyId, RiskAnalisys riskAnalisys) throws SessionExpiredException, BigBangException;
	//public 
	
}
