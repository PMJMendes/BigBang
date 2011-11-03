package bigBang.definitions.shared;

public class Receipt
	extends ReceiptStub
{
	private static final long serialVersionUID = 1L;
	
	public String salesPremium;
	public String comissions;
	public String retrocessions;
	public String FATValue;
	public String issueDate;
	public String endDate;
	public String dueDate;
	public String mediatorId;
	public String notes;
	public String managerId;
	public Contact[] contacts;
	public Document[] documents;

	public Receipt()
	{
		contacts = new Contact[0];
		documents = new Document[0];
	}
}
