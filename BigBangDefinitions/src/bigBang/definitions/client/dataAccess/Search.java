package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.shared.SearchResult;

public class Search<T extends SearchResult> {
	protected String workspaceId;
	protected int totalResultsCount;
	protected int offset;
	protected int count;
	protected SearchParameter[] parameters;
	protected SortParameter[] sorts;
	protected Collection<T> obtainedresults;
	
	public Search(String workspaceId, int totalResultsCount, int offset, int count, SearchParameter[] parameters, SortParameter[] sorts, Collection<T> obtainedResults){
		this.workspaceId = workspaceId;
		this.totalResultsCount = totalResultsCount;
		this.offset = offset;
		this.count = count;
		this.parameters = parameters;
		this.obtainedresults = obtainedResults;
	}
	
	public String getWorkspaceId(){return this.workspaceId;}
	public int getTotalResultsCount(){return this.totalResultsCount;}
	public int getOffset(){return this.offset;}
	public int getCount(){return this.count;}
	public SearchParameter[] getParameters(){return this.parameters;}
	public SortParameter[] getSortParameters(){return this.sorts;}
	public Collection<T> getResults(){return this.obtainedresults;}
}
