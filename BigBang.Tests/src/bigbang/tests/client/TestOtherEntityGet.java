package bigbang.tests.client;

import bigBang.definitions.shared.OtherEntity;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestOtherEntityGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<OtherEntity[]> callback = new AsyncCallback<OtherEntity[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(OtherEntity[] result)
			{
				return;
			}
		};

		Services.otherEntityService.getOtherEntities(callback);
	}
}
