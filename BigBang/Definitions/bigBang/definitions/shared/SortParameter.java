package bigBang.definitions.shared;

import java.io.Serializable;

public abstract class SortParameter implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Enum<?> field;
	public SortOrder order;
	
	public SortParameter(){}
	
	public SortParameter(Enum<?> field, SortOrder order) {
		this.field = field;
		this.order = order;
	}

}
