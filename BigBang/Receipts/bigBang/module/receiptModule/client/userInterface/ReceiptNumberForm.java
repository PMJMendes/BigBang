package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;



public class ReceiptNumberForm extends FormView<String>{

	private TextBoxFormField number;
	
	public ReceiptNumberForm(){
		
		addSection("Número do recibo");
		number = new TextBoxFormField("Número do recibo"); 
		number.setFieldWidth("175px");
		addFormField(number, true);
		Button okButton = new Button("Verificar");
		addWidget(okButton);
		
	}
	@Override
	public String getInfo() {
		return number.getValue();
	}
	@Override
	public void setInfo(String info) {
		
		number.setValue(info);
		
	}

}
