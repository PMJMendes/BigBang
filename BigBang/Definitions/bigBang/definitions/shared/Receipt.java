package bigBang.definitions.shared;

import java.io.Serializable;

public class Receipt
	extends ReceiptStub
{
	private static final long serialVersionUID = 1L;

	public static class ReturnMessage
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String receiptId;
		public String subject; //Subject e text vêm do typified texts
		public String text;
	}

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
	public String inheritMediatorId;
	public String inheritMediatorName;
	public Contact[] contacts;
	public Document[] documents;

	public Receipt()
	{
		contacts = new Contact[0];
		documents = new Document[0];
	}
}
