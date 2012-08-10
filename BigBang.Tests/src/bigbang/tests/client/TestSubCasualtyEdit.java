package bigbang.tests.client;

import bigBang.definitions.shared.SubCasualty;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubCasualtyEdit
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
				DoStep2(result);
			}
		};

		Services.subCasualtyService.getSubCasualty("B8A20B89-6AC3-4538-A07C-A03701260645", callback);
	}

	private static void DoStep2(SubCasualty subCasualty)
	{
		SubCasualty.SubCasualtyItem[] larrItems;
		int i;

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

		subCasualty.internalNotes = "Otário...";

		larrItems = new SubCasualty.SubCasualtyItem[subCasualty.items.length + 1];
		for ( i = 0; i < subCasualty.items.length; i++ )
			larrItems[i] = subCasualty.items[i];

		larrItems[0].deleted = true;
		larrItems[1].damages = 500.0;
		larrItems[1].settlement = 450.0;
		larrItems[1].isManual = true;
		larrItems[2] = new SubCasualty.SubCasualtyItem();
		larrItems[2].coverageId = "23E2CC58-D2EE-4329-A687-A02000F26B2B";
		larrItems[2].damageTypeId = "FC2B4BB5-C210-4DC9-9CB0-A0370120AA98";
		larrItems[2].damages = 600.0;
		larrItems[2].settlement = 550.0;
		larrItems[2].isManual = true;

		subCasualty.items = larrItems;

		Services.subCasualtyService.editSubCasualty(subCasualty, callback);
	}
}
