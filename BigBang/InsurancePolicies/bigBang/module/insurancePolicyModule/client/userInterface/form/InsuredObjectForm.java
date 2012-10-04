package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.insurancePolicyModule.client.userInterface.HeaderFieldsSection;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

public class InsuredObjectForm extends FormView<InsuredObject> {

	//common fields
	protected TextBoxFormField identification;
	protected AddressFormField address;
	protected DatePickerFormField inclusionDate;
	protected DatePickerFormField exclusionDate;
	protected Button deleteObject;

	//person fields
	protected TextBoxFormField personTaxNumber;
	protected ExpandableListBoxFormField personGender;
	protected DatePickerFormField personBirthDate;
	protected TextBoxFormField personClientNumber;
	protected TextBoxFormField personCompanyNumber; 

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

	protected FormViewSection commonSection, personSection, companySection, equipmentSection, locationSection, animalSection;

	protected HeaderFieldsSection headerFields;

	public InsuredObjectForm() {
		this.scrollWrapper.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		//common fields
		this.commonSection = new FormViewSection("Cabeçalho de Unidade de Risco");
		this.identification = new TextBoxFormField("Identificação");
		this.identification.setFieldWidth("300px");
		this.inclusionDate = new DatePickerFormField("Data de Inclusão");
		this.exclusionDate = new DatePickerFormField("Data de Exclusão");
		this.deleteObject = new Button("Eliminar");

		this.commonSection.addFormField(this.identification, true);
		this.commonSection.addFormField(this.inclusionDate, true);
		this.commonSection.addFormField(this.exclusionDate, true);
		this.commonSection.addWidget(deleteObject);
		deleteObject.getElement().getStyle().setFloat(Float.RIGHT);
		deleteObject.getElement().getStyle().setMarginRight(15,Unit.PX);
		deleteObject.getElement().getStyle().setMarginTop(15, Unit.PX);
		this.addSection(this.commonSection);

		FormViewSection addressSection = new FormViewSection("Morada de Risco");
		this.address = new AddressFormField("Morada");
		addressSection.addFormField(this.address);
		addSection(addressSection);

		//person fields
		this.personSection = new FormViewSection("Pessoa");
		this.personTaxNumber = new TextBoxFormField("NIF");
		this.personTaxNumber.setFieldWidth("175px");
		this.personGender = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.GENDERS, "Sexo");
		this.personBirthDate = new DatePickerFormField("Data de Nascimento");
		this.personClientNumber = new TextBoxFormField("Número de Cliente Interno");
		this.personCompanyNumber = new TextBoxFormField("Número na Seguradora");

		this.personSection.addFormFieldGroup(new FormField[]{
				this.personTaxNumber,
				this.personGender,
				this.personBirthDate
		}, true);
		this.personSection.addFormFieldGroup(new FormField[]{
				this.personClientNumber,
				this.personCompanyNumber
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
		this.equipmentFirstRegistryDate = new DatePickerFormField("Data da Primeira Matrícula");
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

		this.headerFields = new HeaderFieldsSection();

		addSection(headerFields);

		this.setValue(new InsuredObject());
	}

	protected void showSectionForTypeWithId(String typeId) {
		showTypeSpecificDataSection(false, null);
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

	protected void showTypeSpecificDataSection(boolean show, String id){
		if(show){
			showSectionForTypeWithId(id);
		}else{
			this.personSection.setVisible(false);
			this.companySection.setVisible(false);
			this.equipmentSection.setVisible(false);
			this.locationSection.setVisible(false);
			this.animalSection.setVisible(false);
		}
	}

	@Override
	public InsuredObject getInfo() {
		InsuredObject result = this.value;

		//common fields
		result.unitIdentification = identification.getValue();
		result.address = address.getValue();
		result.inclusionDate = inclusionDate.getStringValue();
		result.exclusionDate = exclusionDate.getStringValue();

		//person fields
		result.taxNumberPerson = personTaxNumber.getValue();
		result.genderId = personGender.getValue();
		result.birthDate = personBirthDate.getStringValue();
		result.clientNumberPerson = personClientNumber.getValue();
		result.insuranceCompanyInternalIdPerson = personCompanyNumber.getValue();

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

		result.headerFields = headerFields.getValue();
		return result;
	}

	@Override
	public void setInfo(InsuredObject info) {

		if(info == null){
			this.setValue(new InsuredObject());
			return;
		}

		//common fields
		identification.setValue(info.unitIdentification);
		address.setValue(info.address);
		inclusionDate.setValue(info.inclusionDate);
		exclusionDate.setValue(info.exclusionDate);

		showSectionForTypeWithId(info.typeId);

		//person fields
		personTaxNumber.setValue(info.taxNumberPerson);
		personGender.setValue(info.genderId);
		personBirthDate.setValue(info.birthDate);
		personClientNumber.setValue(info.clientNumberPerson);
		personCompanyNumber.setValue(info.insuranceCompanyInternalIdPerson);

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

		headerFields.setHeaderText(info.categoryName + " / " + info.lineName + " / " + info.subLineName);
		headerFields.setValue(info.headerFields);

	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(deleteObject != null){
			deleteObject.setVisible(!readOnly);
		}
		if(headerFields != null){
			headerFields.setReadOnly(readOnly);
		}
	}

	public HasClickHandlers getDeleteButton(){
		return deleteObject;
	}
}
