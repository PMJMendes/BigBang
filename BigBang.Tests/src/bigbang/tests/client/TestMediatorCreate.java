package bigbang.tests.client;

import java.util.HashMap;

import bigBang.definitions.shared.CommissionProfile;
import bigBang.definitions.shared.Mediator;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMediatorCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Mediator mediator;

		AsyncCallback<Mediator> callback = new AsyncCallback<Mediator>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Mediator result)
			{
				return;
			}
		};

		mediator = new Mediator();
		mediator.name = "Peste";
		mediator.comissionProfile = new CommissionProfile();
		mediator.comissionProfile.id = "C7236BA7-73AD-40ED-B6DC-9EFC013691C8";
		mediator.dealPercents = new HashMap<String, Double>();
		mediator.dealPercents.put("BEBB58B5-CD95-4872-B72F-9EE90118938F", 4.0);
		mediator.dealPercents.put("CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9", 3.0);

		Services.mediatorService.createMediator(mediator, callback);
	}
}
