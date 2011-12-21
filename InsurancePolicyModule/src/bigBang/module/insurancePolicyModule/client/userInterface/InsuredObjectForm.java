package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObject.CoverageData;
import bigBang.definitions.shared.InsuredObject.Exercise;
import bigBang.definitions.shared.InsuredObject.HeaderData;
import bigBang.definitions.shared.InsuredObject.HeaderData.FixedField;
import bigBang.definitions.shared.InsuredObject.HeaderData.VariableField;
import bigBang.definitions.shared.InsuredObject.HeaderData.VariableField.VariableValue;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.TwoKeyTable;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.HeaderCell;
import bigBang.library.client.userInterface.TwoKeyTableView;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class InsuredObjectForm extends FormView<InsuredObject> {

	public class DynFormField extends FormField<String> {

		public String id;
		protected FormField<String> field;
		protected FixedField headerField;
		protected String coverageId;

		public DynFormField(FixedField field){
			super();
			this.id = field.fieldId;
			this.headerField = field;

			switch(field.type) {
			case LIST:
				ExpandableListBoxFormField listField = new ExpandableListBoxFormField(field.fieldName);
				listField.setEditable(true);
				listField.setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+field.fieldId, null);
				this.field = listField;
				break;
			case REFERENCE:
				ExpandableListBoxFormField referenceListField = new ExpandableListBoxFormField(field.fieldName);
				referenceListField.setEditable(true);
				referenceListField.setListId(field.refersToId, null);
				this.field = referenceListField;
				break;
			case NUMERIC:
				this.field = new TextBoxFormField(field.fieldName, new FieldValidator<String>() {

					@Override
					public boolean isValid(String value) {
						try{
							Integer.parseInt(value);
						}catch(Exception e){
							return false;
						}
						return true;
					}

					@Override
					public boolean isMandatory() {
						return false;
					}

					@Override
					public String getErrorMessage() {
						return "Apenas valores numéricos";
					}
				});
				break;
			case TEXT:
				this.field = new TextBoxFormField(field.fieldName);
				break;
			case BOOLEAN:
				RadioButtonFormField radioField = new RadioButtonFormField(field.fieldName);
				radioField.addOption("1", "Sim");
				radioField.addOption("0", "Não");
				this.field = radioField;
				break;
			case DATE:
//				DatePickerFormField dateField = new DatePickerFormField(field.fieldName);
//				this.field = dateField;
				break;
			default:
				break;
			}
			this.field.setUnitsLabel(field.unitsLabel);

			initWidget(this.field);
		}

		@Override
		public void clear() {
			this.field.clear();
		}

		@Override
		public void setReadOnly(boolean readonly) {
			this.field.setReadOnly(readonly);
		}

		@Override
		public boolean isReadOnly() {
			return this.field.isReadOnly();
		}

		@Override
		public void setLabelWidth(String width) {
			this.field.setLabelWidth(width);
		}
		
		@Override
		public void setFieldWidth(String width) {
			this.field.setFieldWidth(width);
		}
		
		@Override
		public void setValue(String value, boolean fireEvents) {
			this.field.setValue(value, fireEvents);
		}
		
		@Override
		public String getValue() {
			return this.field.getValue() == null ? null : this.field.getValue().toString();
		}
	}
	
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
	protected Collection<FormViewSection> dynamicFieldSections;
	protected TwoKeyTableView dynamicVariableHeaderDataTable;
	
	protected Collection<DynFormField> dynamicFixedHeaderFields, dynamicVariableHeaderFields;

	public InsuredObjectForm(){
		this.dynamicFieldSections = new ArrayList<FormViewSection>();
		this.dynamicFixedHeaderFields = new ArrayList<InsuredObjectForm.DynFormField>();
		this.dynamicVariableHeaderFields = new ArrayList<InsuredObjectForm.DynFormField>();
		this.dynamicVariableHeaderDataTable = new TwoKeyTableView();
		
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
		this.companySection = new FormViewSection("Companhia");
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
		this.equipmentSection = new FormViewSection("Equipamento");
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
		this.addSection(dynamicHeaderSection);
		
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

		//common fields
		result.unitIdentification = identification.getValue();
		result.address = address.getValue();
		result.inclusionDate = inclusionDate.getStringValue();
		result.exclusionDate = exclusionDate.getStringValue();
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
		animalRace.setValue(info.race);
		animalBirthYear.setValue(info.birthYear);
		animalCityRegistryNumber.setValue(info.cityRegistryNumber);
		animalElectronicTagId.setValue(info.electronicIdTag);
		
		setDynamicHeaderData(info.exercises, info.headerData);
		setDynamicData(info.coverageData);
	}
	
	protected void setDynamicHeaderData(Exercise[] exercises, HeaderData headerData){
		if(headerData == null) {return;}
		this.dynamicHeaderSection.clear();
		setDynamicFixedHeaderData(headerData.fixedFields);
		setDynamicVariableHeaderData(exercises, headerData.variableFields);
	}
	
	protected void setDynamicFixedHeaderData(FixedField[] fields){
		clearDynamicFixedHeaderData();
		if(fields == null){
			clearDynamicFixedHeaderData();
		}else{
			for(int i = 0; i < fields.length; i++) {
				FixedField field = fields[i];
				DynFormField formField = new DynFormField(field);
				formField.setFieldWidth("175px");
				formField.setValue(field.value);
				this.dynamicFixedHeaderFields.add(formField);
				this.dynamicHeaderSection.addFormField(formField, true);
			}
		}
	}
	
	protected void clearDynamicFixedHeaderData(){
		this.dynamicHeaderSection.unregisterAllFormFields();
		for(DynFormField f : this.dynamicFixedHeaderFields){
			this.dynamicFixedHeaderFields.remove(f);
			f.removeFromParent();
		}
		this.dynamicFixedHeaderFields.clear();	
	}
	
	protected FixedField[] getDynamicFixedFieldHeaderData(){
		return null; //TODO
	}
	
	protected void setDynamicVariableHeaderData(Exercise[] exercises, VariableField[] fields){
		if(exercises == null){
			clearDynamicVariableHeaderData();
			return;
		}
		if(fields == null){
			return;
		}

		HeaderCell[] rowHeaders = new HeaderCell[fields.length];
		HeaderCell[] columnHeaders = new HeaderCell[exercises.length];
		
		for(int i = 0; i < fields.length; i++) {
			HeaderCell header = new HeaderCell();
			header.id = fields[i].fieldId;
			header.text = fields[i].fieldName;
			rowHeaders[i] = header;
		}
		for(int i = 0; i < exercises.length; i++) {
			HeaderCell header = new HeaderCell();
			header.id = exercises[i].id;
			header.text = exercises[i].label;
			columnHeaders[i] = header;
		}
		dynamicVariableHeaderDataTable.setHeaders(rowHeaders, columnHeaders);

//		for(int i = 0; i < fields.length; i++){
//			VariableField field = fields[i];
//			Field tableField = new Field();
//			tableField.id = field.fieldId;
//			tableField.value = field.
//		}

		this.dynamicHeaderSection.addWidget(this.dynamicVariableHeaderDataTable);
		this.dynamicVariableHeaderDataTable.render();
	}
	
	protected void clearDynamicVariableHeaderData(){
		//TODO
	}
	
	protected VariableField[] getDynamicVariableHeaderData(){
		return null;//TODO
	}
	
	protected void setDynamicData(CoverageData[] data){
		if(data == null) {
			removeDynamicData();
			return;
		}
		for(int i = 0; i < data.length; i++){
			CoverageData covData = data[i];
			FormViewSection section = new FormViewSection("Cobertura " + covData.coverageLabel);
			
			for(int j = 0; j < covData.fixedFields.length; j++){
				FixedField field = covData.fixedFields[j];
				DynFormField formField = new DynFormField(field);
				formField.setFieldWidth("175px");
				formField.setValue(field.value);
				section.addFormField(formField, true);
			}
			addSection(section);
		}
	}
	
	protected CoverageData[] fillDynamicData(CoverageData[] data){
		return null; //TODO
	}
	
	protected void removeDynamicData(){
		for(FormViewSection s : this.dynamicFieldSections){
			s.unregisterAllFormFields();
			s.removeFromParent();
		}
	}

	public void setForPolicyStatusNew(InsurancePolicyStub.PolicyStatus status){
		switch(status){
		case PROVISIONAL:
			this.type.setReadOnly(true);
			this.inclusionDate.setVisible(false);
			this.exclusionDate.setVisible(false);
			break;
		case VALID:
			this.type.setReadOnly(true);
			this.inclusionDate.setVisible(true);
			this.inclusionDate.setVisible(true);
			break;
		case OBSOLETE:
			this.type.setReadOnly(true);
			break;
		}
	}

}
