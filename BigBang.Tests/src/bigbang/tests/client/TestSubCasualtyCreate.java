package bigbang.tests.client;

import bigBang.definitions.shared.SubCasualty;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubCasualtyCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		SubCasualty subCasualty;

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

		subCasualty = new SubCasualty();
		subCasualty.casualtyId = "CA38CF67-FB1C-4213-9ABE-A03C011459A1";
		subCasualty.referenceId = "01B7B31C-0EFF-4A93-84EE-A02000F26B1F";
		subCasualty.referenceTypeId = "D0C5AE6B-D340-4171-B7A3-9F81011F5D42";
		subCasualty.hasJudicial = false;
		subCasualty.text = "Escorregou, partiu um pé.";
		subCasualty.internalNotes = "Salta agora só com um pé...";
		subCasualty.insuredObjectId = "C264CBDC-CEB3-4966-9363-A037011F9C0D";

		subCasualty.items = new SubCasualty.SubCasualtyItem[2];
		subCasualty.items[0] = new SubCasualty.SubCasualtyItem();
		subCasualty.items[0].coverageId = "23E2CC58-D2EE-4329-A687-A02000F26B2B";
		subCasualty.items[0].damageTypeId = "FC2B4BB5-C210-4DC9-9CB0-A0370120AA98";
		subCasualty.items[0].damages = 2054.45;
		subCasualty.items[0].settlement = null;
		subCasualty.items[0].isManual = false;
		subCasualty.items[1] = new SubCasualty.SubCasualtyItem();
		subCasualty.items[1].coverageId = "23E2CC58-D2EE-4329-A687-A02000F26B2B";
		subCasualty.items[1].damageTypeId = "FC2B4BB5-C210-4DC9-9CB0-A0370120AA98";
		subCasualty.items[1].damages = 1854.45;
		subCasualty.items[1].settlement = null;
		subCasualty.items[1].isManual = false;

		Services.casualtyService.createSubCasualty(subCasualty, callback);
	}
}
