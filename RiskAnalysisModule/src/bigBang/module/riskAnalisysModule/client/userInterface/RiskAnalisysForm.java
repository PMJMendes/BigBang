package bigBang.module.riskAnalisysModule.client.userInterface;

import bigBang.definitions.shared.RiskAnalisys;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class RiskAnalisysForm extends FormView<RiskAnalisys> {

	protected TextBoxFormField number;
	protected AddressFormField address;
	protected TextBoxFormField clientName;
	protected TextBoxFormField clientNumber;
	
	public RiskAnalisysForm(){
		number = new TextBoxFormField("Número");
		address = new AddressFormField();
		clientName = new TextBoxFormField("Nome");
		clientNumber = new TextBoxFormField("Número");
		
		addSection("Informação Geral");
		addFormField(number);
		addFormField(address);
		
		addSection("Cliente");
		addFormField(clientName);
		addFormField(clientNumber);
	}
	
	@Override
	public RiskAnalisys getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(RiskAnalisys info) {
		// TODO Auto-generated method stub
		
	}

}
