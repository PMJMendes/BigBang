package bigbang.tests.client;

import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;

import com.google.gwt.core.client.GWT;

public class Services
{
	public static final AuthenticationServiceAsync authenticationService =
			GWT.create(AuthenticationService.class);
	public static final InsurancePolicyServiceAsync insurancePolicyService =
			GWT.create(InsurancePolicyService.class);
}
