package bigBang.definitions.client.types;

import bigBang.library.shared.Address;
import bigBang.library.shared.Contact;
import bigBang.library.shared.Document;

public class Client
	extends ClientStub
{
	private static final long serialVersionUID = 1L;

	public String groupId;
	public String taxNumber;
	public Address address;
	public String NIB;
	public String typeId;
	public String subtypeId;
	public String mediatorId;
	public String managerId;
	public String operationalProfileId;
	public String caeId;
	public String activityNotes;
	public String sizeId;
	public String revenueId;
	public String birthDate;
	public String genderId;
	public String maritalStatusId;
	public String professionId;
	public String notes;
	public Contact[] contacts;
	public Document[] documents;

	public String processId;

//JMMM: Até ver, o email primário irá para dentro dos contactos.

	public Client()
	{
		contacts = new Contact[0];
		documents = new Document[0];
	}
}
