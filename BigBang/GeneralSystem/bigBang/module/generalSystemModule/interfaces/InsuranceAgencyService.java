package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsuranceAgencyService")
public interface InsuranceAgencyService
	extends RemoteService
{
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
	
	public InsuranceAgency[] getInsuranceAgencies() throws SessionExpiredException, BigBangException;
	public InsuranceAgency createInsuranceAgency(InsuranceAgency agency) throws SessionExpiredException, BigBangException;
	public InsuranceAgency saveInsuranceAgency(InsuranceAgency agency) throws SessionExpiredException, BigBangException;
	public void deleteInsuranceAgency(String id) throws SessionExpiredException, BigBangException;
}
