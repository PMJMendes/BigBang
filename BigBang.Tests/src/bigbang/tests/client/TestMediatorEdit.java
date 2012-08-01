package bigbang.tests.client;

import bigBang.definitions.shared.Mediator;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMediatorEdit
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Mediator[]> callback = new AsyncCallback<Mediator[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Mediator[] result)
			{
				int i;

				if ( result == null )
					return;
				else
				{
					for ( i = 0; i < result.length; i++ )
					{
						if ( "DF5EA5BF-B5D4-4F2A-A9A0-A08F0141D105".equalsIgnoreCase(result[i].id) )
						{
							DoStep2(result[i]);
							break;
						}
					}
				}
			}
		};

		Services.mediatorService.getMediators(callback);
	}

	private static void DoStep2(Mediator mediator)
	{
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

		mediator.comissionProfile.id = "CECC8014-200C-4C4F-9F47-9EFC01368139";
		mediator.basePercent = 87.0;
		Services.mediatorService.saveMediator(mediator, callback);
	}
}
