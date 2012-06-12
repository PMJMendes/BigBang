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

	public static class PaymentInfo
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public static class Payment
			implements Serializable
		{
			private static final long serialVersionUID = 1L;

			public String paymentTypeId; // Obrigatorio
			public String value; // Obrigatorio
			public String bankId; // Só é útil para transferências e cheques, e mesmo assim, pode vir a null
			public String chequeOrTransferNumber;  // Só é útil para transferências e cheques, e mesmo assim, pode vir a null
			public String otherReceiptId; // Só é útil para conta corrente ou compensação 
			public boolean markOtherAsPayed; // Só tem significado se o otherReceiptId estiver preenchido
		}

		public String receiptId;
		public Payment[] payments;
	}

	public Double salesPremium;
	public Double comissions;
	public Double retrocessions;
	public Double FATValue;
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
