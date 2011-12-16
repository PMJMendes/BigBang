package bigBang.module.receiptModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class ReceiptSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;
	
	public static enum SortableField
	{
		RELEVANCE,
		TYPE,
		NUMBER,
		CLIENT,
		SUB_LINE,
		EMISSION_DATE,
		LIMIT_DATE,
		MATURITY_DATE,
		PAYMENT_DATE
	}
	
	public ReceiptSortParameter(){}
	
	public ReceiptSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
