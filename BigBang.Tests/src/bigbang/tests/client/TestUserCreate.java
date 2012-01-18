package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.User;
import bigBang.definitions.shared.UserProfile;

public class TestUserCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		User user;

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

		user = new User();
		user.name = "Peste";
		user.username = "teste";
		user.password = "teste";
		user.profile = new UserProfile();
		user.profile.id = "258A1C88-C916-40CB-8CD5-9EB8007F2AEB";
		user.costCenterId = "BCBB8674-B008-453A-912C-9FB700200FB2";
		user.email = "teste@teste.invalid";

		Services.userService.addUser(user, callback);
	}
}
