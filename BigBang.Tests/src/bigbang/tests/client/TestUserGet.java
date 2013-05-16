package bigbang.tests.client;

import bigBang.definitions.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestUserGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<User[]> callback = new AsyncCallback<User[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(User[] result)
			{
				return;
			}
		};

		Services.userService.getUsers(callback);
	}
}
