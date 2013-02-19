package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class QuoteRequestSublineForm extends FormView<Void>{

	Button addField;
	FormViewSection sec;

	public QuoteRequestSublineForm() {

		sec = new FormViewSection("Modalidades");
		addSection(sec);
		addField = new Button("Adicionar Modalidade");
		addWidget(addField);
		((Widget)sec.getContentWrapper()).getElement().getStyle().setProperty("minHeight", "0px");
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

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(addField != null){
			addField.setVisible(!readOnly);
		}
	}

}
