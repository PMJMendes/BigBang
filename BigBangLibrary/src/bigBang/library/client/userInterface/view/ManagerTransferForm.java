package bigBang.library.client.userInterface.view;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.User;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.TextBoxFormField;

public class ManagerTransferForm extends FormView<ManagerTransfer>{
	
	private TextBoxFormField name;
	private TextBoxFormField numObjects;
	private TextBoxFormField objectType;

	public ManagerTransferForm(){
		
		addSection("Informação Geral");
		
		this.name = new TextBoxFormField("Nome do novo gestor");
		this.numObjects = new TextBoxFormField("Nº. de Objectos");
		this.objectType = new TextBoxFormField("Tipo de Processos");
		
		addFormField(name, true);
		addFormField(numObjects, true);
		addFormField(objectType, true);
		
		setReadOnly(true);
	}

	@Override
	public ManagerTransfer getInfo() {
		return this.value;
	}

	@Override
	public void setInfo(ManagerTransfer info) {
		UserBroker broker = (UserBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.USER);
		broker.getUser(info.newManagerId, new ResponseHandler<User>() {
			
			@Override
			public void onResponse(User response) {
				name.setValue(response.name);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				name.setValue("");
			}
		});
		numObjects.setValue(info.dataObjectIds.length+"");
	}

	public void setObjectType(String type){
		objectType.setValue(type);
	}

}
