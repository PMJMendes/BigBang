package bigbang.tests.client;

import bigBang.definitions.shared.InsuranceAgency;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestCompanyGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<InsuranceAgency[]> callback = new AsyncCallback<InsuranceAgency[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuranceAgency[] result)
			{
				return;
			}
		};

		Services.insuranceAgencyService.getInsuranceAgencies(callback);
	}
}
