package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.User;

public class TestUserDelete
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
				int i;

				if ( result == null )
					return;
				else
				{
					for ( i = 0; i < result.length; i++ )
					{
						if ( "57ef9286-9ee4-4257-89e0-9fdc0105c990".equalsIgnoreCase(result[i].id) )
						{
							DoStep2(result[i]);
							break;
						}
					}
				}
			}
		};

		Services.userService.getUsers(callback);
	}

	private static void DoStep2(User user)
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				return;
			}
		};

		Services.userService.deleteUser(user, callback);
	}
}
