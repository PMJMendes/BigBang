package bigBang.definitions.shared;

public class ManagerTransfer
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public String newManagerId;
	public String objectTypeId; // ID do tipo de objecto de dados
	public String[] dataObjectIds; // IDs dos respectivos objectos de dados
	public SearchResult[] objectStubs;
	public boolean isMassTransfer; // True se a transferência fôr em massa. Não confiar no comprimento dos arrays ser 1!
}
