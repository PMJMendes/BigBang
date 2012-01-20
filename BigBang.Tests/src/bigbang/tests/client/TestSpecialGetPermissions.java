package bigbang.tests.client;

import bigBang.definitions.shared.Permission;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialGetPermissions
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Permission[]> callback = new AsyncCallback<Permission[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Permission[] result)
			{
				return;
			}
		};

		Services.permissionService.getProcessPermissions("31B97CAC-CADD-4F9F-8D62-9FB700201362", callback);
	}
}
