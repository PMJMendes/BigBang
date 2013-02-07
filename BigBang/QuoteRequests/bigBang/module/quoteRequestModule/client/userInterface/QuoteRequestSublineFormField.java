package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.shared.FieldContainer;
import bigBang.library.client.userInterface.view.FormView;

public class QuoteRequestSublineFormField extends FormView<FieldContainer[]>{

	List<QuoteRequestSublineFormSection> fields;
	Button addField;
	
	public QuoteRequestSublineFormField() {
		
		addSection("Modalidades");
		addField = new Button("Adicionar Modalidade");
		addWidget(addField);
		
	}
	
	@Override
	public FieldContainer[] getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(FieldContainer[] info) {

		for(FieldContainer field : info){
			QuoteRequestSublineFormSection section = new QuoteRequestSublineFormSection("Modalidade X");
			section.setValue(field);
			fields.add(section);
			addSection(section);
		}
	
	}

	HasClickHandlers getAddFieldButton(){
		return addField;
	}

}
