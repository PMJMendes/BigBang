package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class ClientFormView extends FormView<Client> implements ClientProcessDataBrokerClient {

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
	private ExpandableListBoxFormField otherClientType;

	private FormViewSection specificSection;

	public ClientFormView() {
		super();

		name = new TextBoxFormField("Nome");
		taxNumber = new TextBoxFormField("Nº Contribuinte");
		address = new AddressFormField();
		group = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CLIENT_GROUP, "Grupo");
		NIB = new TextBoxFormField("NIB");
		mediator = new ExpandableListBoxFormField("Mediador");
		clientManager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Cliente");
		profile = new ExpandableListBoxFormField(ModuleConstants.ListIDs.OperationalProfiles, "Perfil Operacional");
		email = new TextBoxFormField("Email");
		CAE = new ExpandableListBoxFormField(ModuleConstants.ListIDs.CAEs, "CAE");
		CAE.setPopupWidth("600px");
		CAE.setReadOnly(true);
		activityObservations = new TextBoxFormField("Observações sobre actividade");
		numberOfWorkers = new ExpandableListBoxFormField(ModuleConstants.ListIDs.CompanySizes, "Número de trabalhadores");
		gender = new ExpandableListBoxFormField(ModuleConstants.ListIDs.Sexes, "Sexo");
		birthDate = new DatePickerFormField();
		maritalStatus = new ExpandableListBoxFormField(ModuleConstants.ListIDs.MaritalStatuses, "Estado Civil");
		profession = new ExpandableListBoxFormField(ModuleConstants.ListIDs.Professions, "Profissão");
		revenue = new ExpandableListBoxFormField(ModuleConstants.ListIDs.SalesVolumes, "Facturação");
		otherClientType = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ClientSubtypes, "Tipo");
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
		final RadioButton radioButtonC = new RadioButton("clientType", "Outro");

		radioButtonI.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(radioButtonE.getValue())
					setCompanyMode();
				else if(radioButtonI.getValue())
					setIndividualMode();
				else if(radioButtonC.getValue())
					setOtherMode();
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
					setOtherMode();
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
					setOtherMode();
			}
		});

		HorizontalPanel radioWrapper = new HorizontalPanel();
		radioWrapper.setSpacing(5);
		radioWrapper.add(radioButtonI);
		radioWrapper.add(radioButtonE);
		radioWrapper.add(radioButtonC);
		addWidget(radioWrapper);

		specificSection = new FormViewSection("Informação Específica");
		addSection(specificSection);

		radioButtonI.setValue(true);
		setIndividualMode();

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

	public void setOtherMode() {
		specificSection.setVisible(true);
		specificSection.clear();
		specificSection.addFormField(otherClientType);
	}

	@Override
	public Client getInfo() {
		Client result = getValue();
		
		name.getValue();
		taxNumber.getValue();
		address.getValue();
		group.getValue();
		NIB.getValue();
		mediator.getValue();
		clientManager.getValue();
		profile.getValue();
		email.getValue();
		CAE.getValue();
		activityObservations.getValue();
		numberOfWorkers.getValue();
		revenue.getValue();
		birthDate.getValue();
		gender.getValue();
		maritalStatus.getValue();
		profession.getValue();
		notes.getValue();
		//TODO CLIENT TYPE FJVC
		otherClientType.getValue();
		
		return result;
	}

	@Override
	public void setInfo(Client info) {
		name.setValue(info.name);
		taxNumber.setValue(info.taxNumber);
		address.setValue(info.address);
		group.setValue(info.groupId);
		NIB.setValue(info.NIB);
		mediator.setValue(info.mediatorId);
		clientManager.setValue(info.managerId);
		profile.setValue(info.operationalProfileId);
		email.setValue("TODO"); //TODO FJVC
		CAE.setValue(info.caeId);
		activityObservations.setValue(info.activityNotes);
		numberOfWorkers.setValue(info.sizeId);
		revenue.setValue(info.revenueId);
		//if(birthDate != null)
		//	birthDate.setValue(new Date(info.birthDate)); //TODO FJVC
		gender.setValue(info.genderId);
		maritalStatus.setValue(info.maritalStatusId);
		profession.setValue(info.professionId);
		notes.setValue(info.notes);
		//TODO CLIENT TYPE FJVC
		otherClientType.setValue(info.subtypeId);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDataVersion(String dataElementId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addClient(Client client) {}

	@Override
	public void updateClient(Client client) {
		if(this.value.id.equals(client.id)){
			this.setValue(client, false);
		}
	}

	@Override
	public void removeClient(String clientId) {}

}
