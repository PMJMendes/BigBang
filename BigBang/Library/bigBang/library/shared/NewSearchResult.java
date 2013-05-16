package bigBang.library.shared;

import java.io.Serializable;

import bigBang.definitions.shared.SearchResult;

public class NewSearchResult extends SearchResult
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String workspaceId;
	public int totalCount;
	public SearchResult[] results;
}
