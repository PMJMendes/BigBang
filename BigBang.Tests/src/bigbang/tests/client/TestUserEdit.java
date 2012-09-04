package bigbang.tests.client;

import bigBang.definitions.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestUserEdit
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
						if ( "238dbe6a-3285-4161-9c66-9fdc0107fcb2".equalsIgnoreCase(result[i].id) )
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
		AsyncCallback<User> callback = new AsyncCallback<User>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(User result)
			{
				return;
			}
		};

		user.name = "Teste";
		user.email = "teste@invalid";
		Services.userService.saveUser(user, callback);
	}
}
