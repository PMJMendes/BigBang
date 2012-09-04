package bigbang.tests.client;

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
//		mediator.username = "teste";
//		mediator.password = "teste";
//		mediator.profile = new UserProfile();
//		mediator.profile.id = "258A1C88-C916-40CB-8CD5-9EB8007F2AEB";
//		mediator.costCenterId = "BCBB8674-B008-453A-912C-9FB700200FB2";
//		mediator.email = "teste@teste.invalid";

		Services.mediatorService.createMediator(mediator, callback);
	}
}
