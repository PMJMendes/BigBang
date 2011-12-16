package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuredObject;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class InsuredObjectForm extends FormView<InsuredObject> {

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
	protected TextBoxFormField animalAge;
	protected TextBoxFormField animalCityRegistryNumber;
	protected TextBoxFormField animalElectronicTagId;
	
	protected FormViewSection commonSection, typeSection, personSection, companySection, equipmentSection, locationSection, animalSection;
	
	public InsuredObjectForm(){
		//common fields
		this.commonSection = new FormViewSection("Informação Geral");
		this.identification = new TextBoxFormField("Identificação");
		this.address = new AddressFormField("Morada");
		this.inclusionDate = new DatePickerFormField("Data de Inclusão");
		this.exclusionDate = new DatePickerFormField("Data de Exclusão");

		this.commonSection.addFormField(this.identification);
		this.commonSection.addFormField(this.address);
		this.commonSection.addFormField(this.inclusionDate);
		this.commonSection.addFormField(this.exclusionDate);
		this.addSection(this.commonSection);
		
		//type
		this.typeSection = new FormViewSection("Tipo de Objecto");
		this.type = new RadioButtonFormField();
	
		this.typeSection.addFormField(this.type);
		this.addSection(this.typeSection);
		
		this.type.addOption(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PERSON, "Pessoa");
		this.type.addOption(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_COMPANY, "Companhia");
		this.type.addOption(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_EQUIPMENT, "Equipamento");
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
		this.personGender = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.GENDERS, "Sexo");
		this.personBirthDate = new DatePickerFormField("Data de Nascimento");
		this.personClientNumber = new TextBoxFormField("Número de Cliente Interno");
		this.personInsurangeAgency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		
		this.personSection.addFormField(this.personTaxNumber);
		this.personSection.addFormField(this.personGender);
		this.personSection.addFormField(this.personBirthDate);
		this.personSection.addFormField(this.personClientNumber);
		this.personSection.addFormField(this.personInsurangeAgency);
		this.addSection(this.personSection);
		
		//company fields
		this.companySection = new FormViewSection("Companhia");
		this.companyTaxNumber = new TextBoxFormField("NIPC");
		this.companyCAE = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CAEs, "CAE Principal");
		this.companyGrievousCAE = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CAEs, "CAE Mais Gravoso");
		this.companyActivityNotes = new TextAreaFormField("Notas da Actividade");
		this.companyProductNotes = new TextAreaFormField("Notas do Produto");
		this.companyBusinessVolume = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.SALES_VOLUMES, "Volume de Facturação");
		this.companyEuropeanUnionEntity = new TextBoxFormField("Entidade da União Europeia");
		this.companyClientNumber = new TextBoxFormField("Número de Cliente Interno");
		
		this.companySection.addFormField(this.companyTaxNumber);
		this.companySection.addFormField(this.companyCAE);
		this.companySection.addFormField(this.companyGrievousCAE);
		this.companySection.addFormField(this.companyActivityNotes);
		this.companySection.addFormField(this.companyProductNotes);
		this.companySection.addFormField(this.companyBusinessVolume);
		this.companySection.addFormField(this.companyEuropeanUnionEntity);
		this.companySection.addFormField(this.companyClientNumber);
		this.addSection(this.companySection);
		
		//equipment fields
		this.equipmentSection = new FormViewSection("Equipamento");
		this.equipmentMakeAndModel = new TextBoxFormField("Marca e Modelo");
		this.equipmentDescription = new TextAreaFormField("Descrição");
		this.equipmentFirstRegistryDate = new DatePickerFormField("Data do Primeiro Registo");
		this.equipmentManufactureYear = new TextBoxFormField("Data de Fabrico");
		this.equipmentClientInternalId = new TextBoxFormField("Identificação no Cliente");
		this.equipmentInsuranceCompanyInternalVehicleId = new TextBoxFormField("Identificação na Seguradora");
		
		this.equipmentSection.addFormField(this.equipmentMakeAndModel);
		this.equipmentSection.addFormField(this.equipmentDescription);
		this.equipmentSection.addFormField(this.equipmentFirstRegistryDate);
		this.equipmentSection.addFormField(this.equipmentManufactureYear);
		this.equipmentSection.addFormField(this.equipmentClientInternalId);
		this.equipmentSection.addFormField(this.equipmentInsuranceCompanyInternalVehicleId);
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
		this.animalAge = new TextBoxFormField("Idade (anos)");
		this.animalCityRegistryNumber = new TextBoxFormField("Número de Registo");
		this.animalElectronicTagId = new TextBoxFormField("Identificação Electrónica");
		
		this.animalSection.addFormField(this.animalSpecies);
		this.animalSection.addFormField(this.animalRace);
		this.animalSection.addFormField(this.animalAge);
		this.animalSection.addFormField(this.animalCityRegistryNumber);
		this.animalSection.addFormField(this.animalElectronicTagId);
		this.addSection(this.animalSection);
		
		this.setValue(new InsuredObject());
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
	
	protected void showTypeSection(boolean show){
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
	public InsuredObject getInfo() {
		InsuredObject result = this.value;
		
		//TODO
		
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
		animalSpecies.setValue(info.race);
//		animalAge.setValue(info.age);
//		animalCityRegistryNumber.setValue(info.cityRegistryNumber);
//		animalElectronicTagId
//		protected TextBoxFormField animalElectronicTagId;
	}

}
