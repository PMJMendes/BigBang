package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.ManagerTransfer.Status;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.TextBoxFormField;

public class ManagerTransferForm extends FormView<ManagerTransfer>{
	
	private String id; // ID do objecto de dados que representa a transferência de gestor, ou null se fôr um direct transfer
	private String[] managedProcessIds; // IDs dos processos a transferir
	private String[] dataObjectIds; // IDs dos respectivos objectos de dados
	private SearchResult[] objectStubs;
	private String objectTypeId; // ID do tipo de objecto de dados
	private String newManagerId;
	private boolean directTransfer; // (*) True se o newManagerId fôr o próprio utilizador
	private boolean isMassTransfer; // True se a transferência fôr em massa. Não confiar no comprimento dos arrays ser 1!
	private String processId; // ID do processo de transferência, ou null se fôr um direct transfer
	private Status status; // (*)
	
	private FormViewSection sect;
	private TextBoxFormField name;
	private TextBoxFormField numObjects;
	private TextBoxFormField objectType;
	private TextBoxFormField processStatus;

	public ManagerTransferForm(){
		
		sect = new FormViewSection("Informação Geral");		
		addSection(sect);
		
		this.name = new TextBoxFormField("Nome do novo gestor");
		this.numObjects = new TextBoxFormField("Nº. de Objectos");
		this.objectType = new TextBoxFormField("Tipo de Objectos");
		this.processStatus = new TextBoxFormField("Estado");
		
		
	}
	@Override
	public ManagerTransfer getInfo() {
		// TODO Auto-generated method stub
		return this.value;
	}

	@Override
	public void setInfo(ManagerTransfer info) {
		
		sect.clear();
		
		this.id = info.id;
		this.managedProcessIds = info.managedProcessIds;
		this.directTransfer = info.directTransfer;
		this.dataObjectIds = info.dataObjectIds;
		this.isMassTransfer = info.isMassTransfer;
		this.objectStubs = info.objectStubs;
		this.objectTypeId = info.objectTypeId;
		this.newManagerId = info.newManagerId;
		this.processId = info.processId;
		this.status = info.status;
		
		name.setValue(this.newManagerId);
		numObjects.setValue(dataObjectIds.length+"");
		processStatus.setValue(getStatus(info.status));
		
		name.setEditable(false);
		numObjects.setEditable(false);
		objectType.setEditable(false);
		processStatus.setEditable(false);
		
		addFormField(name, true);
		addFormField(numObjects, true);
		addFormField(objectType, true);
		addFormField(processStatus, true);
		
	}
	
	private String getStatus(Status status) {
		
		switch(status){
			case ACCEPTED: return "Aceite";
			case CANCELED: return "Cancelado";
			case DIRECT:return "Directo";
			default: return "Pendente";
		}
	
	}
	public void setObjectType(String type){
		
		objectType.setValue(type);
	}

}
