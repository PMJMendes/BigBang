package bigbang.tests.client;

import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.interfaces.ReceiptServiceAsync;

import com.google.gwt.core.client.GWT;

public class Services
{
	public static final AuthenticationServiceAsync authenticationService =
			GWT.create(AuthenticationService.class);
	public static final InsurancePolicyServiceAsync insurancePolicyService =
			GWT.create(InsurancePolicyService.class);
	public static final ReceiptServiceAsync receiptService =
			GWT.create(ReceiptService.class);
}
