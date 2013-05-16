package bigbang.tests.client;

import bigBang.definitions.shared.OtherEntity;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestOtherEntityCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		OtherEntity entity;

		AsyncCallback<OtherEntity> callback = new AsyncCallback<OtherEntity>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(OtherEntity result)
			{
				return;
			}
		};

		entity = new OtherEntity();
		entity.name = "Loja do Manel";
		entity.type = "22D9D54E-1B37-4936-B097-A14801381F4E";

		Services.otherEntityService.createOtherEntity(entity, callback);
	}
}
