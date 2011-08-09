package bigBang.library.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public abstract class SearchParameter
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public String freeText;
	
	public Collection<SortParameter> sortables;
	
	public SearchParameter(){
		sortables = new ArrayList<SortParameter>();
	}
}
