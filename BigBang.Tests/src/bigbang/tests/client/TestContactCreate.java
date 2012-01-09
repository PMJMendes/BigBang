package bigbang.tests.client;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestContactCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Contact contact;

		AsyncCallback<Contact> callback = new AsyncCallback<Contact>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Contact result)
			{
				return;
			}
		};

		contact = new Contact();
		contact.ownerTypeId = "7B203DCA-FFAC-46B2-B849-9EBC009DB127";
		contact.ownerId = "57C7551F-B640-495B-8091-9FB700200FB8";
		contact.name = "Teste";
		contact.typeId = "07367032-3A5D-499D-88BD-9EEE013357C9";
		contact.info = new ContactInfo[1];
		contact.info[0] = new ContactInfo();
		contact.info[0].typeId = "96467849-6FE1-4113-928C-9EDF00F40FB9";
		contact.info[0].value = "teste@com.invalid";
		Services.contactsService.createContact(contact, callback);
	}
}
