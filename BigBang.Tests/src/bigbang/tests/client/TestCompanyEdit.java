package bigbang.tests.client;

import bigBang.definitions.shared.InsuranceAgency;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestCompanyEdit
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
				int i;

				if ( result.length > 0 )
				{
					for ( i = 0; i < result.length; i++ )
					{
						if ( result[i].name.startsWith("x") )
						{
							DoStep2(result[i]);
							return;
						}
					}
	
					DoStep2(result[0]);
				}
			}
		};

		Services.insuranceAgencyService.getInsuranceAgencies(callback);
	}

	private static void DoStep2(InsuranceAgency agency)
	{
		AsyncCallback<InsuranceAgency> callback = new AsyncCallback<InsuranceAgency>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuranceAgency result)
			{
				return;
			}
		};

		if ( agency.name.startsWith("x") )
			agency.name = agency.name.substring(1);
		else
			agency.name = "x" + agency.name;

		Services.insuranceAgencyService.saveInsuranceAgency(agency, callback);
	}
}
