package bigBang.module.receiptModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class ReceiptSortParameter extends SortParameter {

	private static final long serialVersionUID = 1L;
	
	public static enum SortableField
	{
		RELEVANCE
	}
	
	public ReceiptSortParameter(){}
	
	public ReceiptSortParameter(SortableField field, SortOrder order){
		this.field = field;
		this.order = order;
	}

}
