package bigbang.tests.client;

import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectServiceAsync;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.interfaces.ReceiptServiceAsync;

import com.google.gwt.core.client.GWT;

public class Services
{
	public static final AuthenticationServiceAsync authenticationService =
			GWT.create(AuthenticationService.class);
	public static final ClientServiceAsync clientService =
			GWT.create(ClientService.class);
	public static final InsurancePolicyServiceAsync insurancePolicyService =
			GWT.create(InsurancePolicyService.class);
	public static final PolicyObjectServiceAsync policyObjectService =
			GWT.create(PolicyObjectService.class);
	public static final ReceiptServiceAsync receiptService =
			GWT.create(ReceiptService.class);
}
