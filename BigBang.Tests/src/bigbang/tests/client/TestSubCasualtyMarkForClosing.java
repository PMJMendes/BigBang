package bigbang.tests.client;

import bigBang.definitions.shared.SubCasualty;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubCasualtyMarkForClosing
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

		Services.subCasualtyService.markForClosing("A3381236-4356-49E1-A000-A0E900648C5F", "519AC171-DE97-4AE1-BA99-A0E8018488BD", callback );
	}
}
