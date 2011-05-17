package bigBang.library.shared;

import java.io.Serializable;

public class NewSearchResult
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	String workspaceId;
	SearchResult[] results;
}
