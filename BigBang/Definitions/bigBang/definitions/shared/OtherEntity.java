package bigBang.definitions.shared;

import java.io.Serializable;

public class OtherEntity
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String type;
	public String typeLabel;
	public String notes;
	public Contact[] contacts;
	public Document[] documents;

	public OtherEntity()
	{
		contacts = new Contact[0];
		documents = new Document[0];
	}
	
	public OtherEntity(OtherEntity original)
	{
		this.id = original.id;
		this.name = original.name;
		this.type = original.type;
		this.typeLabel = original.typeLabel;
		this.notes = original.notes;
		this.contacts = original.contacts;
		this.documents = original.documents;
	}
}
