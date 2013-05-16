package bigbang.tests.client;

import bigBang.definitions.shared.Line;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestTaxGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Line[]> callback = new AsyncCallback<Line[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Line[] result)
			{
				return;
			}
		};

		Services.coveragesService.getLines(callback);
	}
}
