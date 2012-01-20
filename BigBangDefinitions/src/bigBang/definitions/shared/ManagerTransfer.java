package bigBang.definitions.shared;

import java.io.Serializable;

public class ManagerTransfer
	implements Serializable
{
	public static enum Status
		implements Serializable
	{
		ACCEPTED,
		CANCELED,
		PENDING,
		DIRECT
	}

	private static final long serialVersionUID = 1L;

	public String id; // ID do objecto de dados que representa a transferência de gestor, ou null se fôr um direct transfer
	public String[] managedProcessIds; // IDs dos processos a transferir
	public String[] dataObjectIds; // IDs dos respectivos objectos de dados
	public SearchResult[] objectStubs;
	public String objectTypeId; // ID do tipo de objecto de dados
	public String newManagerId;
	public boolean directTransfer; // (*) True se o newManagerId fôr o próprio utilizador
	public boolean isMassTransfer; // True se a transferência fôr em massa. Não confiar no comprimento dos arrays ser 1!
	public String processId; // ID do processo de transferência, ou null se fôr um direct transfer
	public Status status; // (*)
	// * - Por enquanto, estes dados são desconhecidos em GET

	public Permission[] permissions;
}
