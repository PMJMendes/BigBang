package bigbang.tests.client;

import bigBang.definitions.shared.Mediator;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMediatorDelete
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
						if ( "57ef9286-9ee4-4257-89e0-9fdc0105c990".equalsIgnoreCase(result[i].id) )
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

		Services.mediatorService.deleteMediator(mediator.id, callback);
	}
}
