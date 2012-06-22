package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
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
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericFormFieldWrapper;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.HeaderCell;
import bigBang.library.client.userInterface.TwoKeyTable.Type;
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
				this.field = new NumericFormFieldWrapper();
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

		@Override
		public void focus() {
			field.focus();
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

	protected FormViewSection commonSection, typeSection, personSection, companySection, equipmentSection, locationSection, animalSection, dynamicHeaderSection;
	protected Collection<FormViewSection> dynamicFieldSections;
	protected TwoKeyTableView dynamicVariableHeaderDataTable;
	protected Map<String, TwoKeyTableView> coverageTables;
	protected Map<String, Collection<DynFormField>> coverageFixedFields;

	protected Collection<DynFormField> dynamicFixedHeaderFields;

	public InsuredObjectForm(){
		this.scrollWrapper.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		
		this.coverageFixedFields = new HashMap<String, Collection<DynFormField>>();
		this.coverageTables = new HashMap<String, TwoKeyTableView>();
		this.dynamicFieldSections = new ArrayList<FormViewSection>();
		this.dynamicFixedHeaderFields = new ArrayList<InsuredObjectForm.DynFormField>();
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

		result.headerData = getDynamicHeaderData(result.exercises, result.headerData);
		result.coverageData = getCoverageData(result.exercises, result.coverageData);

		return result;
	}

	private CoverageData[] getCoverageData(Exercise[] exercises, CoverageData[] coverageData) {
		for(int i = 0; i < coverageData.length; i++) {
			String coverageId = coverageData[i].coverageId;

			//FIXED FIELDS
			Collection<DynFormField> fixedFields = this.coverageFixedFields.get(coverageId);
			if(fixedFields == null){continue;}
			FixedField[] coverageFixedFields = coverageData[i].fixedFields;
			for(int j = 0; j < coverageFixedFields.length; j++){
				for(DynFormField field : fixedFields){
					if(coverageFixedFields[j].fieldId.equalsIgnoreCase(field.id)){
						coverageFixedFields[j].value = field.getValue();
						break;
					}
				}
			}

			//EXERCISE-VARIABLE FIELDS
			TwoKeyTableView coverageTable = this.coverageTables.get(coverageId);
			VariableField[] variableFields = coverageData[i].variableFields;
			for(int j = 0; j < variableFields.length; j++) {
				VariableField variableField = variableFields[j];
				for(int k = 0; k < exercises.length; k++) {
					Field field = coverageTable.getValue(variableField.fieldId, exercises[k].id);
					variableField.data[k].value = field == null ? null : field.value;
				}
			}
		}
		return coverageData;
	}

	private HeaderData getDynamicHeaderData(Exercise[] exercises, HeaderData headerData) {
		FixedField[] fixedFields = headerData.fixedFields;

		for(int i = 0; i < fixedFields.length; i++){
			FixedField fixedField = fixedFields[i];
			for(DynFormField f : this.dynamicFixedHeaderFields){
				if(f.id.equalsIgnoreCase(fixedField.fieldId)){
					fixedField.value = f.getValue();
					break;
				}
			}
		}

		VariableField[] variableFields = headerData.variableFields;
		for(int i = 0; i < variableFields.length; i++) {
			VariableField variableField = variableFields[i];
			VariableValue[] values = variableField.data;
			for(int j = 0; j < values.length; j++) {
				VariableValue value = values[j];
				Field tableField = this.dynamicVariableHeaderDataTable.getValue(variableField.fieldId, exercises[j].id);
				value.value = tableField.value;
			}
		}
		return headerData;
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

		setDynamicHeaderData(info.exercises, info.headerData);
		setDynamicData(info.exercises, info.coverageData);
	}

	protected void setDynamicHeaderData(Exercise[] exercises, HeaderData headerData){
		if(headerData == null) {return;}
		this.dynamicHeaderSection.clear();
		setDynamicFixedHeaderData(headerData.fixedFields);
		setDynamicVariableHeaderData(exercises, headerData.variableFields);
	}

	protected void setDynamicFixedHeaderData(FixedField[] fields){
		clearDynamicFixedHeaderData();
		if(fields != null){
			for(int i = 0; i < fields.length; i++) {
				FixedField field = fields[i];
				DynFormField formField = new DynFormField(field);
				formField.setReadOnly(this.isReadOnly());
				formField.setFieldWidth("175px");
				formField.setValue(field.value);
				this.dynamicFixedHeaderFields.add(formField);
				this.dynamicHeaderSection.addFormField(formField, i != (fields.length - 1));
			}
		}
	}

	protected void clearDynamicFixedHeaderData(){
		this.dynamicHeaderSection.unregisterAllFormFields();
		Iterator<DynFormField> iterator = this.dynamicFixedHeaderFields.iterator();

		while(iterator.hasNext()){
			DynFormField field = iterator.next();
			field.removeFromParent();
			iterator.remove();
		}
	}

	protected FixedField[] getDynamicFixedFieldHeaderData(){
		FixedField[] result = new FixedField[this.dynamicFixedHeaderFields.size()];
		int i = 0;
		for(DynFormField field : this.dynamicFixedHeaderFields){
			FixedField f = field.headerField;
			f.value = field.getValue();
			result[i] = f;
			i++;
		}
		return result;
	}

	protected void setDynamicVariableHeaderData(Exercise[] exercises, VariableField[] fields){
		this.dynamicVariableHeaderDataTable.removeFromParent();
		removeDynamicData();

		this.dynamicVariableHeaderDataTable = new TwoKeyTableView();

		if(exercises == null){
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

		for(int i = 0; i < fields.length; i++){
			VariableField field = fields[i];

			for(int j = 0; j < field.data.length; j++) {
				VariableValue fieldValue = field.data[j];
				String exerciseId = exercises[j].id;
				Field tableField = new Field();
				tableField.value = fieldValue.value;
				switch(field.type){
				case BOOLEAN:
					tableField.type = Type.BOOLEAN;
					break;
				case DATE:
					tableField.type = Type.DATE;
					break;
				case LIST:
					tableField.type = Type.LIST;
					tableField.id = field.fieldId;
					break;
				case NUMERIC:
					tableField.type = Type.NUMERIC;
					break;
				case REFERENCE:
					tableField.type = Type.REFERENCE;
					tableField.reference = field.refersToId;
					break;
				case TEXT:
					tableField.type = Type.TEXT;
					break;
				}
				tableField.value = fieldValue.value;
				this.dynamicVariableHeaderDataTable.setValue(field.fieldId, exerciseId, tableField);
			}
		}

		this.dynamicVariableHeaderDataTable.render();
		this.dynamicVariableHeaderDataTable.setReadOnly(this.isReadOnly());
		this.dynamicHeaderSection.addWidget(this.dynamicVariableHeaderDataTable, false);
	}

	protected void setDynamicData(Exercise[] exercises, CoverageData[] data){
		removeDynamicData();
		if(data == null) {
			return;
		}
		HeaderCell[] columnHeaders = new HeaderCell[exercises.length];
		for(int i = 0; i < exercises.length; i++) {
			Exercise exercise = exercises[i];
			HeaderCell header = new HeaderCell();
			header.id = exercise.id;
			header.text = exercise.label;
			columnHeaders[i] = header;
		}

		for(int i = 0; i < data.length; i++){
			CoverageData covData = data[i];
			if(covData.fixedFields.length == 0 && covData.variableFields.length == 0){
				continue;
			}

			FormViewSection section = new FormViewSection("Cobertura " + covData.coverageLabel);
			ArrayList<DynFormField> coverageFields = new ArrayList<DynFormField>();

			for(int j = 0; j < covData.fixedFields.length; j++){
				FixedField field = covData.fixedFields[j];
				DynFormField formField = new DynFormField(field);
				formField.setReadOnly(this.isReadOnly());
				formField.setFieldWidth("175px");
				formField.setValue(field.value);
				section.addFormField(formField, j != (covData.fixedFields.length - 1));
				coverageFields.add(formField);
			}
			this.coverageFixedFields.put(covData.coverageId, coverageFields);

			TwoKeyTableView table = new TwoKeyTableView();
			HeaderCell[] rowHeaders = new HeaderCell[covData.variableFields.length]; 

			if(covData.variableFields.length != 0){
				for(int j = 0; j < covData.variableFields.length; j++) {
					HeaderCell header = new HeaderCell();
					header.id = covData.variableFields[j].fieldId;
					header.text = covData.variableFields[j].fieldName;
					rowHeaders[j] = header;
				}

				table.setHeaders(rowHeaders, columnHeaders);

				for(int j = 0; j < exercises.length; j++) {
					for(int k = 0; k < covData.variableFields.length; k++){
						VariableField field = covData.variableFields[k];
						Field tableField = new Field();
						switch(field.type){
						case BOOLEAN:
							tableField.type = Type.BOOLEAN;
							break;
						case DATE:
							tableField.type = Type.DATE;
							break;
						case LIST:
							tableField.type = Type.LIST;
							tableField.id = field.fieldId;
							break;
						case NUMERIC:
							tableField.type = Type.NUMERIC;
							break;
						case REFERENCE:
							tableField.type = Type.REFERENCE;
							tableField.reference = field.refersToId;
							break;
						case TEXT:
							tableField.type = Type.TEXT;
							break;
						}
						tableField.value = field.data[j].value;
						table.setValue(field.fieldId, exercises[j].id, tableField);
					}
				}
				table.render();
				table.setReadOnly(this.isReadOnly());
				section.addWidget(table, false);
				this.coverageTables.put(covData.coverageId, table);
			}
			this.dynamicFieldSections.add(section);
			addSection(section);
		}
	}

	protected void removeDynamicData(){
		for(FormViewSection s : this.dynamicFieldSections){
			s.unregisterAllFormFields();
			s.removeFromParent();
		}
		this.coverageFixedFields.clear();
		this.coverageTables.clear();
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

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(this.dynamicVariableHeaderDataTable != null){
			this.dynamicVariableHeaderDataTable.setReadOnly(readOnly);
		}
		if(this.coverageTables != null) {
			for(TwoKeyTableView table : this.coverageTables.values()){
				table.setReadOnly(readOnly);
			}
		}
	}

}
