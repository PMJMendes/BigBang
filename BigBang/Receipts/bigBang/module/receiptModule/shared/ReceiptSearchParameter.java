package bigBang.module.receiptModule.shared;

import bigBang.definitions.shared.SearchParameter;

public class ReceiptSearchParameter
	extends SearchParameter
{
	private static final long serialVersionUID = 1L;

	public String companyId;
	public String mediatorId;
	public String ownerId;
	public String[] typeIds;
	
	//Dates
	public String emitedFrom;
	public String emitedTo;
	public String maturityFrom;
	public String maturityTo;
	public String paymentFrom;
	public String paymentTo;
	
	public String categoryId;
	public String lineId;
	public String subLineId;
}
