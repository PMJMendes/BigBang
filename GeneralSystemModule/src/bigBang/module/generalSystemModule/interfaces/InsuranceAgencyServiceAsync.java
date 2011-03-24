package bigBang.module.generalSystemModule.interfaces;

import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InsuranceAgencyServiceAsync extends Service {

	void createInsuranceAgency(InsuranceAgency agency,
			AsyncCallback<InsuranceAgency> callback);

	void deleteInsuranceAgency(String id, AsyncCallback<String> callback);

	void getInsuranceAgencies(AsyncCallback<InsuranceAgency[]> callback);

	void getInsuranceAgency(AsyncCallback<InsuranceAgency> callback);

	void saveInsuranceAgency(InsuranceAgency agency,
			AsyncCallback<String> callback);

}
