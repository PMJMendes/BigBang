package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

public class QuoteRequestSublineForm extends FormView<Void>{

	Button addField;
	
	public QuoteRequestSublineForm() {
		
		addSection("Modalidades");
		addField = new Button("Adicionar Modalidade");
		addWidget(addField);
		
	}

	public HasClickHandlers getAddFieldButton(){
		return addField;
	}

	@Override
	public void setInfo(Void info) {
		return;
	}

	public Void getInfo() {
		return null;
	}

}
