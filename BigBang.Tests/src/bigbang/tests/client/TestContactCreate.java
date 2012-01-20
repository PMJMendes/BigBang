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
		contact.ownerTypeId = "D535A99E-149F-44DC-A28B-9EE600B240F5";
		contact.ownerId = "8F474C7D-DE85-4BEC-8139-9FB70020135F";
		contact.name = "Teste";
		contact.typeId = "04F6BC3C-0283-47F0-9670-9EEE013350D9";
		contact.info = new ContactInfo[1];
		contact.info[0] = new ContactInfo();
		contact.info[0].typeId = "96467849-6FE1-4113-928C-9EDF00F40FB9";
		contact.info[0].value = "joao.mendes@premium.minds.com";
		Services.contactsService.createContact(contact, callback);
	}
}
