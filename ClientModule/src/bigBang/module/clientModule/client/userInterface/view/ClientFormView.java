package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.clientModule.shared.ClientFormValidator;
import bigBang.module.clientModule.shared.ClientProcess;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientFormView extends FormView {
	
	public ClientFormView() {
		super();
		addTitleSection("Cliente 1", "Nº 23445566", null);
		
		addSection("Informação Geral");
		
		TextBoxFormField clientNameField = new TextBoxFormField("Nome", new ClientFormValidator.ClientNameValidator());
		addFormField(clientNameField);
		
		TextBoxFormField clientNameField2 = new TextBoxFormField("Nome", new ClientFormValidator.ClientNameValidator());
		addFormField(clientNameField2);
		
		TextBoxFormField clientNameField3 = new TextBoxFormField("Nome", new ClientFormValidator.ClientNameValidator());
		addFormField(clientNameField3);
		
		addSection("Moradas");
		
		//addDisclosureSection("Contactos", getFormContact());
		
		//addWidget(new Button("button"));
		//setReadOnly(true);
		
		Button submitButton = new Button("Submeter");
		addSection((String)null);
		addWidget(submitButton);
		submitButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				validate();
			}
		});
	}

	public void showProcess(ClientProcess process) {
		this.addTitleSection(process.getDescription(), process.getId(), null);
	}
	
	private Widget getFormContact(){
		VerticalPanel contactWrapper = new VerticalPanel();
		return contactWrapper;
	}
	
}
