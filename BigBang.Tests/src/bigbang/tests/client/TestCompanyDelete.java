package bigbang.tests.client;

import bigBang.definitions.shared.InsuranceAgency;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestCompanyDelete
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
				if ( result.length > 0 )
					DoStep2(result[result.length / 2].id);
			}
		};

		Services.insuranceAgencyService.getInsuranceAgencies(callback);
	}

	private static void DoStep2(String id)
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				return;
			}
		};

		Services.insuranceAgencyService.deleteInsuranceAgency(id, callback);
	}
}
