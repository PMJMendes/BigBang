package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.clientModule.shared.ClientProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class ClientFormView extends FormView<ClientProxy> {
	
	private TextBoxFormField name;
	private TextBoxFormField taxNumber;
	private AddressFormField address;
	private ListBoxFormField group;
	private TextBoxFormField NIB;
	private ListBoxFormField mediator;
	private ExpandableListBoxFormField clientManager;
	private ExpandableListBoxFormField profile;
	private TextBoxFormField email;
	private ExpandableListBoxFormField CAE;
	private TextBoxFormField activityObservations;
	private ExpandableListBoxFormField numberOfWorkers;
	private ExpandableListBoxFormField revenue;
	private DatePickerFormField birthDate;
	private ExpandableListBoxFormField gender;
	private ExpandableListBoxFormField maritalStatus;
	private ExpandableListBoxFormField profession;
	private TextAreaFormField notes;
	
	private FormViewSection specificSection;
	
	public ClientFormView() {
		super();
				
		name = new TextBoxFormField("Nome");
		taxNumber = new TextBoxFormField("Nº Contribuinte");
		address = new AddressFormField();
		group = new ListBoxFormField("Grupo");
		NIB = new TextBoxFormField("NIB");
		mediator = new ExpandableListBoxFormField("", "Mediador");
		clientManager = new ExpandableListBoxFormField("Gestor de Cliente");
		profile = new ExpandableListBoxFormField("", "Perfil Operacional");
		email = new TextBoxFormField("Email");
		CAE = new ExpandableListBoxFormField("", "CAE");
		activityObservations = new TextBoxFormField("Observações sobre actividade");
		numberOfWorkers = new ExpandableListBoxFormField("", "Número de trabalhadores");
		gender = new ExpandableListBoxFormField("", "Sexo");
		birthDate = new DatePickerFormField();
		maritalStatus = new ExpandableListBoxFormField("", "Estado Civil");
		profession = new ExpandableListBoxFormField("", "Profissão");
		revenue = new ExpandableListBoxFormField("", "Facturação");
		notes = new TextAreaFormField();
		
		addSection("Informação Geral");
	
		addFormField(name);
		addFormField(email);
		addFormField(taxNumber);
		addFormField(NIB);
		addFormField(group);
		addFormField(clientManager);
		addFormField(mediator);
		addFormField(profile);
		
		addSection("Tipo de Cliente");
		
		final RadioButton radioButtonI = new RadioButton("clientType", "Indivíduo");
		final RadioButton radioButtonE = new RadioButton("clientType", "Empresa");
		final RadioButton radioButtonC = new RadioButton("clientType", "Condomínio");
		
		radioButtonI.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(radioButtonE.getValue())
					setCompanyMode();
				else if(radioButtonI.getValue())
					setIndividualMode();
				else if(radioButtonC.getValue())
					setCondominiumMode();
			}
		});

		radioButtonE.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(radioButtonE.getValue())
					setCompanyMode();
				else if(radioButtonI.getValue())
					setIndividualMode();
				else if(radioButtonC.getValue())
					setCondominiumMode();
			}
		});
		
		radioButtonC.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(radioButtonE.getValue())
					setCompanyMode();
				else if(radioButtonI.getValue())
					setIndividualMode();
				else if(radioButtonC.getValue())
					setCondominiumMode();
			}
		});
		
		
		HorizontalPanel radioWrapper = new HorizontalPanel();
		radioWrapper.setSpacing(5);
		radioWrapper.add(radioButtonI);
		radioWrapper.add(radioButtonE);
		radioWrapper.add(radioButtonC);
		addWidget(radioWrapper);
		
		radioButtonI.setValue(true);
		setIndividualMode();
			
		specificSection = new FormViewSection("Informação Específica");
		addSection(specificSection);
		
		addSection("Morada");
		addFormField(address);
		
		addSection("Observações");
		notes.setFieldWidth("550px");
		notes.setFieldHeight("150px");
		addFormField(notes);		
	}
	
	public void setIndividualMode(){
		specificSection.setVisible(true);
		specificSection.clear();
		specificSection.addWidget(birthDate);
		specificSection.addFormField(gender);
		specificSection.addFormField(maritalStatus);
		specificSection.addFormField(profession);
	}
	
	public void setCompanyMode() {
		specificSection.setVisible(true);
		specificSection.clear();
		specificSection.addFormField(CAE);
		specificSection.addFormField(activityObservations);
		specificSection.addFormField(numberOfWorkers);
		specificSection.addFormField(revenue);
	}
	
	public void setCondominiumMode() {
		specificSection.clear();
		specificSection.setVisible(false);
	}
	
	@Override
	public ClientProxy getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(ClientProxy info) {
		// TODO Auto-generated method stub
		
	}

}
