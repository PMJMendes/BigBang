package bigbang.tests.client;

import bigBang.definitions.shared.MedicalFile;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMedicalFileEdit
{

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<MedicalFile> callback = new AsyncCallback<MedicalFile>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(MedicalFile result)
			{
				DoStep2(result);
			}
		};

		Services.medicalFileService.getMedicalFile("2CC1DFAF-4B2E-4778-B40C-A1420103B6E0", callback);
	}

	private static void DoStep2(MedicalFile medicalFile)
	{
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

		medicalFile.nextDate = "2013-01-30";

		Services.medicalFileService.editMedicalFile(medicalFile, callback);
	}
}
