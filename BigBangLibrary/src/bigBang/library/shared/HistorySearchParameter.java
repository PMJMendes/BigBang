package bigBang.library.shared;

import java.io.Serializable;

public class HistorySearchParameter extends SearchParameter {
	
	public static enum SortableField implements Serializable{
		TIMESTAMP
	}

	private static final long serialVersionUID = 1L;

	public String processId;
	public String afterTimestamp;
}
