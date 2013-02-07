package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

import com.google.gwt.user.client.ui.Button;

public class CompositeObjectForm extends FormView<QuoteRequestObject>{

	//COMMON
	private TextBoxFormField identification;
	private AddressFormField address;	
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
	
	public CompositeObjectForm() {

		//COMMON
		deleteObject = new Button("Apagar Unidade de Risco");
		identification = new TextBoxFormField("Identificação");
		address = new AddressFormField("Morada");
		
		commonSection = new FormViewSection("Cabeçalho de Unidade de Risco");		
		commonSection.addWidget(deleteObject);
		commonSection.addFormField(identification);
		commonSection.addFormField(address);
		addSection(commonSection);

		//PERSON
		personSection = new FormViewSection("Pessoa");
		personTaxNumber = new TextBoxFormField("NIF");
		personGender = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.GENDERS, "Sexo");
		personBirthDate = new DatePickerFormField("Data de Nascimento");
		personClientNumber = new TextBoxFormField("Número de Cliente Interno");
		personCompanyNumber = new TextBoxFormField("Número na Seguradora");

		personSection.addFormFieldGroup(new FormField[]{
				personTaxNumber,
				personGender,
				personBirthDate
		}, true);
		personSection.addFormFieldGroup(new FormField[]{
				personClientNumber,
				personCompanyNumber
		}, true);
		addSection(personSection);

		//COMPANY
		companySection = new FormViewSection("Empresa ou Grupo");
		companyTaxNumber = new TextBoxFormField("NIPC");
		companyTaxNumber.setFieldWidth("200px");
		companyCAE = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CAEs, "CAE Principal");
		companyGrievousCAE = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CAEs, "CAE Mais Gravoso");
		companyActivityNotes = new TextAreaFormField("Notas da Actividade");
		companyProductNotes = new TextAreaFormField("Notas do Produto");
		companyBusinessVolume = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.SALES_VOLUMES, "Volume de Facturação");
		companyEuropeanUnionEntity = new TextBoxFormField("Entidade da União Europeia");
		companyClientNumber = new TextBoxFormField("Número de Cliente Interno");

		companySection.addFormFieldGroup(new FormField[]{
				companyTaxNumber,
				companyCAE,
				companyGrievousCAE
		}, true);
		companySection.addFormFieldGroup(new FormField[]{
				companyActivityNotes,
				companyProductNotes,
				companyBusinessVolume
		}, true);
		companySection.addFormFieldGroup(new FormField[]{
				companyEuropeanUnionEntity,
				companyClientNumber,
		}, true);
		addSection(companySection);

		//EQUIPMENT
		equipmentSection = new FormViewSection("Objecto ou Equipamento");
		equipmentMakeAndModel = new TextBoxFormField("Marca e Modelo");
		equipmentDescription = new TextAreaFormField("Descrição");
		equipmentFirstRegistryDate = new DatePickerFormField("Data da Primeira Matrícula");
		equipmentManufactureYear = new TextBoxFormField("Ano de Fabrico");
		equipmentManufactureYear.setFieldWidth("100px");
		equipmentClientInternalId = new TextBoxFormField("Identificação no Cliente");
		equipmentInsuranceCompanyInternalVehicleId = new TextBoxFormField("Identificação na Seguradora");

		equipmentSection.addFormFieldGroup(new FormField[]{
				equipmentMakeAndModel,
				equipmentDescription
		}, true);
		equipmentSection.addFormFieldGroup(new FormField[]{
				equipmentFirstRegistryDate,
				equipmentManufactureYear
		}, true);
		equipmentSection.addFormFieldGroup(new FormField[]{
				equipmentClientInternalId,
				equipmentInsuranceCompanyInternalVehicleId
		}, true);
		addSection(equipmentSection);

		//PLACE
		locationSection = new FormViewSection("Local");
		locationDescription = new TextAreaFormField("Descrição");

		locationSection.addFormField(this.locationDescription);
		addSection(locationSection);

		//ANIMAL
		animalSection = new FormViewSection("Animal");
		animalSpecies = new TextBoxFormField("Espécie");
		animalRace = new TextBoxFormField("Raça");
		animalBirthYear = new TextBoxFormField("Ano de Nascimento");
		animalBirthYear.setFieldWidth("100px");
		animalCityRegistryNumber = new TextBoxFormField("Número de Registo");
		animalElectronicTagId = new TextBoxFormField("Identificação Electrónica");

		animalSection.addFormFieldGroup(new FormField[]{
				animalSpecies,
				animalRace,
				animalBirthYear
		}, true);
		animalSection.addFormFieldGroup(new FormField[]{
				animalCityRegistryNumber,
				animalElectronicTagId
		}, true);
		addSection(animalSection);

		setValidator(new CompositeObjectFormValidator(this));
	}

	@Override
	public QuoteRequestObject getInfo() {
		QuoteRequestObject result = value;

		//common fields
		result.unitIdentification = identification.getValue();
		result.address = address.getValue();

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

	}

	private void showSectionForTypeWithId(String typeId) {
		
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

}
