package bigBang.definitions.client.types;

import bigBang.library.shared.SearchResult;

public class ClientStub	
	extends SearchResult
{	
	private static final long serialVersionUID = 1L;

	public String name;
	public String clientNumber;
	public String groupName;
	
	public ClientStub(){}
	
	public ClientStub(ClientStub original) {
		this.id = original.id;
		this.name = original.id;
		this.clientNumber = original.clientNumber;
		this.groupName = original.groupName;
	}
	
}
