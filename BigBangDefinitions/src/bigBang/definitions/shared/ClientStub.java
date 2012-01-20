package bigBang.definitions.shared;

public class ClientStub	
	extends ProcessBase
{	
	private static final long serialVersionUID = 1L;

	public String name;
	public String clientNumber;
	public String groupName;

	public String processId;

	public ClientStub(){}
	
	public ClientStub(ClientStub original) {
		this.id = original.id;
		this.name = original.id;
		this.clientNumber = original.clientNumber;
		this.groupName = original.groupName;
	}
	
}
