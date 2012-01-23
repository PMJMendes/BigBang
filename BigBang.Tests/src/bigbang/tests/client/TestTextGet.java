package bigbang.tests.client;

import bigBang.definitions.shared.TypifiedText;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestTextGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<TypifiedText[]> callback = new AsyncCallback<TypifiedText[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(TypifiedText[] result)
			{
				return;
			}
		};

		Services.typifiedTextService.getTexts("TEST", callback);
	}
}
