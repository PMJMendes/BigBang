package bigBang.library.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class ConversationSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		SUBJECT,
		TYPE,
		PENDINGDATE
	}

	public ConversationSortParameter(){}

	public ConversationSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
