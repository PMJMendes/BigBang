package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestTextDelete
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
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

		Services.typifiedTextService.deleteText("3DDC3A50-1621-4701-B9EE-9FE10125FA99", callback);
	}
}
