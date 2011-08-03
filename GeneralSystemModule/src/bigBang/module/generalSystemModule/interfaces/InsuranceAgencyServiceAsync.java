package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.client.types.InsuranceAgency;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InsuranceAgencyServiceAsync
	extends Service
{
	void getInsuranceAgencies(AsyncCallback<InsuranceAgency[]> callback);
	void createInsuranceAgency(InsuranceAgency agency, AsyncCallback<InsuranceAgency> callback);
	void saveInsuranceAgency(InsuranceAgency agency, AsyncCallback<InsuranceAgency> callback);
	void deleteInsuranceAgency(String id, AsyncCallback<Void> callback);
}
