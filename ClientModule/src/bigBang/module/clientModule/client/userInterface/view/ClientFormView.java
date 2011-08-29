package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

public class ClientFormView extends FormView<Client> implements ClientProcessDataBrokerClient {

	private TextBoxFormField name;
	private TextBoxFormField taxNumber;
	private AddressFormField address;
	private ListBoxFormField group;
	private TextBoxFormField NIB;
	private ListBoxFormField mediator;
	private ExpandableListBoxFormField clientManager;
	private ExpandableListBoxFormField profile;
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
	private RadioButtonFormField clientType;

	private FormViewSection individualSection, companySection, otherSection;

	public ClientFormView() {
		super();

		name = new TextBoxFormField("Nome");
		taxNumber = new TextBoxFormField("Nº Contribuinte");
		address = new AddressFormField();
		group = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CLIENT_GROUP, "Grupo");
		NIB = new TextBoxFormField("NIB");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		clientManager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Cliente");
		profile = new ExpandableListBoxFormField(ModuleConstants.ListIDs.OperationalProfiles, "Perfil Operacional");
		CAE = new ExpandableListBoxFormField(ModuleConstants.ListIDs.CAEs, "CAE");
		CAE.setPopupWidth("600px");
		CAE.setReadOnly(true);
		activityObservations = new TextBoxFormField("Observações sobre actividade");
		numberOfWorkers = new ExpandableListBoxFormField(ModuleConstants.ListIDs.CompanySizes, "Número de trabalhadores");
		gender = new ExpandableListBoxFormField(ModuleConstants.ListIDs.Sexes, "Sexo");
		birthDate = new DatePickerFormField("Data de Nascimento");
		maritalStatus = new ExpandableListBoxFormField(ModuleConstants.ListIDs.MaritalStatuses, "Estado Civil");
		profession = new ExpandableListBoxFormField(ModuleConstants.ListIDs.Professions, "Profissão");
		revenue = new ExpandableListBoxFormField(ModuleConstants.ListIDs.SalesVolumes, "Facturação");
		otherClientType = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ClientSubtypes, "Tipo");
		notes = new TextAreaFormField();
		clientType = new RadioButtonFormField();

		addSection("Informação Geral");

		addFormField(name);
		addFormField(taxNumber);
		addFormField(NIB);
		addFormField(group);
		addFormField(clientManager);
		addFormField(mediator);
		addFormField(profile);

		addSection("Tipo de Cliente");

		clientType.addOption(ModuleConstants.ClientTypeIDs.Person, "Indivíduo");
		clientType.addOption(ModuleConstants.ClientTypeIDs.Company, "Empresa");
		clientType.addOption(ModuleConstants.ClientTypeIDs.Other, "Outro");

		clientType.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue().equalsIgnoreCase(ModuleConstants.ClientTypeIDs.Person)){
					setIndividualMode();
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.ClientTypeIDs.Company)){
					setCompanyMode();
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.ClientTypeIDs.Other)){
					setOtherMode();
				}
			}
		});
		
		addFormField(clientType);

		individualSection = new FormViewSection("Informação Específica a Indivíduo");
		addSection(individualSection);
		individualSection.addFormField(birthDate);
		individualSection.addFormField(gender);
		individualSection.addFormField(maritalStatus);
		individualSection.addFormField(profession);
		
		companySection = new FormViewSection("Informação Específica a Companhias");
		addSection(companySection);
		companySection.addFormField(CAE);
		companySection.addFormField(activityObservations);
		companySection.addFormField(numberOfWorkers);
		companySection.addFormField(revenue);
		
		otherSection = new FormViewSection("Informação Específica");
		addSection(otherSection);
		otherSection.addFormField(otherClientType);

		clientType.setValue(ModuleConstants.ClientTypeIDs.Person, true);

		addSection("Morada");
		addFormField(address);

		addSection("Observações");
		notes.setFieldWidth("550px");
		notes.setFieldHeight("150px");
		addFormField(notes);
	}

	public void setIndividualMode(){
		companySection.setVisible(false);
		otherSection.setVisible(false);
		individualSection.setVisible(true);
	}

	public void setCompanyMode() {
		companySection.setVisible(true);
		otherSection.setVisible(false);
		individualSection.setVisible(false);
	}
	
	public void setOtherMode() {
		companySection.setVisible(false);
		otherSection.setVisible(true);
		individualSection.setVisible(false);
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
		CAE.getValue();
		activityObservations.getValue();
		numberOfWorkers.getValue();
		revenue.getValue();
		birthDate.getValue();
		gender.getValue();
		maritalStatus.getValue();
		profession.getValue();
		notes.getValue();
		clientType.getValue();
		otherClientType.getValue();
		
		return result;
	}

	@Override
	public void setInfo(Client info) {
		if(info == null){
			clearInfo();
			return;			
		}
		name.setValue(info.name);
		taxNumber.setValue(info.taxNumber);
		address.setValue(info.address);
		group.setValue(info.groupId);
		NIB.setValue(info.NIB);
		mediator.setValue(info.mediatorId);
		clientManager.setValue(info.managerId);
		profile.setValue(info.operationalProfileId);
		CAE.setValue(info.caeId);
		activityObservations.setValue(info.activityNotes);
		numberOfWorkers.setValue(info.sizeId);
		revenue.setValue(info.revenueId);
		if(info.birthDate != null)
			birthDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.birthDate));
		gender.setValue(info.genderId);
		maritalStatus.setValue(info.maritalStatusId);
		profession.setValue(info.professionId);
		notes.setValue(info.notes);
		clientType.setValue(info.typeId, true);
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
