package bigbang.tests.client;

import bigBang.definitions.shared.SubCasualty;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubCasualtyCloseProcess
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<SubCasualty> callback = new AsyncCallback<SubCasualty>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SubCasualty result)
			{
				return;
			}
		};

		Services.subCasualtyService.closeProcess("A3381236-4356-49E1-A000-A0E900648C5F", callback );
	}
}
