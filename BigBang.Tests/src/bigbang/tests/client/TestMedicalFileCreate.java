package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.MedicalFile;

public class TestMedicalFileCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		MedicalFile file;

		AsyncCallback<MedicalFile> callback = new AsyncCallback<MedicalFile>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(MedicalFile result)
			{
				return;
			}
		};

		file = new MedicalFile();
		file.subCasualtyId = "C433CB1A-4C3A-4A64-847C-A0FE01563CE3";
		file.nextDate = "2013-01-30";

		Services.subCasualtyService.createMedicalFile(file, callback);
	}
}
