package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class NegotiationDeleteForm extends FormView<Deletion>{

	private TextAreaFormField motive;

							
	public NegotiationDeleteForm(){
		
		motive = new TextAreaFormField("Motivo");
		
		addSection("Eliminação de Negociação");
		addFormField(motive);
		
	}
	
	@Override
	public Deletion getInfo() {
		Deletion deletion = new Deletion();
		deletion = value;
		deletion.motive = motive.getValue();
		return deletion;
	}

	@Override
	public void setInfo(Deletion info) {
		
		value = info;
		motive.setValue(info.motive);
		
	}

	
	
}
