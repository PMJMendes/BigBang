package bigbang.tests.client;

import bigBang.definitions.shared.ZipCode;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialZipCodeGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<ZipCode> callback = new AsyncCallback<ZipCode>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ZipCode result)
			{
				return;
			}
		};

		Services.zipCodeService.getZipCode("1500-088", callback);
	}
}
