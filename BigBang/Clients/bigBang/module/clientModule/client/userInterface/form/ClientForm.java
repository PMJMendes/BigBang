package bigBang.module.clientModule.client.userInterface.form;

import java.util.List;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.dataAccess.TypifiedListClient;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

public class ClientForm extends FormView<Client> implements ClientProcessDataBrokerClient {

	protected TextBoxFormField name;
	protected TextBoxFormField number;
	protected TextBoxFormField taxNumber;
	protected AddressFormField address;
	protected ExpandableListBoxFormField group;
	protected TextBoxFormField NIB;
	protected ExpandableListBoxFormField mediator;
	protected ExpandableListBoxFormField clientManager;
	protected ExpandableListBoxFormField profile;
	protected ExpandableListBoxFormField CAE;
	protected TextBoxFormField activityObservations;
	protected ExpandableListBoxFormField numberOfWorkers;
	protected ExpandableListBoxFormField revenue;
	protected DatePickerFormField birthDate;
	protected ExpandableListBoxFormField gender;
	protected ExpandableListBoxFormField maritalStatus;
	protected ExpandableListBoxFormField profession;
	protected TextAreaFormField notes;
	protected ExpandableListBoxFormField otherClientType;
	protected RadioButtonFormField clientType;

	protected FormViewSection individualSection, companySection, otherSection;

	protected int clientDataVersion;

	public ClientForm() {
		super();

		name = new TextBoxFormField("Nome");
		name.setMandatory(true);
		name.setFieldWidth("600px");
		number = new TextBoxFormField("Número");
		number.setEditable(false);
		number.setFieldWidth("175px");
		taxNumber = new TextBoxFormField("Nº Contribuinte");
		taxNumber.setFieldWidth("150px");
		address = new AddressFormField();
		group = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CLIENT_GROUP, "Grupo");
		group.allowEdition(false);
		NIB = new TextBoxFormField("NIB");
		NIB.setFieldWidth("200px");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		mediator.setMandatory(true);
		mediator.allowEdition(false);
		clientManager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Cliente");
		profile = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.OPERATIONAL_PROFILES, "Perfil Operacional");
		profile.setMandatory(true);
		profile.allowEdition(false);
		CAE = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CAEs, "CAE");
		CAE.setFieldWidth("400px");
		CAE.setPopupWidth("600px");
		CAE.setReadOnly(true);
		activityObservations = new TextBoxFormField("Observações sobre actividade");
		numberOfWorkers = new ExpandableListBoxFormField(ModuleConstants.ListIDs.CompanySizes, "Número de trabalhadores");
		gender = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.GENDERS, "Sexo");
		birthDate = new DatePickerFormField("Data de Nascimento");
		maritalStatus = new ExpandableListBoxFormField(ModuleConstants.ListIDs.MaritalStatuses, "Estado Civil");
		profession = new ExpandableListBoxFormField(ModuleConstants.ListIDs.Professions, "Profissão");
		revenue = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.SALES_VOLUMES, "Facturação");
		otherClientType = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ClientSubtypes, "Tipo");
		notes = new TextAreaFormField();
		clientType = new RadioButtonFormField();
		clientType.setMandatory(true);

		addSection("Informação Geral");

		addFormField(name);

		addFormFieldGroup(new FormField<?>[]{
				number,
				clientManager
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				mediator,
				profile
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				group,
				taxNumber
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				NIB
		}, true);

		addSection("Tipo de Cliente");

		clientType.addOption(ModuleConstants.ClientTypeIDs.Person, "Indivíduo");
		clientType.addOption(ModuleConstants.ClientTypeIDs.Company, "Empresa");
		clientType.addOption(ModuleConstants.ClientTypeIDs.Other, "Outro");

		clientType.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null){
					setNoneMode();
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.ClientTypeIDs.Person)){
					setIndividualMode();
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.ClientTypeIDs.Company)){
					setCompanyMode();
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.ClientTypeIDs.Other)){
					setOtherMode();
				}else{
					setNoneMode();
				}
			}
		});

		addFormField(clientType);

		individualSection = new FormViewSection("Informação Específica a Indivíduo");
		addSection(individualSection);
		addFormFieldGroup(new FormField<?>[]{
				birthDate,
				gender
		}, true);
		birthDate.setLabelWidth("205px");
		addFormFieldGroup(new FormField<?>[]{
				maritalStatus,
				profession
		}, true);
		maritalStatus.setLabelWidth("205px");

		companySection = new FormViewSection("Informação Específica a Empresas");
		addSection(companySection);
		addFormFieldGroup(new FormField<?>[]{
				numberOfWorkers,
				revenue
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				CAE,
				activityObservations
		}, true);

		otherSection = new FormViewSection("Informação Específica");
		addSection(otherSection);
		otherSection.addFormField(otherClientType);
		registerFormField(otherClientType);
		
		clientType.setValue(ModuleConstants.ClientTypeIDs.Person, true);

		addSection("Morada");
		addFormField(address);

		addSection("Observações");
		notes.setFieldWidth("550px");
		notes.setFieldHeight("150px");
		addFormField(notes);

		((ClientProcessBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT)).registerClient(this);

		final TypifiedListClient listClient = new TypifiedListClient() {
			protected int dataVersion;

			@Override
			public void updateItem(TipifiedListItem item) {
				String managerId = ClientForm.this.getValue().managerId;
				if(managerId != null && managerId.equalsIgnoreCase(item.id)){
					clientManager.setValue(item.value);
				}
			}

			@Override
			public void setTypifiedListItems(List<TipifiedListItem> items) {
				String managerId = ClientForm.this.value == null ? null : ClientForm.this.value.managerId;
				if(managerId == null)
					return;
				for(TipifiedListItem i : items) {
					if(i.id.equalsIgnoreCase(managerId)){
						clientManager.setValue(i.value);
						break;
					}
				}
			}

			@Override
			public void setTypifiedDataVersionNumber(int number) {
				dataVersion = number;
			}

			@Override
			public void removeItem(TipifiedListItem item) {
				String managerId = ClientForm.this.getValue().managerId;
				if(managerId != null && managerId.equalsIgnoreCase(item.id)){
					clientManager.clear();
				}
			}

			@Override
			public int getTypifiedDataVersionNumber() {
				return dataVersion;
			}

			@Override
			public void addItem(TipifiedListItem item) {}
		};

		addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				TypifiedListBroker listBroker = BigBangTypifiedListBroker.Util.getInstance();
				if(event.isAttached()){
					listBroker.registerClient(BigBangConstants.EntityIds.USER, listClient);
				}else{
					listBroker.unregisterClient(BigBangConstants.EntityIds.USER, listClient);
				}
			}
		});

		this.setValue(new Client());
		setForEdit();
		setValidator(new ClientFormValidator(this));
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
	
	public void setNoneMode() {
		companySection.setVisible(false);
		otherSection.setVisible(false);
		individualSection.setVisible(false);
	}

	@Override
	public Client getInfo() {
		Client result = getValue();

		if(value != null) {
			result.name = name.getValue();
			result.taxNumber = taxNumber.getValue();
			result.address = address.getValue();
			result.groupId = group.getValue();
			result.NIB = NIB.getValue();
			result.mediatorId = mediator.getValue();
			result.managerId = clientManager.getValue();
			result.operationalProfileId = profile.getValue();
			result.caeId = CAE.getValue();
			result.activityNotes = activityObservations.getValue();
			result.sizeId = numberOfWorkers.getValue();
			result.revenueId = revenue.getValue();
			result.birthDate = birthDate.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(birthDate.getValue());
			result.genderId = gender.getValue();
			result.maritalStatusId = maritalStatus.getValue();
			result.professionId = profession.getValue();
			result.notes = notes.getValue();
			result.typeId = clientType.getValue();
			result.subtypeId = otherClientType.getValue();
		}

		return result;
	}

	@Override
	public void setInfo(Client info) {
		if(info == null){
			clearInfo();
			return;			
		}
		name.setValue(info.name);
		number.setValue(info.clientNumber);
		taxNumber.setValue(info.taxNumber);
		
		taxNumber.setLabelText(info.isInternational ? "NIF (Internacional)" : "NIF");
		
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
		if(info.birthDate != null){
			birthDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.birthDate));
		}else{
			birthDate.clear();
		}
		gender.setValue(info.genderId);
		maritalStatus.setValue(info.maritalStatusId);
		profession.setValue(info.professionId);
		notes.setValue(info.notes);
		clientType.setValue(info.typeId, true);
		otherClientType.setValue(info.subtypeId);
	}

	public void setForCreate(){
		this.clientManager.setEditable(true);
		this.clientManager.setReadOnly(false);
		this.name.getNativeField().selectAll();
	}

	public void setForEdit(){
		this.clientManager.setEditable(false);
	}


	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT))
			this.clientDataVersion = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return this.clientDataVersion;
	}

	@Override
	public void addClient(Client client) {}

	@Override
	public void updateClient(Client client) {
		if(this.value == null || this.value.id == null)
			return;
		if(this.value.id.equals(client.id)){
			this.setValue(client, false);
		}
	}

	@Override
	public void removeClient(String clientId) {
		this.setValue(null);
	}

}
