package bigBang.definitions.shared;

public class ReceiptStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public static enum ReceiptStatus
	{
		NEW,
		PAYABLE,
		PAID,
		ERROR
	}

	public String number;
	public String clientId;
	public String clientNumber;
	public String clientName;
	public String insurerId;
	public String insurerName;
	public String policyId;
	public String policyNumber;
	public String categoryId;
	public String categoryName;
	public String lineId;
	public String lineName;
	public String subLineId;
	public String subLineName;
	public String typeId;
	public String typeName;
	public Double totalPremium;
	public String maturityDate;
	public String endDate;
	public String description;
	public String statusId;
	public String statusText;
	public ReceiptStatus statusIcon;
}
