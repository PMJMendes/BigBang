package bigBang.module.generalSystemModule.interfaces;

import bigBang.module.generalSystemModule.shared.InsuranceAgency;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsuranceAgencyService")
public interface InsuranceAgencyService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static InsuranceAgencyServiceAsync instance;
		public static InsuranceAgencyServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(InsuranceAgencyService.class);
			}
			return instance;
		}
	}
	
	public InsuranceAgency[] getInsuranceAgencies();
	
	public InsuranceAgency getInsuranceAgency();
	
	public InsuranceAgency createInsuranceAgency(InsuranceAgency agency);
	
	public String deleteInsuranceAgency(String id);
	
	public String saveInsuranceAgency(InsuranceAgency agency);

}
