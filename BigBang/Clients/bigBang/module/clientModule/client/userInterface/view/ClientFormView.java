package bigBang.module.clientModule.client.userInterface.view;

import java.util.List;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
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
import bigBang.module.clientModule.shared.ClientFormValidator;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

public class ClientFormView extends FormView<Client> implements ClientProcessDataBrokerClient {

	private TextBoxFormField name;
	private TextBoxFormField taxNumber;
	private AddressFormField address;
	private ExpandableListBoxFormField group;
	private TextBoxFormField NIB;
	private ExpandableListBoxFormField mediator;
	private TextBoxFormField clientManager;
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

	protected int clientDataVersion;

	public ClientFormView() {
		super();

		name = new TextBoxFormField("Nome", new ClientFormValidator.ClientNameValidator());
		name.setFieldWidth("600px");
		taxNumber = new TextBoxFormField("Nº Contribuinte", new ClientFormValidator.TaxNumberValidator());
		taxNumber.setFieldWidth("100px");
		address = new AddressFormField();
		group = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CLIENT_GROUP, "Grupo", new ClientFormValidator.ClientGroupValidator());
		group.allowEdition(false);
		NIB = new TextBoxFormField("NIB", new ClientFormValidator.NIBValidator());
		NIB.setFieldWidth("200px");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador", new ClientFormValidator.MediatorValidator());
		mediator.allowEdition(false);
		clientManager = new TextBoxFormField("Gestor de Cliente");
		clientManager.setFieldWidth("100px");
		clientManager.setEditable(false);
		profile = new ExpandableListBoxFormField(ModuleConstants.ListIDs.OperationalProfiles, "Perfil Operacional", new ClientFormValidator.ProfileValidator());
		profile.allowEdition(false);
		CAE = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CAEs, "CAE");
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

		addSection("Informação Geral");

		addFormField(name);

		addFormFieldGroup(new FormField<?>[]{
				clientManager,
				mediator,
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				profile,
				group
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				taxNumber,
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
					setIndividualMode();
				}else if(event.getValue().equalsIgnoreCase(ModuleConstants.ClientTypeIDs.Person)){
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
		addFormFieldGroup(new FormField<?>[]{
				birthDate,
				gender
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				maritalStatus,
				profession
		}, true);

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
				String managerId = ClientFormView.this.getValue().managerId;
				if(managerId != null && managerId.equalsIgnoreCase(item.id)){
					clientManager.setValue(item.value);
				}
			}

			@Override
			public void setTypifiedListItems(List<TipifiedListItem> items) {
				String managerId = ClientFormView.this.value == null ? null : ClientFormView.this.value.managerId;
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
				String managerId = ClientFormView.this.getValue().managerId;
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

		result.name = name.getValue();
		result.taxNumber = taxNumber.getValue();
		result.address = address.getValue();
		result.groupId = group.getValue();
		result.NIB = NIB.getValue();
		result.mediatorId = mediator.getValue();
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
		if(info.managerId == null){
			clientManager.clear();
		}else if(info.managerId != null){

			List<TipifiedListItem> items = BigBangTypifiedListBroker.Util.getInstance().getListItems(BigBangConstants.EntityIds.USER);
			for(TipifiedListItem i : items){
				if(i.id.equalsIgnoreCase(info.managerId)){
					clientManager.setValue(i.value);
					break;
				}
			}
		}
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
