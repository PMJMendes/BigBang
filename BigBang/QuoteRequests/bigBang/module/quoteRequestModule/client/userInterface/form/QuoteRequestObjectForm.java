package bigBang.module.quoteRequestModule.client.userInterface.form;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObject.SubLineData;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestObjectSubLineDataSection;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class QuoteRequestObjectForm extends FormView<QuoteRequestObject> {

	//common fields
	protected TextBoxFormField identification;
	protected AddressFormField address;
	protected DatePickerFormField inclusionDate;
	protected DatePickerFormField exclusionDate;
	protected RadioButtonFormField type;

	//person fields
	protected TextBoxFormField personTaxNumber;
	protected ExpandableListBoxFormField personGender;
	protected DatePickerFormField personBirthDate;
	protected TextBoxFormField personClientNumber;
	protected ExpandableListBoxFormField personInsurangeAgency; 

	//company fields
	protected TextBoxFormField companyTaxNumber;
	protected ExpandableListBoxFormField companyCAE;
	protected ExpandableListBoxFormField companyGrievousCAE;
	protected TextAreaFormField companyActivityNotes;
	protected TextAreaFormField companyProductNotes;
	protected ExpandableListBoxFormField companyBusinessVolume;
	protected TextBoxFormField companyEuropeanUnionEntity;
	protected TextBoxFormField companyClientNumber;

	//equipment fields
	protected TextBoxFormField equipmentMakeAndModel;
	protected TextAreaFormField equipmentDescription;
	protected DatePickerFormField equipmentFirstRegistryDate;
	protected TextBoxFormField equipmentManufactureYear;
	protected TextBoxFormField equipmentClientInternalId;
	protected TextBoxFormField equipmentInsuranceCompanyInternalVehicleId;

	//location fields
	protected TextAreaFormField locationDescription;

	//animal fields
	protected TextBoxFormField animalSpecies;
	protected TextBoxFormField animalRace;
	protected TextBoxFormField animalBirthYear;
	protected TextBoxFormField animalCityRegistryNumber;
	protected TextBoxFormField animalElectronicTagId;

	protected FormViewSection commonSection, typeSection, personSection, companySection, equipmentSection, locationSection, animalSection, dynamicHeaderSection;
	protected Map<String, QuoteRequestObjectSubLineDataSection> subLineSections;

	public QuoteRequestObjectForm(){
		this.scrollWrapper.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		this.subLineSections = new HashMap<String, QuoteRequestObjectSubLineDataSection>();

		//common fields
		this.commonSection = new FormViewSection("Informação Geral");
		this.identification = new TextBoxFormField("Identificação");
		this.identification.setFieldWidth("300px");
		this.inclusionDate = new DatePickerFormField("Data de Inclusão");
		this.exclusionDate = new DatePickerFormField("Data de Exclusão");

		this.commonSection.addFormField(this.identification, true);
		this.commonSection.addFormField(this.inclusionDate, true);
		this.commonSection.addFormField(this.exclusionDate, true);
		this.addSection(this.commonSection);

		FormViewSection addressSection = new FormViewSection("Morada de Risco");
		this.address = new AddressFormField("Morada");
		addressSection.addFormField(this.address);
		addSection(addressSection);

		//type
		this.typeSection = new FormViewSection("Tipo de Objecto");
		this.type = new RadioButtonFormField();

		this.typeSection.addFormField(this.type);
		this.addSection(this.typeSection);

		this.type.addOption(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PERSON, "Pessoa");
		this.type.addOption(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_COMPANY, "Empresa ou Grupo");
		this.type.addOption(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_EQUIPMENT, "Objecto ou Equipamento");
		this.type.addOption(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PLACE, "Local");
		this.type.addOption(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_ANIMAL, "Animal");
		this.type.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				showSectionForTypeWithId(event.getValue());
			}
		});

		//person fields
		this.personSection = new FormViewSection("Pessoa");
		this.personTaxNumber = new TextBoxFormField("NIF");
		this.personTaxNumber.setFieldWidth("175px");
		this.personGender = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.GENDERS, "Sexo");
		this.personBirthDate = new DatePickerFormField("Data de Nascimento");
		this.personClientNumber = new TextBoxFormField("Número de Cliente Interno");
		this.personInsurangeAgency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");

		this.personSection.addFormFieldGroup(new FormField[]{
				this.personTaxNumber,
				this.personGender,
				this.personBirthDate
		}, true);
		this.personSection.addFormFieldGroup(new FormField[]{
				this.personClientNumber,
				this.personInsurangeAgency
		}, true);
		this.addSection(this.personSection);

		//company fields
		this.companySection = new FormViewSection("Empresa ou Grupo");
		this.companyTaxNumber = new TextBoxFormField("NIPC");
		this.companyTaxNumber.setFieldWidth("200px");
		this.companyCAE = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CAEs, "CAE Principal");
		this.companyGrievousCAE = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CAEs, "CAE Mais Gravoso");
		this.companyActivityNotes = new TextAreaFormField("Notas da Actividade");
		this.companyProductNotes = new TextAreaFormField("Notas do Produto");
		this.companyBusinessVolume = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.SALES_VOLUMES, "Volume de Facturação");
		this.companyEuropeanUnionEntity = new TextBoxFormField("Entidade da União Europeia");
		this.companyClientNumber = new TextBoxFormField("Número de Cliente Interno");

		this.companySection.addFormFieldGroup(new FormField[]{
				this.companyTaxNumber,
				this.companyCAE,
				this.companyGrievousCAE
		}, true);
		this.companySection.addFormFieldGroup(new FormField[]{
				this.companyActivityNotes,
				this.companyProductNotes,
				this.companyBusinessVolume
		}, true);
		this.companySection.addFormFieldGroup(new FormField[]{
				this.companyEuropeanUnionEntity,
				this.companyClientNumber,
		}, true);
		this.addSection(this.companySection);

		//equipment fields
		this.equipmentSection = new FormViewSection("Objecto ou Equipamento");
		this.equipmentMakeAndModel = new TextBoxFormField("Marca e Modelo");
		this.equipmentDescription = new TextAreaFormField("Descrição");
		this.equipmentFirstRegistryDate = new DatePickerFormField("Data do Primeiro Registo");
		this.equipmentManufactureYear = new TextBoxFormField("Ano de Fabrico");
		this.equipmentManufactureYear.setFieldWidth("100px");
		this.equipmentClientInternalId = new TextBoxFormField("Identificação no Cliente");
		this.equipmentInsuranceCompanyInternalVehicleId = new TextBoxFormField("Identificação na Seguradora");

		this.equipmentSection.addFormFieldGroup(new FormField[]{
				this.equipmentMakeAndModel,
				this.equipmentDescription
		}, true);
		this.equipmentSection.addFormFieldGroup(new FormField[]{
				this.equipmentFirstRegistryDate,
				this.equipmentManufactureYear
		}, true);
		this.equipmentSection.addFormFieldGroup(new FormField[]{
				this.equipmentClientInternalId,
				this.equipmentInsuranceCompanyInternalVehicleId
		}, true);
		this.addSection(this.equipmentSection);

		//location fields
		this.locationSection = new FormViewSection("Local");
		this.locationDescription = new TextAreaFormField("Descrição");

		this.locationSection.addFormField(this.locationDescription);
		this.addSection(this.locationSection);

		//animal fields
		this.animalSection = new FormViewSection("Animal");
		this.animalSpecies = new TextBoxFormField("Espécie");
		this.animalRace = new TextBoxFormField("Raça");
		this.animalBirthYear = new TextBoxFormField("Ano de Nascimento");
		this.animalBirthYear.setFieldWidth("100px");
		this.animalCityRegistryNumber = new TextBoxFormField("Número de Registo");
		this.animalElectronicTagId = new TextBoxFormField("Identificação Electrónica");

		this.animalSection.addFormFieldGroup(new FormField[]{
				this.animalSpecies,
				this.animalRace,
				this.animalBirthYear
		}, true);
		this.animalSection.addFormFieldGroup(new FormField[]{
				this.animalCityRegistryNumber,
				this.animalElectronicTagId
		}, true);
		this.addSection(this.animalSection);

		this.dynamicHeaderSection = new FormViewSection("");
		this.dynamicHeaderSection.showHeader(false);
		this.addSection(dynamicHeaderSection);

		this.setValue(new QuoteRequestObject());
	}

	protected void showSectionForTypeWithId(String typeId) {
		showTypeSpecificDataSection(false);
		if(typeId == null){
			return;
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PERSON)){
			this.personSection.setVisible(true);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_COMPANY)){
			this.companySection.setVisible(true);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_EQUIPMENT)){
			this.equipmentSection.setVisible(true);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PLACE)){
			this.locationSection.setVisible(true);
		}else if(typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_ANIMAL)){
			this.animalSection.setVisible(true);
		}
	}

	public void showTypeSection(boolean show){
		this.typeSection.setVisible(show);
	}

	protected void showTypeSpecificDataSection(boolean show){
		if(show){
			showSectionForTypeWithId(this.type.getValue());
		}else{
			this.personSection.setVisible(false);
			this.companySection.setVisible(false);
			this.equipmentSection.setVisible(false);
			this.locationSection.setVisible(false);
			this.animalSection.setVisible(false);
		}
	}

	@Override
	public QuoteRequestObject getInfo() {
		QuoteRequestObject result = this.value;

		if(result != null) {
			//common fields
			result.unitIdentification = identification.getValue();
			result.address = address.getValue();
			result.typeId = type.getValue();

			//person fields
			result.taxNumberPerson = personTaxNumber.getValue();
			result.genderId = personGender.getValue();
			result.birthDate = personBirthDate.getStringValue();
			result.clientNumberPerson = personClientNumber.getValue();
			result.insuranceCompanyInternalIdPerson = personInsurangeAgency.getValue();

			//company fields
			result.taxNumberCompany = companyTaxNumber.getValue();
			result.caeId = companyCAE.getValue();
			result.grievousCaeId = companyGrievousCAE.getValue();
			result.activityNotes = companyActivityNotes.getValue();
			result.productNotes = companyProductNotes.getValue();
			result.businessVolumeId = companyBusinessVolume.getValue();
			result.europeanUnionEntity = companyEuropeanUnionEntity.getValue();
			result.clientNumberGroup = companyClientNumber.getValue();

			//equipment fields
			result.makeAndModel = equipmentMakeAndModel.getValue();
			result.equipmentDescription = equipmentDescription.getValue();
			result.firstRegistryDate = equipmentFirstRegistryDate.getStringValue();
			result.manufactureYear = equipmentManufactureYear.getValue();
			result.clientInternalId = equipmentClientInternalId.getValue();
			result.insuranceCompanyInternalIdVehicle = equipmentInsuranceCompanyInternalVehicleId.getValue();

			//location fields
			result.siteDescription = locationDescription.getValue();

			//animal fields
			result.species = animalSpecies.getValue();
			result.race = animalRace.getValue();
			result.birthYear = animalBirthYear.getValue();
			result.cityRegistryNumber = animalCityRegistryNumber.getValue();
			result.electronicIdTag = animalElectronicTagId.getValue();

			result.objectData = getSubLinesData();
		}

		return result;
	}

	@Override
	public void setInfo(QuoteRequestObject info) {
		if(info == null){
			this.setValue(new QuoteRequestObject());
			return;
		}

		//common fields
		identification.setValue(info.unitIdentification);
		address.setValue(info.address);
		type.setValue(info.typeId, true);

		//person fields
		personTaxNumber.setValue(info.taxNumberPerson);
		personGender.setValue(info.genderId);
		personBirthDate.setValue(info.birthDate);
		personClientNumber.setValue(info.clientNumberPerson);
		personInsurangeAgency.setValue(info.insuranceCompanyInternalIdPerson);

		//company fields
		companyTaxNumber.setValue(info.taxNumberCompany);
		companyCAE.setValue(info.caeId);
		companyGrievousCAE.setValue(info.grievousCaeId);
		companyActivityNotes.setValue(info.activityNotes);
		companyProductNotes.setValue(info.productNotes);
		companyBusinessVolume.setValue(info.businessVolumeId);
		companyEuropeanUnionEntity.setValue(info.europeanUnionEntity);
		companyClientNumber.setValue(info.clientNumberGroup);

		//equipment fields
		equipmentMakeAndModel.setValue(info.makeAndModel);
		equipmentDescription.setValue(info.equipmentDescription);
		equipmentFirstRegistryDate.setValue(info.firstRegistryDate);
		equipmentManufactureYear.setValue(info.manufactureYear);
		equipmentClientInternalId.setValue(info.clientInternalId);
		equipmentInsuranceCompanyInternalVehicleId.setValue(info.insuranceCompanyInternalIdVehicle);

		//location fields
		locationDescription.setValue(info.siteDescription);

		//animal fields
		animalSpecies.setValue(info.species);
		animalRace.setValue(info.race);
		animalBirthYear.setValue(info.birthYear);
		animalCityRegistryNumber.setValue(info.cityRegistryNumber);
		animalElectronicTagId.setValue(info.electronicIdTag);

		setSubLineData(info.objectData);
	}

	protected void setSubLineData(SubLineData[] data){
		clearSubLineData();

		if(data != null){
			for(SubLineData subLineData : data) {
				addSubLineDataSection(subLineData, true);
			}
		}
	}


	protected SubLineData[] getSubLinesData(){
		SubLineData[] result = new SubLineData[this.subLineSections.size()];

		int i = 0;
		for(QuoteRequestObjectSubLineDataSection section : subLineSections.values()) {
			result[i] = section.getSubLineData();
			i++;
		}

		return result;
	}

	protected void clearSubLineData(){
		for(String key : subLineSections.keySet()) {
			QuoteRequestObjectSubLineDataSection section = subLineSections.get(key);
			this.removeSection(section);
		}
		this.subLineSections.clear();
	}

	protected void addSubLineDataSection(SubLineData data, boolean collapsed) {
		QuoteRequestObjectSubLineDataSection section = new QuoteRequestObjectSubLineDataSection(this.value.id, data, collapsed);
		section.setReadOnly(this.isReadOnly());
		this.subLineSections.put(data.subLineId, section);
		addSection(section);
	}

}
