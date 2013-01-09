package bigBang.module.casualtyModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class MedicalFileSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		REFERENCE,
		NEXT_DATE
	}

	public MedicalFileSortParameter(){}

	public MedicalFileSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
