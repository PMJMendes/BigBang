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

		Services.subCasualtyService.markForClosing("9D0660C4-D6A8-48D2-9D65-A03C0116D5AF", "091B8442-B7B0-40FA-B517-9EB00068A390", callback );
	}
}
