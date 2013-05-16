package bigbang.tests.client;

import bigBang.definitions.shared.Permission;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialGetGlobalPermissions
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

		Services.permissionService.getGeneralOpPermissions("D535A99E-149F-44DC-A28B-9EE600B240F5", callback);
	}
}
